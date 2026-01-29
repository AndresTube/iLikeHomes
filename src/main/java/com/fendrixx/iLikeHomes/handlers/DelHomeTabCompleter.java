package com.fendrixx.iLikeHomes.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import com.fendrixx.iLikeHomes.config.ConfigHandler;
import java.util.List;
import java.util.ArrayList;

public class DelHomeTabCompleter implements TabCompleter {
    private final ConfigHandler configHandler;

    public DelHomeTabCompleter(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            Player p = (Player) sender;
            return configHandler.getHomeList(p.getUniqueId());
        }
        return new ArrayList<>();
    }
}