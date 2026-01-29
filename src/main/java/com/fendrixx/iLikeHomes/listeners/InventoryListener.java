package com.fendrixx.iLikeHomes.listeners;

import java.util.List;

import org.bukkit.Location;
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

    public InventoryListener(GuiManager guiManager, ConfigHandler configHandler, TeleportManager teleportManager,
            MessagesHandler messageManager) {
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

        // validate that it's not air
        if (clicked == null || !clicked.hasItemMeta())
            return;
        List<String> lore = clicked.getItemMeta().getLore();
        if (lore == null || lore.isEmpty())
            return;

        // clean prefix to get the real name
        String displayName = lore.get(0).replace("§8ID: ", "");

        // get location
        Location loc = configHandler.getHomeLocation(p.getUniqueId(), displayName);

        if (loc != null) {
            teleportManager.setupTeleport(p, loc, displayName);
            p.closeInventory();
        } else {
            p.sendMessage(messageManager.getMessage("no-home-error"));
        }
    }
}
