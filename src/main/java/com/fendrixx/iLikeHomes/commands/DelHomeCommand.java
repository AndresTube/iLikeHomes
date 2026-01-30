package com.fendrixx.iLikeHomes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.config.MessagesHandler;

public class DelHomeCommand implements CommandExecutor {
    private ConfigHandler configHandler;
    private MessagesHandler messageManager;
    private Plugin plugin;

    public DelHomeCommand(ConfigHandler configHandler, MessagesHandler messageManager, Plugin plugin) {
        this.configHandler = configHandler;
        this.messageManager = messageManager;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage(messageManager.getMessage("wrong-usage-delhome"));
            return true;
        }

        String homeName = args[0];

        if (!configHandler.getHomeList(p.getUniqueId()).contains(homeName)) {
            p.sendMessage(messageManager.getMessage("home-doesnt-exist"));
            return true;
        }

        configHandler.deleteHome(p.getUniqueId(), homeName);
        p.sendMessage(messageManager.getMessage("home-deleted", homeName));
        return true;
    }
}