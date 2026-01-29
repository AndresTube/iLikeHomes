package com.fendrixx.iLikeHomes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.config.MessagesHandler;
import com.fendrixx.iLikeHomes.managers.TeleportManager;

public class HomeCommand implements CommandExecutor {
    private ConfigHandler configHandler;
    private MessagesHandler messageManager;
    private TeleportManager teleportManager;

    public HomeCommand(ConfigHandler configHandler, MessagesHandler messageManager, TeleportManager teleportManager) {
        this.configHandler = configHandler;
        this.messageManager = messageManager;
        this.teleportManager = teleportManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("home")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§8[§6i§eLike§6Homes§8] §c You must be a player to use this command.");
                return true;
            }
            String homeName;

            if (args.length > 0) {
                homeName = args[0];
            } else {
                homeName = "home";
            }
            Player p = (Player) sender;
            Location loc = configHandler.getHomeLocation(p.getUniqueId(), homeName);

            if (loc == null) {
                p.sendMessage(messageManager.getMessage("no-home-error", homeName));
                return true;
            }
            teleportManager.setupTeleport(p, loc, homeName);
            return true;
        }
        return false;
    }
}
