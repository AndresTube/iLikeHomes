package com.fendrixx.iLikeHomes.commands;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.SuggestWith;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.config.MessagesHandler;
import com.fendrixx.iLikeHomes.managers.TeleportManager;
import com.fendrixx.iLikeHomes.gui.GuiManager;

public class HomeCommands {

    private final ConfigHandler configHandler;
    private final MessagesHandler messageManager;
    private final TeleportManager teleportManager;
    private final GuiManager guiManager;
    private final com.fendrixx.iLikeHomes.managers.CacheManager cacheManager;
    private final Plugin plugin;

    public HomeCommands(ConfigHandler configHandler, MessagesHandler messageManager, TeleportManager teleportManager,
            GuiManager guiManager, com.fendrixx.iLikeHomes.managers.CacheManager cacheManager, Plugin plugin) {
        this.configHandler = configHandler;
        this.messageManager = messageManager;
        this.teleportManager = teleportManager;
        this.guiManager = guiManager;
        this.cacheManager = cacheManager;
        this.plugin = plugin;
    }

    @Command("sethome")
    @Description("Set a home")
    public void sethome(Player p, @Optional String name) {
        if (name == null) {
            p.sendMessage(messageManager.getMessage("wrong-usage"));
            return;
        }

        int maxHomes = plugin.getConfig().getInt("max-homes", 5);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            int homeCount = configHandler.getHomeCount(p.getUniqueId());
            boolean isNewHouse = !configHandler.getHomeNames(p.getUniqueId()).contains(name);

            if (homeCount >= maxHomes && isNewHouse && !p.isOp()) {
                p.sendMessage(messageManager.getMessage("max-homes-reached", String.valueOf(maxHomes)));
                return;
            }

            configHandler.saveHome(p.getUniqueId(), name, p.getLocation(), "CHEST");
            cacheManager.addHomeName(p.getUniqueId(), name);
            p.sendMessage(messageManager.getMessage("home-set", name));

            if (plugin.getConfig().getBoolean("icons.enabled", true)) {
                Bukkit.getScheduler().runTask(plugin, () -> guiManager.openIconPicker(p, name));
            }
        });
    }

    @Command("home")
    @Description("Teleport to a home")
    public void home(Player p, @Default("home") @SuggestWith(HomeSuggestions.class) String name) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            org.bukkit.Location loc = configHandler.getHomeLocation(p.getUniqueId(), name);

            if (loc == null) {
                p.sendMessage(messageManager.getMessage("no-home-error", name));
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> teleportManager.setupTeleport(p, loc, name));
        });
    }

    @Command("delhome")
    @Description("Delete a home")
    public void delhome(Player p, @SuggestWith(HomeSuggestions.class) String name) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!configHandler.getHomeNames(p.getUniqueId()).contains(name)) {
                p.sendMessage(messageManager.getMessage("home-doesnt-exist"));
                return;
            }

            configHandler.deleteHome(p.getUniqueId(), name);
            cacheManager.removeHomeName(p.getUniqueId(), name);
            p.sendMessage(messageManager.getMessage("home-deleted", name));
        });
    }

    @Command("homes")
    @Description("Open the homes GUI")
    public void homes(Player p) {
        guiManager.openGui(p);
    }
}
