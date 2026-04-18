package com.blockeditor.mod.network;

import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.BlockState;

public class GivePickedBlockPacket {
    private final BlockPos pos;

    public GivePickedBlockPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(GivePickedBlockPacket pkt, PacketByteBuf buf) {
        buf.writeBlockPos(pkt.pos);
    }

    public static GivePickedBlockPacket decode(PacketByteBuf buf) {
        return new GivePickedBlockPacket(buf.readBlockPos());
    }

    public static void handle(GivePickedBlockPacket pkt, ServerPlayerEntity player) {
        if (player == null) return;

            ServerWorld level = player.getServerWorld();
            if (!level.getChunkManager().isChunkLoaded(pkt.pos.getX() >> 4, pkt.pos.getZ() >> 4)) return;

            BlockState state = level.getBlockState(pkt.pos);
            Block block = state.getBlock();
            Identifier blockId = Registries.BLOCK.getId(block);
            // Use the actual mod id constant instead of a hardcoded namespace
            if (!com.blockeditor.mod.BlockEditorMod.MOD_ID.equals(blockId.getNamespace())) {
                return;
            }

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

            NbtCompound tag = new NbtCompound();
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

            stack.setNbt(tag);
            if (maybeCustomName != null && !maybeCustomName.isBlank()) {
                tag.putString("CustomName", maybeCustomName);
                stack.setCustomName(net.minecraft.text.Text.literal(maybeCustomName));
            } else {
                // Fallback readable name
                String mimicName = mimic.replace("minecraft:", "").replace('_', ' ');
                stack.setCustomName(net.minecraft.text.Text.literal("§r" + mimicName + " §7(#" + hex + ")"));
            }

            // Enforce only one copy for custom-named user blocks in Creative
            boolean isCreative = player.getAbilities().creativeMode;

            // Match predicate: same item, same Color & OriginalBlock, and if CustomName present must match
            java.util.function.BiPredicate<ItemStack, ItemStack> sameCustomBlock = (a, b) -> {
                if (a.isEmpty() || b.isEmpty()) return false;
                if (!a.isOf(b.getItem())) return false;
                NbtCompound ta = a.getNbt();
                NbtCompound tb = b.getNbt();
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
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack cur = player.getInventory().getStack(i);
                if (sameCustomBlock.test(cur, stack)) {
                    foundSlot = i;
                    break;
                }
            }

            if (foundSlot != -1) {
                // Move existing to selected slot if possible, else swap
                int selected = player.getInventory().selectedSlot;
                if (player.getInventory().getStack(selected).isEmpty()) {
                    player.getInventory().setStack(selected, player.getInventory().getStack(foundSlot));
                    player.getInventory().setStack(foundSlot, ItemStack.EMPTY);
                } else if (foundSlot != selected) {
                    ItemStack tmp = player.getInventory().getStack(selected);
                    player.getInventory().setStack(selected, player.getInventory().getStack(foundSlot));
                    player.getInventory().setStack(foundSlot, tmp);
                }

                // If creative, also de-duplicate extra copies beyond one
                if (isCreative) {
                    boolean keepOne = false;
                    for (int i = 0; i < player.getInventory().size(); i++) {
                        ItemStack cur = player.getInventory().getStack(i);
                        if (sameCustomBlock.test(cur, stack)) {
                            if (!keepOne) {
                                // Normalize to a single item in this slot
                                cur.setCount(1);
                                keepOne = true;
                            } else {
                                player.getInventory().setStack(i, ItemStack.EMPTY);
                            }
                        }
                    }
                }
                player.playerScreenHandler.sendContentUpdates();
                return;
            }

            // Not found: give/add the stack following placement policy
            if (isCreative) stack.setCount(1);

            boolean placed = false;
            int containerSize = player.getInventory().size();

            // 1) Try an empty hotbar slot and select it
            for (int i = 0; i < 9; i++) {
                if (player.getInventory().getStack(i).isEmpty()) {
                    player.getInventory().setStack(i, stack);
                    player.getInventory().selectedSlot = i;
                    placed = true;
                    break;
                }
            }

            // 2) Try an empty main inventory slot
            if (!placed) {
                for (int i = 9; i < containerSize; i++) {
                    if (player.getInventory().getStack(i).isEmpty()) {
                        player.getInventory().setStack(i, stack);
                        placed = true;
                        break;
                    }
                }
            }

            // 3) Replace a non-hotbar slot: drop replaced stack, insert new one; keep hotbar untouched
            if (!placed) {
                for (int i = containerSize - 1; i >= 9; i--) {
                    ItemStack toReplace = player.getInventory().getStack(i);
                    if (!toReplace.isEmpty()) {
                        ItemStack dropCopy = toReplace.copy();
                        player.getInventory().setStack(i, ItemStack.EMPTY);
                        player.dropItem(dropCopy, false);
                        player.getInventory().setStack(i, stack);
                        placed = true;
                        break;
                    }
                }
            }

            if (placed) {
                String show = (maybeCustomName != null && !maybeCustomName.isBlank()) ? maybeCustomName : ("#" + hex);
                boolean inHand = player.getInventory().selectedSlot < 9 && player.getInventory().getStack(player.getInventory().selectedSlot) == stack;
                player.sendMessage(
                    net.minecraft.text.Text.literal("§a§lPicked! §r§f" + show + (inHand ? " §7(in your hand)" : " §7(in inventory)")),
                    true
                );
            } else {
                // Fallback (should not happen): drop
                player.dropItem(stack, false);
                player.sendMessage(
                    net.minecraft.text.Text.literal("§c§lInventory Full! §r§7Block dropped on ground"),
                    true
                );
            }
            player.playerScreenHandler.sendContentUpdates();
    }
}
