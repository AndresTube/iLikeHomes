package com.fendrixx.iLikeHomes;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.gui.GuiManager;
import com.fendrixx.iLikeHomes.handlers.DelHomeTabCompleter;
import com.fendrixx.iLikeHomes.handlers.HomeTabCompleter;
import com.fendrixx.iLikeHomes.handlers.ReloadTabCompleter;
import com.fendrixx.iLikeHomes.listeners.InventoryListener;
import com.fendrixx.iLikeHomes.managers.TeleportManager;

import net.md_5.bungee.api.ChatColor;

import com.fendrixx.iLikeHomes.commands.SetHomeCommand;
import com.fendrixx.iLikeHomes.commands.DelHomeCommand;
import com.fendrixx.iLikeHomes.commands.HomeCommand;
import com.fendrixx.iLikeHomes.commands.HomesCommand;
import com.fendrixx.iLikeHomes.commands.ReloadCommand;
import com.fendrixx.iLikeHomes.config.MessagesHandler;

public class Main extends JavaPlugin {
    private ConfigHandler configHandler;
    private GuiManager guiManager;
    private MessagesHandler messageManager;
    private TeleportManager teleportManager;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // create config.yml
        saveResource("messages.yml", false); // create messages.yml
        createConfigurationHomes(); // create homes.yml

        // load configs
        File messagesFile = new File(getDataFolder(), "messages.yml");
        FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        File homesFile = new File(getDataFolder(), "homes.yml");
        FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        // initialize handlers/managers
        this.configHandler = new ConfigHandler(homesConfig, homesFile);
        this.messageManager = new MessagesHandler(messagesConfig);
        this.teleportManager = new TeleportManager(this, this.messageManager, this.configHandler);
        this.guiManager = new GuiManager(this.configHandler, this.messageManager);

        // commands
        getCommand("sethome").setExecutor(new SetHomeCommand(this.configHandler, this.messageManager, this));
        getCommand("homes").setExecutor(new HomesCommand(this.guiManager));
        getCommand("home").setExecutor(new HomeCommand(this.configHandler, this.messageManager, this.teleportManager));
        getCommand("ilikehomes").setExecutor(new ReloadCommand(this));
        getCommand("ilikehomes").setTabCompleter(new ReloadTabCompleter());
        getCommand("home").setTabCompleter(new HomeTabCompleter(this.configHandler));
        getCommand("delhome").setExecutor(new DelHomeCommand(this.configHandler, this.messageManager, this));
        getCommand("delhome").setTabCompleter(new DelHomeTabCompleter(this.configHandler));

        getServer().getPluginManager().registerEvents(
                new InventoryListener(this.guiManager, this.configHandler, teleportManager, messageManager), this);
        getServer().getPluginManager().registerEvents(this.teleportManager, this);
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6i&eLike&6Homes&8] &aPlugin enabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8[&6i&eLike&6Homes&8] &ety for using my plugin! &8~ Fendrixx"));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6i&eLike&6Homes&8] &cPlugin disabled!"));
    }

    public void createConfigurationHomes() {
        File homeFile = new File(getDataFolder(), "homes.yml");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        if (!homeFile.exists()) {
            try {
                homeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reloadPluginConfig() {
        // reload config.yml
        reloadConfig();

        // reload messages.yml
        File messagesFile = new File(getDataFolder(), "messages.yml");
        FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        this.messageManager.setConfiguration(messagesConfig);

        // update handlers
        this.teleportManager = new TeleportManager(this, this.messageManager, this.configHandler);

        // update gui manager
        this.guiManager = new GuiManager(this.configHandler, this.messageManager);

        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6i&eLike&6Homes&8] &aPlugin reloaded!"));
    }
}