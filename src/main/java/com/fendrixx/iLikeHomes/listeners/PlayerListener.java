package com.fendrixx.iLikeHomes.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.managers.CacheManager;

public class PlayerListener implements Listener {

    private final Plugin plugin;
    private final CacheManager cacheManager;
    private final ConfigHandler configHandler;

    public PlayerListener(Plugin plugin, CacheManager cacheManager, ConfigHandler configHandler) {
        this.plugin = plugin;
        this.cacheManager = cacheManager;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            cacheManager.setHomeNames(event.getPlayer().getUniqueId(),
                    configHandler.getHomeNames(event.getPlayer().getUniqueId()));
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cacheManager.invalidate(event.getPlayer().getUniqueId());
    }
}
