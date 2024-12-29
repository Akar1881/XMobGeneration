# XMobGeneration

A powerful Minecraft plugin for managing custom mob spawn areas with an intuitive GUI interface.

## Features

- Create up to 45 custom mob spawn areas
- GUI-based management system
- WorldEdit integration for easy area selection
- Customizable mob types, spawn counts, and respawn delays
- Per-area spawn control
- Custom drops system for each spawn area
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
5. Configure custom drops for each area through the GUI

## Custom Drops System

Each spawn area can have its own custom drops configuration:

1. Open the area edit menu and click the chest icon (slot 31)
2. Drag and drop items into the first 45 slots of the inventory
3. Use the dye button to toggle custom drops on/off
4. Click the emerald button to manually save changes
5. Changes are also saved automatically when closing the inventory

## Area Restart System

Areas automatically restart at configurable intervals:

- Set restart interval in config.yml (minimum 1 minute)
- Warning message appears before restart
- All mobs are respawned during restart
- Area configurations are preserved
- Helps prevent potential issues with long-running spawn areas

## Building from Source

1. Clone the repository
2. Build using Maven:
```bash
mvn clean package
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).

## Support

If you encounter any issues or have questions:
1. Check the [Issues](https://github.com/Akar1881/XMobGeneration/issues) page
2. Create a new issue if your problem isn't already listed

## Changelog

See [changelog.txt](changelog.txt) for detailed version history.

### Latest Changes (v1.1.0)
- Added custom drops system with GUI interface
- Implemented area restart system
- Improved inventory handling
- Added automatic and manual saving for custom drops
- Fixed item dragging functionality

## Credits

- Built with Spigot API
- Uses WorldEdit for area selection