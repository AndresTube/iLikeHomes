package com.fendrixx.iLikeHomes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.fendrixx.iLikeHomes.Main;
import com.fendrixx.iLikeHomes.utils.TextCenter;

public class ReloadCommand implements CommandExecutor {
    private Main plugin;

    public ReloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            String text = "§a======== §6i§eLike§6Homes §aHelp ========\n"
                    + "§a/sethome <name> §7- Set a home\n"
                    + "§a/home <name> §7- Teleport to a home\n"
                    + "§a/delhome <name> §7- Delete a home\n"
                    + "§a/homes §7- Open a GUI with all your homes\n"
                    + "§a/ilikehomes reload §7- Reload the plugin\n"
                    + "§a/ilikehomes help §7- Shows this message\n"
                    + "§a=========================================";
            String centered = TextCenter.centerText(text);
            sender.sendMessage(centered);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("ilikehomes.reload")) {
                plugin.reloadPluginConfig();
                sender.sendMessage("§8[§6i§eLike§6Homes§8] §aPlugin reloaded!");
                return true;
            }
            sender.sendMessage("§8[§6i§eLike§6Homes§8] §cYou don't have permission to use this command!");
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("ilikehomes.help")) {
                String text = "§a======== §6i§eLike§6Homes §aHelp ========\n"
                        + "§a/sethome <name> §7- Set a home\n"
                        + "§a/home <name> §7- Teleport to a home\n"
                        + "§a/delhome <name> §7- Delete a home\n"
                        + "§a/homes §7- Open a GUI with all your homes\n"
                        + "§a/ilikehomes reload §7- Reload the plugin\n"
                        + "§a/ilikehomes help §7- Shows this message\n"
                        + "§a=========================================";
                String centered = TextCenter.centerText(text);
                sender.sendMessage(centered);
                return true;
            }
            sender.sendMessage("§8[§6i§eLike§6Homes§8] §cYou don't have permission to use this command!");
            return true;
        }
        return true;
    }
}