# XMobGeneration

A powerful Minecraft plugin for managing custom mob spawn areas with an intuitive GUI interface.

## Features

- Create up to 45 custom mob spawn areas
- GUI-based management system
- WorldEdit integration for easy area selection
- Support for both vanilla mobs and MythicMobs (optional)
- Customizable mob types, spawn counts, and respawn delays
- Per-area spawn control
- Custom drops system for each spawn area
  - Easy item management through GUI
  - Configurable drop chances in areas.yml
  - Default 100% drop chance for new items
  - Persistent drop chances
  - Clean item lore handling
- Advanced mob configuration system
  - Custom mob names with level display
  - Configurable health and damage
  - Level system with visual indicators
- Custom mob equipment system
  - Configure armor and off-hand items
  - Persistent equipment per area
  - Equipment preserved through respawns and server restarts
  - Easy equipment management through GUI
- Automatic area restart system
- Persistent data storage

## Requirements

- Spigot/Paper 1.17+
- WorldEdit plugin
- MythicMobs (Optional) - For spawning custom MythicMobs

## Installation

1. Download the latest release from [Modrinth](https://modrinth.com/project/xmobgeneration/)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will generate default configuration files
5. You can download latest release on github releases.

## Commands

- `/xmg create <name>` - Create a new spawn area
- `/xmg delete <name>` - Delete a spawn area
- `/xmg config <name> <mobType|mythic:mobType> <count> <delay>` - Configure an area
- `/xmg setmobnames <areaname> <mobname>` - Set custom name for mobs in an area
- `/xmg mobconfig <areaname> <health> <damage> <level>` - Configure mob stats
- `/xmg list` - List all spawn areas
- `/xmg gui` - Open the GUI interface
- `/xmg reload` - Reload configuration and restart all mob areas
- `/xmg help` - Show help message

## Permissions

- `xmg.admin` - Access to all XMobGeneration commands (default: op)

## Configuration

The plugin creates a `config.yml` file with customizable settings:

```yaml
settings:
  default-mob-type: ZOMBIE
  default-spawn-count: 5
  default-respawn-delay: 30
  restart-interval: 10  # Time in minutes between area restarts
```

Messages can also be customized in the configuration file.

## Usage

1. Use WorldEdit to select an area (using the wooden axe)
2. Create a spawn area using `/xmg create <name>`
3. Configure the area using the GUI or commands
4. Enable/disable spawning using the GUI
5. Configure custom drops and mob stats through the GUI

### MythicMobs Integration

To use MythicMobs in your spawn areas:

1. Install MythicMobs plugin (optional)
2. Use the format `mythic:mobtype` when configuring mob types
   Example: `/xmg config myarea mythic:CustomBoss 5 30`

Note: Version 1.6.0 and above will work perfectly fine without MythicMobs installed. The plugin will only enable MythicMobs features when the dependency is present.

## Support

If you encounter any issues or have questions:
1. Check the [Issues](https://github.com/Akar1881/XMobGeneration/issues) page
2. Create a new issue if your problem isn't already listed

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).