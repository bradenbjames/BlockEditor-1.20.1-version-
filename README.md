# BlockEditor - Custom Block Creator

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://minecraft.net/)
[![Version](https://img.shields.io/badge/Version-1.0.4-informational.svg)](#)
[![Fabric](https://img.shields.io/badge/Fabric_Loader-0.19.2+-orange.svg)](https://fabricmc.net/)
[![Fabric API](https://img.shields.io/badge/Fabric_API-0.92.2+-yellow.svg)](https://modrinth.com/mod/fabric-api)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

[View the CurseForge page description](./CURSEFORGE.md)

Create and customize blocks with any color, texture, and **custom names** in Minecraft! Now with enhanced WorldEdit integration, smart UI features, and **world-based pick block functionality**.

![Block Editor Interface](./images/1.0.4/Screenshot%202025-10-26%20183801.png)
*Latest Block Editor interface (v1.0.4) featuring folder organization, color-tinted groups, and improved dialog layout*

## ✨ New Features (v1.0.4)

### 🗂️ Foldered History with Color Tinting
- Create folders from the Recent Blocks panel (top-right “+”).
- Expanded folders tint child items to match the folder color for quick visual grouping.
- Expand/collapse with pixel-perfect arrows; lighter hover makes items stand out.
- Drag-and-drop items into folders or back to the main list.

### 🎨 Folder Dialog: Name + 48-Color Palette
- New modal dialog to name a folder and choose a color by hex or preset.
- 48 curated, distinct preset colors arranged in a wrapping grid.
- Clean, consistent spacing between labels, fields, and the palette.

### 🔧 UX & Stability
- Clear Registry also clears folders and history storage.
- Survival-only “Full Stack” toggle (right) and compact/large toggle (left).
- Drag threshold so single clicks don’t start drags; clicks work reliably.
- Folder colors persist to disk and restore across sessions.

### 📸 Screenshots (v1.0.4)
![v1.0.4 - History Panel](./images/1.0.4/Screenshot%202025-10-26%20183544.png)
![v1.0.4 - History Panel Alt](./images/1.0.4/Screenshot%202025-10-26%20183603.png)
![v1.0.4 - Folder Items Tinted](./images/1.0.4/Screenshot%202025-10-26%20183609.png)
![v1.0.4 - Folder Dialog](./images/1.0.4/Screenshot%202025-10-26%20183626.png)
![v1.0.4 - UI Overview](./images/1.0.4/Screenshot%202025-10-26%20183801.png)

## ✨ New Features (v1.0.3)

### 🖱️ **Enhanced Pick Block System**
- **World-Based Middle-Click**: Middle-click any custom block in the world to instantly pick it up
- **Smart Inventory Search**: Automatically searches your inventory for matching blocks first
- **Server Integration**: If block not found locally, requests the exact block from server
- **Perfect Block Matching**: Maintains all properties including color, texture, and custom names
- **Seamless Workflow**: Middle-click → Block equipped → Ready to build immediately

### 🎯 **Advanced Block Detection**
- **Custom Block Recognition**: Detects `dynamic_block`, `user_`, and `u_` prefixed blocks
- **Property Preservation**: Extracts and maintains Color, MimicBlock, and CustomName data
- **Robust Color Handling**: Supports both integer and string hex color formats
- **Fallback Protection**: Graceful handling of missing or corrupted block data

### 🔄 **Improved User Experience**
- **Status Messages**: Clear feedback when picking blocks or requesting from server
- **Event Cancellation**: Prevents default pick block behavior for custom blocks
- **Cross-Session Compatibility**: Works with existing v1.0.2 history and naming features
- **Performance Optimized**: Efficient NBT reading and inventory scanning

## ✨ Previous Features (v1.0.2)

### 🏷️ **Custom Block Naming**
- **Name Your Blocks**: Give each block a unique custom name (e.g., "wall", "roof", "floor")
- **WorldEdit Ready**: Use custom names directly in WorldEdit commands: `//set be:wall`, `//replace stone be:roof`
- **Smart Validation**: Automatic duplicate name detection with intelligent numbering (wall, wall2, wall3...)
- **Persistent Names**: Custom names are saved and work across world restarts

### 🖱️ **Enhanced Pick Block Integration**
- **World-to-Inventory**: Middle-click custom blocks in the world to add them to your inventory
- **Smart Block Matching**: Advanced algorithm matches Color + OriginalBlock + CustomName
- **Server Synchronization**: Seamless client-server communication for block reconstruction
- **Inventory Priority**: Always checks existing inventory first before creating new blocks

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

**16.7 Million Colors × 17 Textures × Custom Names = Infinite Building Possibilities**

### 🎨 **Full RGB Spectrum with Smart Storage**
- **Any Color**: Full hex color support from `#000000` to `#FFFFFF` (16,777,216 colors)
- **17 Base Textures**: Wool, Stone, Dirt, Sand, Concrete, Deepslate, Wood, Smooth Stone, Cobblestone, Terracotta, Concrete Powder, Glass, Diorite, Calcite, Mushroom Stem, Dead Tube Coral, Pearlescent Froglight
- **20 Slots Per Type**: 340 total custom block slots with persistent storage
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

### � **Enhanced Pick Block Workflow**
1. **In-World Picking**: Middle-click any custom block in the world
2. **Automatic Detection**: System identifies custom blocks and extracts properties
3. **Inventory Search**: Searches your inventory for matching block first
4. **Server Request**: If not found, automatically requests block from server
5. **Instant Equip**: Block appears in your hand ready for immediate use

### �🏗️ **Basic Block Creation**
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
# Basic usage with custom names (bare names work!)
/bset wall
/breplace stone floor  
/bset accent

# Also works with be: prefix
/bset be:wall
/breplace stone be:floor

# Complex building operations
//copy
//paste

# Selection and replacement
//sel cuboid
//pos1
//pos2
/breplace minecraft:stone custom_stone
```

**Pro Tip**: Create blocks with descriptive names like "red_brick", "blue_roof", "green_wall" for easy WorldEdit use!

### 📋 **Commands**
- `/blockeditor list` - List all custom block name mappings
- `/blockeditor test <name>` - Check if a custom block name resolves correctly
- `/blockeditor clear` - Info on clearing the registry
- `/bset <pattern>` - WorldEdit set using custom block names (e.g., `/bset blue`)
- `/breplace <from> <to>` - WorldEdit replace using custom block names (e.g., `/breplace stone blue`)

**Note**: With `/bset` and `/breplace`, you can use bare custom names directly — no `be:` prefix needed!

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
- **Mod Loader**: Fabric (Loader 0.19.2+, Fabric API 0.92.2+)
- **Side**: Client & Server (works in multiplayer)
- **Dependencies**: None required (WorldEdit optional but recommended)
- **Performance**: Optimized for large builds and multiplayer servers

## 🚀 **Installation**

1. **Download**: Get latest release from [Releases](../../releases)
2. **Fabric**: Install [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.2+ for MC 1.20.1
3. **Fabric API**: Download [Fabric API](https://modrinth.com/mod/fabric-api) and place it in your `mods` folder
4. **Install**: Place `be-1.0.4.jar` in your `mods` folder
5. **Optional**: Install WorldEdit for Fabric for enhanced building features
6. **Launch**: Start Minecraft with your Fabric profile

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

### v1.0.4 (Current)
- 🗂️ Folder organization in the Recent Blocks panel (expand/collapse)
- 🎨 Folder color tinting with persistent storage
- 🖱️ Drag-and-drop items into/out of folders
- 🎛️ Survival-only full-stack toggle; compact/large view toggle
- 🎨 Folder dialog with name + 48-color palette and polished layout
- 🧹 Clear Registry removes folders/history entirely
- 🐛 Drag threshold prevents accidental dragging on click

### v1.0.3
- ✨ **World-based middle-click pick block system**
- ✨ **Enhanced block detection** for custom blocks in the world
- ✨ **Smart inventory search** with perfect property matching
- ✨ **Server integration** for seamless block reconstruction
- ✨ **Advanced NBT handling** supporting multiple color formats
- 🔧 **Improved user feedback** with status messages
- 🐛 **Event handling optimization** preventing conflicts with vanilla pick block

### v1.0.2
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

- Built for Minecraft 1.20.1 with Fabric
- WorldEdit integration for enhanced building
- Community feedback for feature improvements

---

**Create, Name, Build - Make every block uniquely yours! 🎨**