package com.fendrixx.iLikeHomes.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ReloadTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("ilikehomes")) {
            // only first argument
            if (args.length == 1) {
                return List.of("reload", "help");
            }
        }
        return new ArrayList<>();
    }
}
