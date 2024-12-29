# XMobGeneration

A powerful Minecraft plugin for managing custom mob spawn areas with an intuitive GUI interface.

## Features

- Create up to 45 custom mob spawn areas
- GUI-based management system
- WorldEdit integration for easy area selection
- Customizable mob types, spawn counts, and respawn delays
- Per-area spawn control
- Custom drops system for each spawn area
- Advanced mob configuration system
  - Custom mob names with level display
  - Configurable health and damage
  - Level system with visual indicators
- Automatic area restart system
- Persistent data storage

## Requirements

- Spigot/Paper 1.17+
- WorldEdit plugin

## Installation

1. Download the latest release from [Modrinth](https://modrinth.com/project/xmobgeneration/)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will generate default configuration files

## Commands

- `/xmg create <name>` - Create a new spawn area
- `/xmg delete <name>` - Delete a spawn area
- `/xmg config <name> <mobType> <count> <delay>` - Configure an area
- `/xmg setmobnames <areaname> <mobname>` - Set custom name for mobs in an area
- `/xmg mobconfig <areaname> <health> <damage> <level>` - Configure mob stats
- `/xmg list` - List all spawn areas
- `/xmg gui` - Open the GUI interface
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

## Mob Configuration System

Each spawn area can have customized mob settings:

1. Custom Names
   - Use `/xmg setmobnames <area> <name>` or the GUI
   - Names appear as holograms above mobs
   - Format: [Lv.X] Name [Health❤]

2. Mob Stats
   - Configure via `/xmg mobconfig` or GUI
   - Adjustable health, damage, and level
   - Changes apply to new and existing mobs
   - Access through crafting table icon in area edit menu

## Support

If you encounter any issues or have questions:
1. Check the [Issues](https://github.com/Akar1881/XMobGeneration/issues) page
2. Create a new issue if your problem isn't already listed

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).