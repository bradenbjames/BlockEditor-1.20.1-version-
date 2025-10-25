package com.blockeditor.mod.network;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClearRegistryPacket {
    private final boolean clearHistory;

    public ClearRegistryPacket(boolean clearHistory) {
        this.clearHistory = clearHistory;
    }

    public static void encode(ClearRegistryPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.clearHistory);
    }

    public static ClearRegistryPacket decode(FriendlyByteBuf buffer) {
        return new ClearRegistryPacket(buffer.readBoolean());
    }

    public static void handle(ClearRegistryPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = player.serverLevel();
                UserBlockRegistry registry = UserBlockRegistry.get(level);
                
                int clearedCount = registry.clearAllUserBlocks();
                
                // Clear WorldEdit integration mappings
                try {
                    com.blockeditor.mod.integration.WorldEditIntegration.clearAllMappings();
                    BlockEditorMod.LOGGER.info("Cleared WorldEdit integration mappings");
                } catch (Exception e) {
                    BlockEditorMod.LOGGER.error("Failed to clear WorldEdit integration mappings", e);
                }
                
                // Send feedback to player
                String message = "Cleared " + clearedCount + " user blocks from server registry.";
                player.sendSystemMessage(Component.literal(message));

                BlockEditorMod.LOGGER.info("Player {} cleared user block registry ({} blocks)", player.getName().getString(), clearedCount);
            }
        });
        context.setPacketHandled(true);
    }
}