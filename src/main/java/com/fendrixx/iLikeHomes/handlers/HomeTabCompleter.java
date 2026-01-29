package com.fendrixx.iLikeHomes.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import com.fendrixx.iLikeHomes.config.ConfigHandler;

public class HomeTabCompleter implements TabCompleter {
    private ConfigHandler configHandler;

    public HomeTabCompleter(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("home")) {
            // only first argument
            if (args.length == 1 && sender instanceof Player) {
                Player p = (Player) sender;
                // get home list of player
                return configHandler.getHomeList(p.getUniqueId());
            }
        }
        return new ArrayList<>();
    }
}