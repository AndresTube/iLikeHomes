package com.fendrixx.iLikeHomes.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.config.MessagesHandler;

public class TeleportManager implements Listener {
    private final Map<UUID, BukkitTask> waitingPlayers = new HashMap<>();
    private final MessagesHandler messageManager;
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;

    public TeleportManager(JavaPlugin plugin, MessagesHandler messageManager, ConfigHandler configHandler) {
        this.plugin = plugin;
        this.messageManager = messageManager;
        this.configHandler = configHandler;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null)
            return;

        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            Player player = event.getPlayer();
            if (waitingPlayers.containsKey(player.getUniqueId())) {
                cancelTeleport(player);
            }
        }
    }

    public void setupTeleport(Player player, Location destination, String homeName) {
        UUID uuid = player.getUniqueId();

        if (waitingPlayers.containsKey(uuid)) {
            cancelTeleport(player);
        }

        int seconds = plugin.getConfig().getInt("teleport-warmup", 3);
        player.sendMessage(messageManager.getMessage("teleport-start", homeName, String.valueOf(seconds)));

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(destination);
                player.sendMessage(messageManager.getMessage("teleport-success", homeName));
                waitingPlayers.remove(uuid);
            }
        }.runTaskLater(plugin, seconds * 20L);

        waitingPlayers.put(uuid, task);
    }

    public void cancelTeleport(Player player) {
        UUID uuid = player.getUniqueId();

        if (waitingPlayers.containsKey(uuid)) {
            waitingPlayers.get(uuid).cancel();
            waitingPlayers.remove(uuid);
            player.sendMessage(messageManager.getMessage("teleport-cancelled"));
        }
    }
}
