package com.fendrixx.iLikeHomes.commands;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import org.bukkit.command.CommandSender;
import com.fendrixx.iLikeHomes.Main;

@Command({ "ilikehomes", "ilh" })
public class ReloadCommand {

    private final Main plugin;

    public ReloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @Description("Reload the plugin")
    @CommandPermission("ilikehomes.reload")
    public void reload(CommandSender sender) {
        plugin.reloadPluginConfig();
        sender.sendMessage("§8[§6i§eLike§6Homes§8] §aPlugin reloaded!");
    }

    @Subcommand("help")
    @Description("Shows help message")
    public void help(CommandSender sender) {
        sendHelp(sender);
    }

    private void sendHelp(CommandSender sender) {
        String text = "§a======== §6i§eLike§6Homes §aHelp ========\n"
                + "§a/sethome <name> §7- Set a home\n"
                + "§a/home [name] §7- Teleport to a home\n"
                + "§a/delhome <name> §7- Delete a home\n"
                + "§a/homes §7- Open a GUI with all your homes\n"
                + "§a/ilikehomes reload §7- Reload the plugin\n"
                + "§a/ilikehomes help §7- Shows this message\n"
                + "§a==============================";
        sender.sendMessage(text);
    }
}