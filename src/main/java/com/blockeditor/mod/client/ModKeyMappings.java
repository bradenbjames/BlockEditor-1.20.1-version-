package com.blockeditor.mod.client;

import com.blockeditor.mod.BlockEditorMod;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final String CATEGORY = "key.categories." + BlockEditorMod.MOD_ID;

    public static final KeyBinding OPEN_BLOCK_EDITOR = new KeyBinding(
        "key." + BlockEditorMod.MOD_ID + ".open_editor",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        CATEGORY
    );
}