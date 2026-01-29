package com.fendrixx.iLikeHomes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.config.MessagesHandler;

public class SetHomeCommand implements CommandExecutor {
    private ConfigHandler configHandler;
    private MessagesHandler messageManager;
    private Plugin plugin;

    public SetHomeCommand(ConfigHandler configHandler, MessagesHandler messageManager, Plugin plugin) {
        this.configHandler = configHandler;
        this.messageManager = messageManager;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("sethome")) {
            if (args.length == 0) {
                p.sendMessage(messageManager.getMessage("wrong-usage"));
                return true;
            } else {
                int maxHomes = plugin.getConfig().getInt("max-homes", 5);
                int homeCount = configHandler.getHomeCount(p.getUniqueId());
                String homeName = args[0];
                boolean isNewHouse = !configHandler.getHomeList(p.getUniqueId()).contains(homeName);

                if (homeCount >= maxHomes && isNewHouse && !p.isOp()) {
                    p.sendMessage(messageManager.getMessage("max-homes-reached", String.valueOf(maxHomes)));
                    return true;
                } else {
                    p.sendMessage(messageManager.getMessage("home-set", homeName));
                    configHandler.saveHome(p.getUniqueId(), homeName, p.getLocation());
                    return true;
                }
            }
        }
        return false;
    }
}
