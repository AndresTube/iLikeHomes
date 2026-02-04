package com.fendrixx.iLikeHomes;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import com.fendrixx.iLikeHomes.commands.*;
import com.fendrixx.iLikeHomes.config.*;
import com.fendrixx.iLikeHomes.gui.GuiManager;
import com.fendrixx.iLikeHomes.listeners.InventoryListener;
import com.fendrixx.iLikeHomes.managers.TeleportManager;
import com.fendrixx.iLikeHomes.storage.*;

public class Main extends JavaPlugin {
    private Storage storage;
    private ConfigHandler configHandler;
    private GuiManager guiManager;
    private MessagesHandler messageManager;
    private TeleportManager teleportManager;
    private com.fendrixx.iLikeHomes.managers.CacheManager cacheManager;
    private Lamp<BukkitCommandActor> lamp;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);

        this.storage = new DatabaseStorage(this);

        File messagesFile = new File(getDataFolder(), "messages.yml");
        FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        this.configHandler = new ConfigHandler(this.storage);
        this.messageManager = new MessagesHandler(messagesConfig);
        this.teleportManager = new TeleportManager(this, this.messageManager, this.configHandler);
        this.guiManager = new GuiManager(this, this.configHandler, this.messageManager);
        this.cacheManager = new com.fendrixx.iLikeHomes.managers.CacheManager();

        this.lamp = BukkitLamp.builder(this).build();

        lamp.register(new HomeCommands(configHandler, messageManager, teleportManager, guiManager, cacheManager, this));
        lamp.register(new ReloadCommand(this));

        getServer().getPluginManager().registerEvents(
                new InventoryListener(this, this.guiManager, this.configHandler, teleportManager, messageManager),
                this);
        getServer().getPluginManager().registerEvents(
                new com.fendrixx.iLikeHomes.listeners.PlayerListener(this, cacheManager, configHandler), this);
        getServer().getPluginManager().registerEvents(this.teleportManager, this);

        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6i&eLike&6Homes&8] &aPlugin enabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8[&6i&eLike&6Homes&8] &ety for using my plugin! &8~ Fendrixx"));
    }

    @Override
    public void onDisable() {
        if (this.storage != null) {
            this.storage.reload();
        }
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6i&eLike&6Homes&8] &cPlugin disabled!"));
    }

    public void reloadPluginConfig() {
        reloadConfig();

        if (this.storage != null) {
            this.storage.reload();
        }
        this.storage = new DatabaseStorage(this);

        this.configHandler = new ConfigHandler(this.storage);

        File messagesFile = new File(getDataFolder(), "messages.yml");
        FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        this.messageManager.setConfiguration(messagesConfig);

        this.teleportManager = new TeleportManager(this, this.messageManager, this.configHandler);
        this.guiManager = new GuiManager(this, this.configHandler, this.messageManager);

        this.cacheManager.clearAll();
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                cacheManager.setHomeNames(p.getUniqueId(), configHandler.getHomeNames(p.getUniqueId()));
            });
        }

        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6i&eLike&6Homes&8] &aPlugin reloaded!"));
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public com.fendrixx.iLikeHomes.managers.CacheManager getCacheManager() {
        return cacheManager;
    }
}