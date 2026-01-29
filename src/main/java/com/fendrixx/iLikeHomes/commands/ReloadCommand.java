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
            sender.sendMessage("§8[§6i§eLike§6Homes§8] §cWrong usage! use &f/ilikehomes");
            return true;
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
        return true;
    }
}