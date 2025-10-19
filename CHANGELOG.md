# Changelog

All notable changes to the BlockEditor mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-10-19

### Added
- Initial release of BlockEditor mod
- Custom block creation GUI with color picker
- Support for multiple block types (wool, stone, dirt, sand, concrete, deepslate, wood)
- Hex color input system (6-digit codes)
- Numbered user block system (`user_wool1`, `user_stone1`, etc.)
- WorldEdit integration for custom blocks
- Pick block functionality (middle-click support)
- Persistent storage system for custom blocks
- Commands system:
  - `/be clear` - Clear all custom user blocks
  - `/be refresh` - Refresh block colors in area
- Performance optimizations:
  - Smart color application (only when needed)
  - Efficient client-server synchronization
  - Minimal logging for clean console output
- Multiplayer compatibility
- Client-side color registration system
- Automatic color application on block placement
- Manual refresh system for troubleshooting

### Technical Details
- Built for Minecraft 1.20.1
- Requires Forge 47.4.0+
- Compatible with WorldEdit (optional)
- Works on both client and server
- No external dependencies required

### Known Issues
- None at release

---

## Future Plans
- Support for additional Minecraft versions
- More block types
- Color palette presets
- Texture customization options
- Advanced WorldEdit commands