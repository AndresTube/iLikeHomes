# iLikeHomes

A generic, simple, and normal plugin to manage homes in minecraft

![Version](https://img.shields.io/badge/Version-1.1.0-blue?style=for-the-badge)
![Author](https://img.shields.io/badge/Author-Fendrixx-red?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Spigot%20/%20Paper-gold?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

## Features

- **Multi-Home System**: Set, name, and manage multiple homes easily.
- **Home Icon Chooser**: Personalize your homes with a custom GUI selector after setting them.
- **Interactive GUI**: View and teleport to your homes through a beautiful, paginated inventory.
- **Teleport Warmup**: Prevent combat escapes with a configurable teleportation delay.
- **Dual Storage Support**: Choose between **MySQL** or **SQLite** databases to storage data.
- **Full Localization**: Every message and UI element is 100% customizable in `messages.yml`.

---

## Installation

1. Download the `iLikeHomes-1.1.0.jar` from the [Releases](https://github.com/fendrixx/iLikeHomes/releases) page.
2. Drop it into your server's `plugins/` folder.
3. Restart your server to generate the configuration files.
4. Configure your preferred storage type in `config.yml`.
5. Enjoy!

---

## Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/sethome <name>` | Create a new home at your location | `ilikehomes.user` |
| `/home <name>` | Teleport to one of your homes | `ilikehomes.user` |
| `/homes` | Open the homes selection GUI | `ilikehomes.user` |
| `/delhome <name>` | Remove a home | `ilikehomes.user` |
| `/ilh reload` | Reload the plugin configuration | `ilikehomes.reload` |
| `/ilh help` | View the help menu | `ilikehomes.user` |

---

## Configuration

The plugin generates three main configuration files:

- **`config.yml`**: Manage storage settings (MySQL/SQLite), warmup times, and available icons.
- **`messages.yml`**: Customize all plugin messages and GUI titles.
- **`homes.yml`**: (Internal) Legacy storage file when using the YAML backend.

---

## Author

**Fendrixx**
*This is my third plugin project! I've put a lot of effort into making it polished and reliable compared to my previous work. I hope you find it useful for your community!*

**Support**
- **Discord**: `andrxs.f`

---

## License

Distributed under the MIT License. See `LICENSE` for more information.
