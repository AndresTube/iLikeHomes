package com.fendrixx.iLikeHomes.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.gui.GuiManager;
import com.fendrixx.iLikeHomes.managers.TeleportManager;
import com.fendrixx.iLikeHomes.config.MessagesHandler;

public class InventoryListener implements Listener {
    private GuiManager guiManager;
    private ConfigHandler configHandler;
    private MessagesHandler messageManager;
    private TeleportManager teleportManager;
    private org.bukkit.plugin.Plugin plugin;

    public InventoryListener(org.bukkit.plugin.Plugin plugin, GuiManager guiManager, ConfigHandler configHandler,
            TeleportManager teleportManager,
            MessagesHandler messageManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.configHandler = configHandler;
        this.teleportManager = teleportManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof GuiManager)) {
            return;
        }
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType() == Material.AIR)
            return;

        String title = event.getView().getTitle();
        String pickerTitle = messageManager.getRawMessage("icon-picker-title");

        if (title.startsWith(pickerTitle)) {
            if (!plugin.getConfig().getBoolean("icons.enabled", true)) {
                p.closeInventory();
                return;
            }
            String homeName = title.substring(title.lastIndexOf("(") + 1, title.lastIndexOf(")"));
            String iconName = clicked.getType().name();

            org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

                Location loc = configHandler.getHomeLocation(p.getUniqueId(), homeName);
                if (loc != null) {
                    configHandler.saveHome(p.getUniqueId(), homeName, loc, iconName);
                    p.sendMessage(messageManager.getMessage("icon-selected", homeName));
                }
                org.bukkit.Bukkit.getScheduler().runTask(plugin, p::closeInventory);
            });
            return;
        }

        if (clicked.getType() == Material.GREEN_WOOL) {
            p.closeInventory();
            net.md_5.bungee.api.chat.TextComponent message = new net.md_5.bungee.api.chat.TextComponent(
                    messageManager.getMessage("new-home-item-message"));
            message.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                    net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/sethome "));
            p.spigot().sendMessage(message);
            return;
        }

        if (!clicked.hasItemMeta())
            return;

        List<String> lore = clicked.getItemMeta().getLore();
        if (lore == null || lore.isEmpty())
            return;

        String displayName = lore.get(0).replace("§8ID: ", "");

        org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Location loc = configHandler.getHomeLocation(p.getUniqueId(), displayName);

            org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
                if (loc != null) {
                    teleportManager.setupTeleport(p, loc, displayName);
                    p.closeInventory();
                } else {
                    p.sendMessage(messageManager.getMessage("no-home-error"));
                }
            });
        });

    }
}
