# BlockEditor - Custom Block Creator (Fabric)

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://minecraft.net/)
[![Version](https://img.shields.io/badge/Version-1.0.4-informational.svg)](#)
[![Fabric](https://img.shields.io/badge/Fabric_Loader-0.19.2+-orange.svg)](https://fabricmc.net/)
[![Fabric API](https://img.shields.io/badge/Fabric_API-0.92.2+-yellow.svg)](https://modrinth.com/mod/fabric-api)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Create custom-colored blocks with any hex color, 19 base textures, and custom names. Full WorldEdit integration, Xaero's Minimap/World Map support, and persistent block history with folder organization.

![Block Editor Interface](./images/1.0.4/Screenshot%202025-10-26%20183801.png)

---

## Features

### Block Creation
- **16.7 million colors** — full hex RGB input (`#000000` to `#FFFFFF`)
- **19 base textures** — Wool, Stone, Dirt, Sand, Concrete, Deepslate, Wood, Smooth Stone, Cobblestone, Terracotta, Concrete Powder, Glass, Tinted Glass, Stained Glass, Diorite, Calcite, Mushroom Stem, Dead Tube Coral, Pearlescent Froglight
- **20 slots per texture** — 380 total custom block slots with persistent world storage
- **Custom naming** — give each block a unique name for easy WorldEdit use
- **Smart validation** — duplicate name detection with automatic numbering (`wall`, `wall2`, `wall3`...)
- **Created blocks go straight to your hand** — existing held item is moved to inventory or dropped

### Block Editor GUI (press G)
- **Hex color field** — live validation, click to select all for quick paste
- **Block picker grid** — scrollable grid of all 19 base textures
- **Live preview** — tinted block preview updates as you type
- **Recent Blocks panel** — scrollable history of created blocks (up to 500 entries)
- **Folder organization** — create color-tinted folders, drag-and-drop blocks between them
- **Folder dialog** — name + 48-color palette picker
- **Compact/enlarged view** — toggle between display modes
- **Full Stack toggle** — survival-only toggle for x1 vs x64 stack creation
- **Clear Registry** — two-click confirmation, clears all custom blocks, folders, and history
- **Persistent history** — saved to `config/blockeditor_history.dat` across sessions

### Pick Block (Middle-Click)
- **In-world** — middle-click any custom block to pick it up with all properties intact
- **In GUI** — middle-click a history entry to find and equip that block from inventory
- **Smart search** — checks inventory first, requests from server if not found
- **Property matching** — preserves color, texture, and custom name

### WorldEdit Integration
Custom blocks work seamlessly with WorldEdit commands:

```
# Bare custom names — no prefix needed
/bset wall
/breplace stone floor

# With be: prefix also works
/bset be:wall
/breplace stone be:floor

# Standard WorldEdit commands with be: prefix
//set be:wall
//replace stone be:roof
```

The mod intercepts `//` commands containing `be:` and translates custom names to internal registry names automatically.

### Xaero's Minimap & World Map Integration
Custom blocks display their actual colors on Xaero's Minimap and World Map. The mod overrides `getMapColor()` via Mixin and uses a thread-safe color cache to provide the nearest of Minecraft's 62 MapColor presets to each block's RGB color. Uses perceptually-weighted color matching (green-sensitive) for accurate results.

**Requires Xaero's Block Colors setting set to "Vanilla Map Colors" (mode 1).** If colors appear white, check: Minimap Settings > Block Colors > Vanilla Map Colors, and World Map Settings > Block Colors > Vanilla Map Colors.

---

## Commands

### Block Management

| Command | Permission | Description |
|---------|-----------|-------------|
| `/be clear` | OP (level 2) | Clears all custom blocks from the registry and notifies all players |
| `/be refresh` | OP (level 2) | Rescans a 16-block radius and refreshes all custom block entities |

### Block Info

| Command | Permission | Description |
|---------|-----------|-------------|
| `/blockeditor list` | Any player | Lists all custom block name-to-registry mappings |
| `/blockeditor test <name>` | Any player | Looks up a custom block name and shows its registry info |
| `/blockeditor clear` | Any player | Shows info about using the GUI Clear Registry button |
| `/translate` | Any player | Lists all custom name-to-internal ID mappings |
| `/translate <name>` | Any player | Translates a single custom name to its `be:u_*` registry name |

### WorldEdit Proxy Commands

| Command | Description |
|---------|-------------|
| `/bset <pattern>` | Translates custom names and executes `//set` (e.g., `/bset wall` or `/bset be:wall`) |
| `/breplace <pattern>` | Translates custom names and executes `//replace` (e.g., `/breplace stone floor`) |

Both proxy commands support bare custom names (no `be:` prefix required) and will automatically resolve them to internal block IDs.

### Debug

| Command | Permission | Description |
|---------|-----------|-------------|
| `/bedebug` | OP (level 2) | Dumps all UserBlockRegistry entries and WorldEdit integration mappings |

---

## Keybindings

| Key | Context | Action |
|-----|---------|--------|
| **G** | In-game | Open/close Block Editor GUI |
| **Middle Mouse** | Looking at block | Pick custom block (copies color, texture, and name) |
| **Middle Mouse** | In GUI history | Find and equip that block from inventory |
| **Middle Mouse** | In GUI (survival) | Toggle Full Stack mode (x1 / x64) |
| **1 / 2** | In GUI | Scroll history up / down |
| **W / S** | In GUI (unfocused) | Quick history scroll up / down |
| **Space** | In GUI | Scroll history down |
| **Arrow Up/Down** | In GUI | Scroll history |
| **PgUp / PgDn** | In GUI | Fast scroll history |

---

## Block Types

### Base Textures (19)
Each texture has a corresponding dynamic block for the GUI and 20 user block slots for custom-named blocks:

| Texture | Dynamic Block | User Block Slots |
|---------|--------------|-----------------|
| Wool | `dynamic_block_wool` | `u_wool1` - `u_wool20` |
| Stone | `dynamic_block_stone` | `u_stone1` - `u_stone20` |
| Dirt | `dynamic_block_dirt` | `u_dirt1` - `u_dirt20` |
| Sand | `dynamic_block_sand` | `u_sand1` - `u_sand20` |
| Concrete | `dynamic_block_concrete` | `u_concrete1` - `u_concrete20` |
| Deepslate | `dynamic_block_deepslate` | `u_deepslate1` - `u_deepslate20` |
| Wood | `dynamic_block_wood` | `u_wood1` - `u_wood20` |
| Smooth Stone | `dynamic_block_smooth_stone` | `u_smooth_stone1` - `u_smooth_stone20` |
| Cobblestone | `dynamic_block_cobblestone` | `u_cobblestone1` - `u_cobblestone20` |
| Terracotta | `dynamic_block_terracotta` | `u_terracotta1` - `u_terracotta20` |
| Concrete Powder | `dynamic_block_concrete_powder` | `u_concrete_powder1` - `u_concrete_powder20` |
| Glass | `dynamic_block_glass` | `u_glass1` - `u_glass20` |
| Tinted Glass | `dynamic_block_tinted_glass` | `u_tinted_glass1` - `u_tinted_glass20` |
| Stained Glass | `dynamic_block_stained_glass` | `u_stained_glass1` - `u_stained_glass20` |
| Diorite | `dynamic_block_diorite` | `u_diorite1` - `u_diorite20` |
| Calcite | `dynamic_block_calcite` | `u_calcite1` - `u_calcite20` |
| Mushroom Stem | `dynamic_block_mushroom_stem` | `u_mushroom_stem1` - `u_mushroom_stem20` |
| Dead Tube Coral | `dynamic_block_dead_tube_coral` | `u_dead_tube_coral1` - `u_dead_tube_coral20` |
| Pearlescent Froglight | `dynamic_block_pearlescent_froglight` | `u_pearlescent_froglight1` - `u_pearlescent_froglight20` |

**Total registered blocks: 381** (20 dynamic + 19x20 user slots + 1 base `dynamic_block`)

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.2+ for Minecraft 1.20.1
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) 0.92.2+ and place in your `mods` folder
3. Place `be-1.0.4.jar` in your `mods` folder
4. (Optional) Install [WorldEdit for Fabric](https://modrinth.com/mod/worldedit) for building commands
5. (Optional) Install [Xaero's Minimap](https://modrinth.com/mod/xaeros-minimap) / [World Map](https://modrinth.com/mod/xaeros-world-map) — custom block colors show automatically

## Technical Specs

| Property | Value |
|----------|-------|
| Mod ID | `be` |
| Minecraft | 1.20.1 |
| Mod Loader | Fabric |
| Fabric Loader | 0.19.2+ |
| Fabric API | 0.92.2+ |
| Java | 17 |
| Yarn Mappings | 1.20.1+build.10 |
| Side | Client & Server |
| License | MIT |

### Mixins
| Mixin | Target | Purpose |
|-------|--------|---------|
| `DynamicBlockMapColorMixin` | `AbstractBlock.AbstractBlockState` | Returns nearest MapColor for custom blocks (Xaero's map support) |
| `DoItemPickMixin` | `MinecraftClient` | Intercepts middle-click pick-block for custom blocks |

### Network Packets
| Packet | Direction | Purpose |
|--------|-----------|---------|
| `be:create_block` | Client -> Server | Create a custom block with color, texture, name, and stack size |
| `be:clear_registry` | Client -> Server | Clear all custom blocks from the registry |
| `be:give_picked_block` | Client -> Server | Request a picked block from the server |

---

## Screenshots

![History Panel](./images/1.0.4/Screenshot%202025-10-26%20183544.png)
![Folder Items](./images/1.0.4/Screenshot%202025-10-26%20183609.png)
![Folder Dialog](./images/1.0.4/Screenshot%202025-10-26%20183626.png)
![UI Overview](./images/1.0.4/Screenshot%202025-10-26%20183801.png)

---

## Version History

### v1.0.4 (Current)
- Folder organization with color tinting and drag-and-drop
- Folder dialog with 48-color palette picker
- Xaero's Minimap & World Map color integration
- Survival full-stack toggle and compact/enlarged view toggle
- Clear Registry removes folders and history
- Drag threshold prevents accidental drags
- Fixed hex box input, paste behavior, and G key toggle
- WorldEdit proxy commands support bare custom names (no `be:` prefix needed)
- Created blocks always placed directly in hand

### v1.0.3
- World-based middle-click pick block system
- Smart inventory search with property matching
- Server integration for block reconstruction

### v1.0.2
- Custom block naming system
- WorldEdit integration with `/bset` and `/breplace`
- Middle-click block history shortcuts
- Auto-numbering for duplicate names
- 20 slots per block type

### v1.0.1
- Initial WorldEdit proxy commands
- Basic custom naming support

### v1.0.0
- Initial release — hex color customization, 9 texture types, WorldEdit basics

---

## Contributing

Contributions welcome — report bugs, suggest features, or submit pull requests.

## License

MIT License — see [LICENSE](LICENSE) for details.
