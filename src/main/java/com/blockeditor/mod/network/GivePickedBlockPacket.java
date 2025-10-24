package com.blockeditor.mod.network;

import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server-verified packet to give the player the block they middle-clicked.
 * Carries only the BlockPos; server validates and reconstructs the ItemStack
 * from the actual block + block entity data to prevent spoofing.
 */
public class GivePickedBlockPacket {
    private final BlockPos pos;

    public GivePickedBlockPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(GivePickedBlockPacket pkt, FriendlyByteBuf buf) {
        buf.writeBlockPos(pkt.pos);
    }

    public static GivePickedBlockPacket decode(FriendlyByteBuf buf) {
        return new GivePickedBlockPacket(buf.readBlockPos());
    }

    public static void handle(GivePickedBlockPacket pkt, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            if (!level.isLoaded(pkt.pos)) return;

            BlockState state = level.getBlockState(pkt.pos);
            Block block = state.getBlock();
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
            // Use the actual mod id constant instead of a hardcoded namespace
            if (blockId == null || !com.blockeditor.mod.BlockEditorMod.MOD_ID.equals(blockId.getNamespace())) return;

            String path = blockId.getPath();
            boolean isDynamicOrUser = path.startsWith("dynamic_block") || path.startsWith("u_") || path.startsWith("user_");
            if (!isDynamicOrUser) return;

            BlockEntity be = level.getBlockEntity(pkt.pos);
            if (!(be instanceof DynamicBlockEntity dbe)) return;

            // Build ItemStack from actual block and BE data
            ItemStack stack = new ItemStack(block);

            int color = dbe.getColor();
            String hex = String.format("%06X", color);
            String mimic = dbe.getMimicBlock();

            CompoundTag tag = new CompoundTag();
            tag.putString("Color", hex);
            tag.putString("OriginalBlock", mimic);
            tag.putInt("Red", (color >> 16) & 0xFF);
            tag.putInt("Green", (color >> 8) & 0xFF);
            tag.putInt("Blue", color & 0xFF);

            // Try to recover CustomName if this is a UserBlock via the registry reverse map
            String maybeCustomName = null;
            if (path.startsWith("u_")) {
                UserBlockRegistry registry = UserBlockRegistry.get(level);
                String identifier = path.substring(2); // e.g. u_wool1 -> wool1
                maybeCustomName = registry.getCustomName(identifier);
            }

            stack.setTag(tag);
            if (maybeCustomName != null && !maybeCustomName.isBlank()) {
                tag.putString("CustomName", maybeCustomName);
                stack.setHoverName(net.minecraft.network.chat.Component.literal(maybeCustomName));
            } else {
                // Fallback readable name
                String mimicName = mimic.replace("minecraft:", "").replace('_', ' ');
                stack.setHoverName(net.minecraft.network.chat.Component.literal("§r" + mimicName + " §7(#" + hex + ")"));
            }

            // Enforce only one copy for custom-named user blocks in Creative
            boolean isCreative = player.getAbilities().instabuild;

            // Match predicate: same item, same Color & OriginalBlock, and if CustomName present must match
            java.util.function.BiPredicate<ItemStack, ItemStack> sameCustomBlock = (a, b) -> {
                if (a.isEmpty() || b.isEmpty()) return false;
                if (!a.is(b.getItem())) return false;
                CompoundTag ta = a.getTag();
                CompoundTag tb = b.getTag();
                if (ta == null || tb == null) return false;
                String ca = ta.getString("Color");
                String cb = tb.getString("Color");
                String oa = ta.getString("OriginalBlock");
                String ob = tb.getString("OriginalBlock");
                if (!ca.equalsIgnoreCase(cb) || !oa.equals(ob)) return false;
                if (ta.contains("CustomName") || tb.contains("CustomName")) {
                    return ta.getString("CustomName").equals(tb.getString("CustomName"));
                }
                return true;
            };

            // Search for existing matching stack first
            int foundSlot = -1;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack cur = player.getInventory().getItem(i);
                if (sameCustomBlock.test(cur, stack)) {
                    foundSlot = i;
                    break;
                }
            }

            if (foundSlot != -1) {
                // Move existing to selected slot if possible, else swap
                int selected = player.getInventory().selected;
                if (player.getInventory().getItem(selected).isEmpty()) {
                    player.getInventory().setItem(selected, player.getInventory().getItem(foundSlot));
                    player.getInventory().setItem(foundSlot, ItemStack.EMPTY);
                } else if (foundSlot != selected) {
                    ItemStack tmp = player.getInventory().getItem(selected);
                    player.getInventory().setItem(selected, player.getInventory().getItem(foundSlot));
                    player.getInventory().setItem(foundSlot, tmp);
                }

                // If creative, also de-duplicate extra copies beyond one
                if (isCreative) {
                    boolean keepOne = false;
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack cur = player.getInventory().getItem(i);
                        if (sameCustomBlock.test(cur, stack)) {
                            if (!keepOne) {
                                // Normalize to a single item in this slot
                                cur.setCount(1);
                                keepOne = true;
                            } else {
                                player.getInventory().setItem(i, ItemStack.EMPTY);
                            }
                        }
                    }
                }
                player.inventoryMenu.broadcastChanges();
                return;
            }

            // Not found: give/add the stack
            // If creative, ensure only one is ever present by setting count=1
            if (isCreative) stack.setCount(1);

            int selectedSlot = player.getInventory().selected;
            boolean placed = false;

            // Prefer selected slot if empty
            if (player.getInventory().getItem(selectedSlot).isEmpty()) {
                player.getInventory().setItem(selectedSlot, stack);
                placed = true;
            } else {
                // Try empty hotbar slot
                for (int i = 0; i < 9; i++) {
                    if (player.getInventory().getItem(i).isEmpty()) {
                        player.getInventory().setItem(i, stack);
                        player.getInventory().selected = i;
                        placed = true;
                        break;
                    }
                }
                // Try empty main inventory slot
                if (!placed) {
                    for (int i = 9; i < player.getInventory().getContainerSize(); i++) {
                        if (player.getInventory().getItem(i).isEmpty()) {
                            player.getInventory().setItem(i, stack);
                            // Move it to selected
                            ItemStack curSel = player.getInventory().getItem(selectedSlot);
                            player.getInventory().setItem(selectedSlot, stack);
                            player.getInventory().setItem(i, curSel);
                            placed = true;
                            break;
                        }
                    }
                }
                // As a last resort, try add() which stacks if possible
                if (!placed) {
                    if (player.getInventory().add(stack)) {
                        placed = true;
                    } else {
                        // Drop on ground if no space
                        player.drop(stack, false);
                        player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("§c§lInventory Full! §r§7Block dropped on ground"),
                            true
                        );
                    }
                }
            }

            if (placed) {
                String show = (maybeCustomName != null && !maybeCustomName.isBlank()) ? maybeCustomName : ("#" + hex);
                player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§a§lPicked! §r§f" + show + " §7in your hand"),
                    true
                );
            }
            player.inventoryMenu.broadcastChanges();
        });
        ctx.setPacketHandled(true);
    }
}
