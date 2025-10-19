package com.blockeditor.mod.client;

import com.blockeditor.mod.BlockEditorMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final String CATEGORY = "key.categories." + BlockEditorMod.MOD_ID;

    public static final KeyMapping OPEN_BLOCK_EDITOR = new KeyMapping(
        "key." + BlockEditorMod.MOD_ID + ".open_editor",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_B,
        CATEGORY
    );
}