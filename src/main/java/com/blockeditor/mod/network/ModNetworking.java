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
        System.out.println("ModNetworking.register called - registering packets");

        INSTANCE.registerMessage(0, CreateBlockPacket.class,
            CreateBlockPacket::encode,
            CreateBlockPacket::decode,
            CreateBlockPacket::handle
        );

        System.out.println("ModNetworking.register completed - CreateBlockPacket registered");
    }

    public static void sendToServer(Object message) {
        System.out.println("ModNetworking.sendToServer called with: " + message.getClass().getSimpleName());
        INSTANCE.send(PacketDistributor.SERVER.noArg(), message);
    }
}