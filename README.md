# BlockEditor - Custom Block Creator

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://minecraft.net/)
[![Version](https://img.shields.io/badge/Version-1.0.2-informational.svg)](#)
[![Forge](https://img.shields.io/badge/Forge-47.3.0+-orange.svg)](https://files.minecraftforge.net/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Create and customize blocks with any color, texture, and **custom names** in Minecraft! Now with enhanced WorldEdit integration and smart UI features.

![Block Editor Interface](./images/1.0.2/Screenshot%202025-10-21%20192157.png)
*Enhanced Block Editor interface with custom naming, recent blocks history, and middle-click shortcuts*

## ✨ New Features (v1.0.2)

### 🏷️ **Custom Block Naming**
- **Name Your Blocks**: Give each block a unique custom name (e.g., "wall", "roof", "floor")
- **WorldEdit Ready**: Use custom names directly in WorldEdit commands: `//set be:wall`, `//replace stone be:roof`
- **Smart Validation**: Automatic duplicate name detection with intelligent numbering (wall, wall2, wall3...)
- **Persistent Names**: Custom names are saved and work across world restarts

### 🖱️ **Middle-Click Block History**
- **Instant Access**: Middle-click any block in the Recent Blocks panel to instantly equip it
- **Smart Finding**: Searches your inventory first, creates new block if not found
- **Auto-Numbering**: Automatically creates uniquely named versions (up to 50 variations)
- **One-Click Workflow**: Middle-click → Block equipped → Screen closes → Ready to build

### 🎨 **Enhanced UI & Experience**
- **Streamlined Interface**: Clean, modern design with better spacing and visual clarity
- **Recent Blocks Panel**: Visual history of your created blocks with names and colors
- **Smart Block Grid**: Optimized layout for faster block type selection
- **Color Preview**: Real-time hex color validation and preview

### 🌍 **Advanced WorldEdit Integration**
- **Custom Name Commands**: `/bset be:your_custom_name` works instantly
- **Replace Operations**: `/breplace stone be:wall` using your named blocks
- **Command Autocomplete**: WorldEdit recognizes all your custom block names
- **Seamless Workflow**: Create block → Name it → Use in WorldEdit immediately

## 🌈 Unlimited Color & Texture Combinations

**16.7 Million Colors × 9 Textures × Custom Names = Infinite Building Possibilities**

### 🎨 **Full RGB Spectrum with Smart Storage**
- **Any Color**: Full hex color support from `#000000` to `#FFFFFF` (16,777,216 colors)
- **9 Base Textures**: Wool, Stone, Dirt, Sand, Concrete, Deepslate, Wood, Smooth Stone, Cobblestone
- **20 Slots Per Type**: 180 total custom block slots with persistent storage
- **Custom Names**: Each block can have a unique, memorable name

### 🔥 **Why BlockEditor is Different**

**Real Minecraft Blocks vs Visual Overlays:**

✅ **Full Integration**
- Work with all vanilla commands: `/give`, `/setblock`, `/fill`
- **WorldEdit Compatible**: `//set be:wall`, `//replace stone be:floor`
- Creative inventory integration with proper names
- Persistent across saves and multiplayer servers

❌ **What Other Mods Can't Do**
- Other color mods: Visual-only, no WorldEdit support
- Limited palettes: Fixed colors, no custom names
- Command incompatible: Don't work with `/give` or WorldEdit

## 🎮 Usage Guide

### 🏗️ **Basic Block Creation**
1. **Open Editor**: Press **G** (default) to open Block Editor
2. **Choose Texture**: Click any block type from the grid
3. **Set Color**: Enter hex color (e.g., `FF0000`) in the color field
4. **Name Your Block**: Enter a custom name (e.g., "wall", "roof", "accent")
5. **Create**: Click "Create Block" - it appears in your inventory with the custom name
6. **Build**: Place the block anywhere - it keeps your color and name!

### �️ **Middle-Click Shortcuts**
1. **Find Block**: See a block you want in the Recent Blocks panel?
2. **Middle-Click**: Middle-click on it for instant access
3. **Auto-Equip**: Block appears in your hand (slot 0) and screen closes
4. **Smart Creation**: If you don't have it, automatically creates "blockname2", "blockname3", etc.

### 🌍 **WorldEdit Integration**
Use your custom-named blocks in any WorldEdit command:

```bash
# Basic usage with custom names
/bset be:wall
/breplace stone be:floor  
/bset be:accent

# Complex building operations
//copy
//paste
/bset 50% be:wall,30% be:floor,20% be:accent

# Selection and replacement
//sel cuboid
//pos1
//pos2
/breplace minecraft:stone be:custom_stone
```

**Pro Tip**: Create blocks with descriptive names like "red_brick", "blue_roof", "green_wall" for easy WorldEdit use!

### 📋 **Commands**
- `/be clear` - Clear all custom blocks from registry
- **WorldEdit Commands**: Use `/bset be:your_block_name` and `/breplace from_block be:your_block_name`

## 🎨 **Color & Naming System**

### 🏷️ **Custom Names**
- **Flexible Naming**: Use any name (letters, numbers, underscores)
- **Automatic Cleanup**: Invalid characters converted to underscores
- **Duplicate Handling**: Automatic numbering prevents name conflicts
- **WorldEdit Ready**: Names work instantly in WorldEdit commands

### 🌈 **Color System**
- **Hex Input**: 6-digit hex codes without # (e.g., `FF0000`, `00FF00`, `0066CC`)
- **Real-Time Preview**: See your color applied immediately
- **Full RGB Range**: Any color imaginable from pure black to pure white
- **Persistent Colors**: Colors saved with your world and across servers

### 🗂️ **Block Organization**
- **Recent History**: Visual panel showing your last created blocks
- **Type Categories**: Organized by texture type (wool, stone, concrete, etc.)
- **20 Slots Each**: 20 custom blocks per texture type (180 total)
- **Smart Slot Management**: Automatic slot assignment with visual feedback

## 🔧 **Technical Specifications**

- **Minecraft Version**: 1.20.1
- **Mod Loader**: Forge 47.3.0+
- **Side**: Client & Server (works in multiplayer)
- **Dependencies**: None required (WorldEdit optional but recommended)
- **Performance**: Optimized for large builds and multiplayer servers

## 🚀 **Installation**

1. **Download**: Get latest release from [Releases](../../releases)
2. **Forge**: Install Minecraft Forge 47.3.0+ for MC 1.20.1
3. **Install**: Place `.jar` in your `mods` folder
4. **Optional**: Install WorldEdit for enhanced building features
5. **Launch**: Start Minecraft with Forge profile

## 💡 **Pro Building Tips**

### 🏗️ **Efficient Workflow**
1. **Plan Colors**: Choose your palette before building
2. **Name Systematically**: Use names like "wall_light", "wall_dark", "trim"
3. **Use Middle-Click**: Quick access to frequently used blocks
4. **WorldEdit Integration**: Build structures with `/bset be:wall` commands

### 🎨 **Color Coordination**
```
Modern Building Palette:
- be:concrete_light (#E8E8E8) - Main walls
- be:concrete_dark (#4A4A4A) - Accents  
- be:glass_blue (#0077BE) - Windows
- be:trim_gold (#FFD700) - Details

Medieval Castle Theme:
- be:stone_wall (#8B7D6B) - Main structure
- be:stone_dark (#5D5D5D) - Foundation
- be:wood_beam (#8B4513) - Support beams
- be:roof_slate (#2F4F4F) - Roofing
```

## 📋 **Version History**

### v1.0.2 (Current)
- ✨ **Custom block naming system**
- ✨ **Enhanced WorldEdit integration** with custom names (`/bset`, `/breplace`)
- ✨ **Middle-click block history shortcuts**
- ✨ **Smart duplicate name handling** (auto-numbering with timestamps)
- ✨ **Improved UI design** and user experience
- ✨ **Better inventory management** (blocks always appear in hand)
- 🐛 **20 slots per block type** (increased from limited slots)
- 🐛 **Fixed block persistence** issues

### v1.0.1
- 🔧 **Initial WorldEdit proxy commands**
- 🔧 **Basic custom naming support**
- 🐛 **Bug fixes** for block creation

### v1.0.0
- 🎉 Initial release with basic color customization
- 🎨 9 block texture types with hex color support
- 🔧 Basic WorldEdit integration
- 💾 World persistence and multiplayer support

## 🤝 **Contributing**

Contributions welcome! Feel free to:
- 🐛 Report bugs or suggest features
- 🔧 Submit pull requests
- � Improve documentation
- 🎨 Share your amazing builds using BlockEditor!

## 📄 **License**

MIT License - see [LICENSE](LICENSE) for details.

## 🙏 **Acknowledgments**

- Built for Minecraft 1.20.1 with Forge
- WorldEdit integration for enhanced building
- Community feedback for feature improvements

---

**Create, Name, Build - Make every block uniquely yours! 🎨**