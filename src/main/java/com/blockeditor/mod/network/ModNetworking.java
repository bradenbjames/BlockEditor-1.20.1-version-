package com.blockeditor.mod.network;

import com.blockeditor.mod.BlockEditorMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";

    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(BlockEditorMod.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void register() {
        BlockEditorMod.LOGGER.debug("Registering network packets");

        INSTANCE.registerMessage(0, CreateBlockPacket.class,
            CreateBlockPacket::encode,
            CreateBlockPacket::decode,
            CreateBlockPacket::handle
        );

        INSTANCE.registerMessage(1, ClearRegistryPacket.class,
            ClearRegistryPacket::encode,
            ClearRegistryPacket::decode,
            ClearRegistryPacket::handle
        );

        INSTANCE.registerMessage(2, GivePickedBlockPacket.class,
            GivePickedBlockPacket::encode,
            GivePickedBlockPacket::decode,
            GivePickedBlockPacket::handle
        );
        BlockEditorMod.LOGGER.debug("Packet registration complete");
    }

    public static void sendToServer(Object message) {
        BlockEditorMod.LOGGER.trace("Sending packet to server: {}", message.getClass().getSimpleName());
        INSTANCE.send(PacketDistributor.SERVER.noArg(), message);
    }
}