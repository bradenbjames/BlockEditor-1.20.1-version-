package com.blockeditor.mod.network;

import com.blockeditor.mod.BlockEditorMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ModNetworking {
    public static final Identifier CREATE_BLOCK_ID = new Identifier(BlockEditorMod.MOD_ID, "create_block");
    public static final Identifier CLEAR_REGISTRY_ID = new Identifier(BlockEditorMod.MOD_ID, "clear_registry");
    public static final Identifier GIVE_PICKED_BLOCK_ID = new Identifier(BlockEditorMod.MOD_ID, "give_picked_block");

    public static void register() {
        BlockEditorMod.LOGGER.debug("Registering network packets");

        ServerPlayNetworking.registerGlobalReceiver(CREATE_BLOCK_ID, (server, player, handler, buf, responseSender) -> {
            CreateBlockPacket packet = CreateBlockPacket.decode(buf);
            server.execute(() -> CreateBlockPacket.handle(packet, player));
        });

        ServerPlayNetworking.registerGlobalReceiver(CLEAR_REGISTRY_ID, (server, player, handler, buf, responseSender) -> {
            ClearRegistryPacket packet = ClearRegistryPacket.decode(buf);
            server.execute(() -> ClearRegistryPacket.handle(packet, player));
        });

        ServerPlayNetworking.registerGlobalReceiver(GIVE_PICKED_BLOCK_ID, (server, player, handler, buf, responseSender) -> {
            GivePickedBlockPacket packet = GivePickedBlockPacket.decode(buf);
            server.execute(() -> GivePickedBlockPacket.handle(packet, player));
        });

        BlockEditorMod.LOGGER.debug("Packet registration complete");
    }

    public static void sendToServer(CreateBlockPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        CreateBlockPacket.encode(packet, buf);
        ClientPlayNetworking.send(CREATE_BLOCK_ID, buf);
    }

    public static void sendToServer(ClearRegistryPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        ClearRegistryPacket.encode(packet, buf);
        ClientPlayNetworking.send(CLEAR_REGISTRY_ID, buf);
    }

    public static void sendToServer(GivePickedBlockPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        GivePickedBlockPacket.encode(packet, buf);
        ClientPlayNetworking.send(GIVE_PICKED_BLOCK_ID, buf);
    }
}