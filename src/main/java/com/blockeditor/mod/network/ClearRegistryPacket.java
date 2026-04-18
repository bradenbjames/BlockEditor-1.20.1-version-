package com.blockeditor.mod.network;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;

public class ClearRegistryPacket {
    private final boolean clearHistory;

    public ClearRegistryPacket(boolean clearHistory) {
        this.clearHistory = clearHistory;
    }

    public static void encode(ClearRegistryPacket packet, PacketByteBuf buffer) {
        buffer.writeBoolean(packet.clearHistory);
    }

    public static ClearRegistryPacket decode(PacketByteBuf buffer) {
        return new ClearRegistryPacket(buffer.readBoolean());
    }

    public static void handle(ClearRegistryPacket packet, ServerPlayerEntity player) {
        if (player != null) {
            ServerWorld level = player.getServerWorld();
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
            player.sendMessage(Text.literal(message));

            BlockEditorMod.LOGGER.info("Player {} cleared user block registry ({} blocks)", player.getName().getString(), clearedCount);
        }
    }
}