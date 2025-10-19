package com.blockeditor.mod.client.gui;

import com.blockeditor.mod.network.CreateBlockPacket;
import com.blockeditor.mod.network.ModNetworking;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import com.blockeditor.mod.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class BlockEditorScreen extends Screen {

    private String hexColor = "FFFFFF";
    private Block selectedBlock = Blocks.STONE;
    private int scrollOffset = 0;

    private EditBox hexBox;
    private EditBox searchBox;

    private List<Block> allBlocks = new ArrayList<>();
    private List<Block> filteredBlocks = new ArrayList<>();

    // History of created blocks
    private static List<CreatedBlockInfo> createdBlocksHistory = new ArrayList<>();

    private static final int BLOCKS_PER_ROW = 8;
    private static final int BLOCK_SIZE = 32;
    private static final int BLOCK_PADDING = 4;

    // Inner class to store created block info
    private static class CreatedBlockInfo {
        Block originalBlock;
        String hexColor;
        int color;

        CreatedBlockInfo(Block block, String hex, int color) {
            this.originalBlock = block;
            this.hexColor = hex;
            this.color = color;
        }
    }

    public BlockEditorScreen() {
        super(Component.literal("Block Editor"));
    }

    @Override
    protected void init() {
        super.init();

        // Get all registered blocks (filter to only allowed blocks)
        allBlocks.clear();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR) {
                // Only add blocks that pass our validation
                if (isFullSolidBlock(block)) {
                    allBlocks.add(block);
                }
            }
        }
        filteredBlocks = new ArrayList<>(allBlocks);

        int centerX = this.width / 2;

        // Calculate total width needed: search (180) + gap (10) + # (10) + hex (80) + gap (10) + color preview (40) + gap (10) + block preview (16) = 356
        int totalWidth = 180 + 10 + 10 + 80 + 10 + 40 + 10 + 16;
        int startX = centerX - (totalWidth / 2);

        // Search box - centered as part of the group
        searchBox = new EditBox(this.font, startX, 30, 180, 20, Component.literal("Search"));
        searchBox.setHint(Component.literal("Search blocks..."));
        this.addRenderableWidget(searchBox);

        // Hex color input box - centered as part of the group
        int hexY = 30;
        int hexX = startX + 180 + 10 + 10; // After search box + gap + # symbol width

        hexBox = new EditBox(this.font, hexX, hexY, 80, 20, Component.literal("Hex Color"));
        hexBox.setValue(hexColor);
        hexBox.setMaxLength(6);
        hexBox.setHint(Component.literal("FFFFFF"));
        this.addRenderableWidget(hexBox);

        // Buttons - centered together at bottom
        int gridEndY = 55 + (4 * (BLOCK_SIZE + BLOCK_PADDING));
        int buttonY = gridEndY + 40;
        int buttonSpacing = 10;
        int buttonWidth = 90;
        int totalButtonWidth = (buttonWidth * 2) + buttonSpacing;
        int buttonStartX = centerX - (totalButtonWidth / 2);

        // Create Block button
        this.addRenderableWidget(Button.builder(
            Component.literal("Create Block"),
            button -> createColoredBlock()
        ).bounds(buttonStartX, buttonY, buttonWidth, 20).build());

        // Cancel button
        this.addRenderableWidget(Button.builder(
            Component.literal("Cancel"),
            button -> this.onClose()
        ).bounds(buttonStartX + buttonWidth + buttonSpacing, buttonY, buttonWidth, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render background
        graphics.fill(0, 0, this.width, this.height, 0xC0101010);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);

        // Draw block grid
        renderBlockGrid(graphics, mouseX, mouseY);

        // Calculate positions to match init()
        int centerX = this.width / 2;
        int totalWidth = 180 + 10 + 10 + 80 + 10 + 40 + 10 + 16;
        int startX = centerX - (totalWidth / 2);
        int hexX = startX + 180 + 10 + 10;
        int gridEndY = 55 + (4 * (BLOCK_SIZE + BLOCK_PADDING));

        // Draw "#" label before hex input box (positioned correctly with centering)
        graphics.drawString(this.font, "#", hexX - 10, 34, 0xFFFFFF);

        // Draw live color preview box next to hex input (to the right)
        int colorPreviewX = hexX + 90;
        int colorPreviewY = 30;
        int color = parseHexColor(hexBox.getValue());

        // Draw border for color preview
        graphics.fill(colorPreviewX - 1, colorPreviewY - 1, colorPreviewX + 41, colorPreviewY + 21, 0xFFFFFFFF);
        // Draw color preview
        graphics.fill(colorPreviewX, colorPreviewY, colorPreviewX + 40, colorPreviewY + 20, 0xFF000000 | color);

        // Draw block preview with color tint next to color preview
        int blockPreviewX = colorPreviewX + 50;
        int blockPreviewY = 28;

        // Save the current pose
        var pose = graphics.pose();
        pose.pushPose();

        // Apply color tint using RenderSystem
        RenderSystem.setShaderColor(
            ((color >> 16) & 0xFF) / 255.0f,
            ((color >> 8) & 0xFF) / 255.0f,
            (color & 0xFF) / 255.0f,
            1.0f
        );

        // Render the selected block item with tint
        graphics.renderItem(selectedBlock.asItem().getDefaultInstance(), blockPreviewX, blockPreviewY);

        // Reset color
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        pose.popPose();

        // Draw selected block name below grid, centered
        String blockName = BuiltInRegistries.BLOCK.getKey(selectedBlock).getPath().replace("_", " ");
        graphics.drawCenteredString(this.font, "Selected: " + blockName, this.width / 2, gridEndY + 10, 0xFFFFFFFF);

        // Draw history panel on the right side
        renderHistoryPanel(graphics);

        // Render all widgets (buttons and text boxes) - THIS IS CRITICAL
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderHistoryPanel(GuiGraphics graphics) {
        if (createdBlocksHistory.isEmpty()) return;

        int panelX = this.width - 110;
        int panelY = 60;
        int panelWidth = 100;
        int itemHeight = 30;
        int maxItems = Math.min(createdBlocksHistory.size(), 8);

        // Draw panel background
        graphics.fill(panelX - 5, panelY - 25, panelX + panelWidth + 5, panelY + (maxItems * itemHeight) + 5, 0xD0000000);

        // Draw title
        graphics.drawCenteredString(this.font, "§eRecent", panelX + panelWidth / 2, panelY - 15, 0xFFFFFF);

        // Draw each created block in history
        for (int i = 0; i < maxItems; i++) {
            CreatedBlockInfo info = createdBlocksHistory.get(i);
            int itemY = panelY + (i * itemHeight);

            // Draw block icon with color tint
            var pose = graphics.pose();
            pose.pushPose();

            RenderSystem.setShaderColor(
                ((info.color >> 16) & 0xFF) / 255.0f,
                ((info.color >> 8) & 0xFF) / 255.0f,
                (info.color & 0xFF) / 255.0f,
                1.0f
            );

            graphics.renderItem(info.originalBlock.asItem().getDefaultInstance(), panelX, itemY);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            pose.popPose();

            // Draw hex color text
            graphics.drawString(this.font, "#" + info.hexColor, panelX + 20, itemY + 4, 0xFFFFFF);
        }
    }

    private void renderBlockGrid(GuiGraphics graphics, int mouseX, int mouseY) {
        int startX = this.width / 2 - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2;
        int startY = 55;
        int maxRows = 4;
        int gridHeight = maxRows * (BLOCK_SIZE + BLOCK_PADDING);

        // Filter blocks based on search
        String search = searchBox.getValue().toLowerCase();
        if (!search.isEmpty()) {
            filteredBlocks = new ArrayList<>();
            for (Block block : allBlocks) {
                String blockName = BuiltInRegistries.BLOCK.getKey(block).toString();
                if (blockName.contains(search)) {
                    filteredBlocks.add(block);
                }
            }
        } else {
            filteredBlocks = new ArrayList<>(allBlocks);
        }

        // Render visible blocks
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < BLOCKS_PER_ROW; col++) {
                int index = (row + scrollOffset) * BLOCKS_PER_ROW + col;
                if (index >= filteredBlocks.size()) break;

                Block block = filteredBlocks.get(index);
                int x = startX + col * (BLOCK_SIZE + BLOCK_PADDING);
                int y = startY + row * (BLOCK_SIZE + BLOCK_PADDING);

                // Draw block background
                int bgColor = block == selectedBlock ? 0xFF44FF44 : 0xFF888888;
                graphics.fill(x, y, x + BLOCK_SIZE, y + BLOCK_SIZE, bgColor);

                // Draw block item
                graphics.renderItem(block.asItem().getDefaultInstance(), x + 8, y + 8);

                // Highlight on hover
                if (mouseX >= x && mouseX < x + BLOCK_SIZE && mouseY >= y && mouseY < y + BLOCK_SIZE) {
                    graphics.fill(x, y, x + BLOCK_SIZE, y + BLOCK_SIZE, 0x80FFFFFF);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle block selection clicks - MUST match renderBlockGrid positions
        int startX = this.width / 2 - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2;
        int startY = 55;  // Changed from 60 to match renderBlockGrid
        int maxRows = 4;  // Changed from 6 to match renderBlockGrid

        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < BLOCKS_PER_ROW; col++) {
                int index = (row + scrollOffset) * BLOCKS_PER_ROW + col;
                if (index >= filteredBlocks.size()) break;

                int x = startX + col * (BLOCK_SIZE + BLOCK_PADDING);
                int y = startY + row * (BLOCK_SIZE + BLOCK_PADDING);

                if (mouseX >= x && mouseX < x + BLOCK_SIZE && mouseY >= y && mouseY < y + BLOCK_SIZE) {
                    selectedBlock = filteredBlocks.get(index);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int maxScroll = Math.max(0, (filteredBlocks.size() / BLOCKS_PER_ROW) - 4);  // Changed from 5 to 4 to match maxRows
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) delta));
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        // Validate and clean hex input (only allow hex characters)
        String value = hexBox.getValue().toUpperCase();
        value = value.replaceAll("[^0-9A-F]", "");
        if (value.length() > 6) {
            value = value.substring(0, 6);
        }
        hexColor = value;
        if (!hexBox.getValue().equals(value)) {
            hexBox.setValue(value);
        }
    }

    private int parseHexColor(String hex) {
        try {
            // Remove any # prefix if present
            hex = hex.replace("#", "").trim();

            // Pad with zeros if too short
            while (hex.length() < 6) {
                hex = "0" + hex;
            }

            // Parse hex string to integer
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return 0xFFFFFF; // Default to white on error
        }
    }

    private void createColoredBlock() {
        if (this.minecraft != null && this.minecraft.player != null) {
            // Validate hex color is exactly 6 characters
            if (hexColor.length() != 6) {
                // Show error message to player
                if (this.minecraft.player != null) {
                    this.minecraft.player.displayClientMessage(
                        Component.literal("§cError: Hex color must be exactly 6 characters (e.g., FF0000 for red)"),
                        false
                    );
                }
                return;
            }

            // Determine which block type to use based on the selected block
            Block blockToUse = getBlockTypeForTexture(selectedBlock);

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockToUse);
            ResourceLocation mimicBlockId = BuiltInRegistries.BLOCK.getKey(selectedBlock);

            System.out.println("BlockEditorScreen: Sending CreateBlockPacket to server");
            System.out.println("  Block type: " + blockId);
            System.out.println("  Mimic: " + mimicBlockId);
            System.out.println("  Color: " + hexColor.toUpperCase());

            // Send packet to server to create the item
            ModNetworking.sendToServer(new CreateBlockPacket(
                hexColor.toUpperCase(),
                mimicBlockId.toString(),
                blockId.toString()
            ));

            System.out.println("BlockEditorScreen: Packet sent!");

            // Add to history (client-side only for display)
            int color = parseHexColor(hexColor);
            createdBlocksHistory.add(0, new CreatedBlockInfo(selectedBlock, hexColor.toUpperCase(), color));
            if (createdBlocksHistory.size() > 10) {
                createdBlocksHistory.remove(10);
            }

            // Show success message
            String blockName = mimicBlockId.getPath().replace("_", " ");
            if (this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§aCreated colored block: §f" + blockName + " §7(#" + hexColor.toUpperCase() + ")"),
                    false
                );
            }
        }
        this.onClose();
    }

    // Helper method to check if a block is allowed (only specific blocks)
    private boolean isFullSolidBlock(Block block) {
        String blockId = BuiltInRegistries.BLOCK.getKey(block).getPath().toLowerCase();

        // Only allow specific block types: sand, wool, dirt, stone, concrete, deepslate
        String[] allowedKeywords = {
            "sand", "wool", "dirt", "stone", "concrete", "deepslate"
        };

        for (String keyword : allowedKeywords) {
            if (blockId.contains(keyword)) {
                // Additional check: exclude non-full blocks even if they contain the keyword
                String[] excludedKeywords = {
                    "stair", "slab", "fence", "wall", "button", "pressure"
                };

                for (String excluded : excludedKeywords) {
                    if (blockId.contains(excluded)) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    // Helper method to determine which block type to use based on the selected block
    private Block getBlockTypeForTexture(Block selectedBlock) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(selectedBlock);
        String blockName = blockId.getPath().toLowerCase();

        System.out.println("getBlockTypeForTexture: blockId=" + blockId + ", blockName=" + blockName);

        // Check for texture category keywords
        if (blockName.contains("dirt") || blockName.contains("coarse")) {
            System.out.println("Matched DIRT texture");
            return ModBlocks.DYNAMIC_BLOCK_DIRT.get();
        } else if (blockName.contains("sand")) {
            System.out.println("Matched SAND texture");
            return ModBlocks.DYNAMIC_BLOCK_SAND.get();
        } else if (blockName.contains("wool")) {
            System.out.println("Matched WOOL texture");
            return ModBlocks.DYNAMIC_BLOCK_WOOL.get();
        } else if (blockName.contains("concrete") && !blockName.contains("powder")) {
            System.out.println("Matched CONCRETE texture");
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE.get();
        } else if (blockName.contains("deepslate") && !blockName.contains("ore")) {
            System.out.println("Matched DEEPSLATE texture");
            return ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.get();
        } else {
            // Default to stone texture for stone and any other blocks
            System.out.println("Defaulting to STONE texture");
            return ModBlocks.DYNAMIC_BLOCK.get();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}