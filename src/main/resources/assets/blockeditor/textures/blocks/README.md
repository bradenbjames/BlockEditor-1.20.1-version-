# Base Texture Placeholders

This directory should contain the base textures for custom block creation:

## Required Textures:
- `stone_base.png` - 16x16 gray stone texture with typical Minecraft stone pattern
- `dirt_base.png` - 16x16 brown dirt texture with typical Minecraft dirt pattern  
- `sand_base.png` - 16x16 yellow sand texture with typical Minecraft sand pattern
- `concrete_base.png` - 16x16 white concrete texture (solid color for easy color tinting)

## Texture Requirements:
- All textures must be 16x16 pixels PNG format
- Use standard Minecraft texture style and resolution
- Textures will be dynamically tinted by the color picker in the GUI
- Keep textures relatively neutral for best color tinting results

## Implementation:
The ResourceGenerator class will create colored variants of these base textures
by applying RGB color values from the GUI color picker.