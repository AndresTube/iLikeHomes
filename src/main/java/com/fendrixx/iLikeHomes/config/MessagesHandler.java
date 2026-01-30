package com.fendrixx.iLikeHomes.config;

import org.bukkit.configuration.file.FileConfiguration;

public class MessagesHandler {
    private FileConfiguration config;
    private String prefix;

    public MessagesHandler(FileConfiguration config) {
        this.config = config;
        this.updatePrefix();
    }

    public void setConfiguration(FileConfiguration newConfig) {
        this.config = newConfig;
        this.updatePrefix();
    }

    private void updatePrefix() {
        this.prefix = colorize(config.getString("prefix", "&8[&6i&eLike&6Homes&8] &f"));
    }

    // translate colors
    public String colorize(String text) {
        if (text == null)
            return "";
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
    }

    // auto-prefix
    public String getMessage(String path, String... replacements) {
        String msg = config.getString(path);
        if (msg == null)
            // bip bip error system
            return "§cMissing message: " + path;

        // if we pass a replacement (e.g. the name of the house), we replace it ez
        if (replacements.length > 0) {
            msg = msg.replace("%home%", replacements[0]);
        }

        if (replacements.length > 1) {
            msg = msg.replace("%seconds%", replacements[1]);
        }

        return prefix + colorize(msg);
    }

    public String getRawMessage(String path, String... replacements) {
        String msg = config.getString(path);
        if (msg == null)
            return "§cMissing message: " + path;

        if (replacements.length > 0) {
            msg = msg.replace("%home%", replacements[0]);
        }

        if (replacements.length > 1) {
            msg = msg.replace("%seconds%", replacements[1]);
        }

        return colorize(msg); // no prefix for better looking gui
    }
}
