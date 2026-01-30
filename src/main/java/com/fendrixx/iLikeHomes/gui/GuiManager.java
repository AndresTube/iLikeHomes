package com.fendrixx.iLikeHomes.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.fendrixx.iLikeHomes.config.ConfigHandler;
import com.fendrixx.iLikeHomes.config.MessagesHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class GuiManager implements InventoryHolder {
    private MessagesHandler messageManager;
    private ConfigHandler configHandler;

    public GuiManager(ConfigHandler configHandler, MessagesHandler messageManager) {
        this.configHandler = configHandler;
        this.messageManager = messageManager;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public void openGui(Player p) {
        // empty inventory
        String title = messageManager.getRawMessage("gui-title");
        Inventory gui = Bukkit.createInventory(this, 27, title);

        // ask to ConfigHandler
        Set<String> homes = configHandler.getHomeNames(p.getUniqueId());

        if (homes == null || homes.isEmpty()) {
            p.sendMessage(messageManager.getMessage("no-home-error"));
            return;
        }

        // for every name
        for (String name : homes) {
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(messageManager.getRawMessage("home-item-name", name));
            List<String> lore = new ArrayList<>();
            lore.add("§8ID: " + name); // invisible ID
            lore.add(messageManager.getRawMessage("home-item-lore"));
            meta.setLore(lore);
            item.setItemMeta(meta);

            gui.addItem(item); // put in order
        }

        p.openInventory(gui);
    }
}
