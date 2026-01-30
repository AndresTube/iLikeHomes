package com.fendrixx.iLikeHomes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.fendrixx.iLikeHomes.Main;

public class ReloadCommand implements CommandExecutor {
    private Main plugin;

    public ReloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // idk how to center a message soooo
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a======== §6i§eLike§6Homes §aHelp ========");
            sender.sendMessage("§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/sethome <name> §7- Set a home");
            sender.sendMessage("§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/home <name> §7- Teleport to a home");
            sender.sendMessage("§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/delhome <name> §7- Delete a home");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/homes §7- Open a GUI with all your homes");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/ilikehomes reload §7- Reload the plugin");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/ilikehomes help §7- Shows this message");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a=========================================");
            return true;
            // this is a foking disaster LOL
        }
        // check permissions
        if (!sender.hasPermission("ilikehomes.admin")) {
            sender.sendMessage("§8[§6i§eLike§6Homes§8] §cYou don't have permission to use this command!");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadPluginConfig();
            sender.sendMessage("§8[§6i§eLike§6Homes§8] §aPlugin reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            // idk how to center a message x2
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a======== §6i§eLike§6Homes §aHelp ========");
            sender.sendMessage("§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/sethome <name> §7- Set a home");
            sender.sendMessage("§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/home <name> §7- Teleport to a home");
            sender.sendMessage("§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/delhome <name> §7- Delete a home");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/homes §7- Open a GUI with all your homes");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/ilikehomes reload §7- Reload the plugin");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a/ilikehomes help §7- Shows this message");
            sender.sendMessage(
                    "§r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §r §a=========================================");
            return true;
            // this is a foking disaster bro, pls make a method to just put idk,
            // sender.sendCenteredMessage()
        }

        return true;
    }
}