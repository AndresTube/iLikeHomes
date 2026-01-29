package com.fendrixx.iLikeHomes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fendrixx.iLikeHomes.gui.GuiManager;

public class HomesCommand implements CommandExecutor {
    private GuiManager guiManager;

    public HomesCommand(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("homes")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§8[§6i§eLike§6Homes§8] §cYou must be a player to use this command.");
                return true;
            }
            Player p = (Player) sender;
            guiManager.openGui(p); // open gui
            return true;
        }
        return false;
    }
}
