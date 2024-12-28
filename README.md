# XMobGeneration

A powerful Minecraft plugin for managing custom mob spawn areas with an intuitive GUI interface.

## Features

- Create up to 45 custom mob spawn areas
- GUI-based management system
- WorldEdit integration for easy area selection
- Customizable mob types, spawn counts, and respawn delays
- Per-area spawn control
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
```

Messages can also be customized in the configuration file.

## Usage

1. Use WorldEdit to select an area (using the wooden axe)
2. Create a spawn area using `/xmg create <name>`
3. Configure the area using the GUI or commands
4. Enable/disable spawning using the GUI

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
1. Check the [Issues](https://github.com/YourUsername/XMobGeneration/issues) page
2. Create a new issue if your problem isn't already listed

## Credits

- Built with Spigot API
- Uses WorldEdit for area selection