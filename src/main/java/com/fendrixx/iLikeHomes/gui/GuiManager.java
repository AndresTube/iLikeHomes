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
    private final org.bukkit.plugin.Plugin plugin;

    public GuiManager(org.bukkit.plugin.Plugin plugin, ConfigHandler configHandler, MessagesHandler messageManager) {
        this.plugin = plugin;
        this.configHandler = configHandler;
        this.messageManager = messageManager;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public void openGui(Player p) {
        String title = messageManager.getRawMessage("gui-title");
        Inventory gui = Bukkit.createInventory(this, 27, title);

        Set<String> homes = configHandler.getHomeNames(p.getUniqueId());

        if (homes == null || homes.isEmpty()) {
            p.sendMessage(messageManager.getMessage("no-home-error"));
            return;
        }

        for (String name : homes) {
            String iconName = "CHEST";
            if (plugin.getConfig().getBoolean("icons.enabled", true)) {
                iconName = configHandler.getHomeIcon(p.getUniqueId(), name);
            }
            Material material = Material.matchMaterial(iconName);
            if (material == null)
                material = Material.CHEST;

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(messageManager.getRawMessage("home-item-name", name));
            List<String> lore = new ArrayList<>();
            lore.add("§8ID: " + name);
            lore.add(messageManager.getRawMessage("home-item-lore"));
            meta.setLore(lore);
            item.setItemMeta(meta);

            gui.addItem(item);
        }

        ItemStack item = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(messageManager.getRawMessage("new-home-item-name"));
        String lore = messageManager.getRawMessage("new-home-item-lore");
        meta.setLore(List.of(lore));
        item.setItemMeta(meta);
        gui.setItem(26, item);

        p.openInventory(gui);
    }

    // feature asked by a friend lol
    public void openIconPicker(Player p, String homeName) {
        String title = messageManager.getRawMessage("icon-picker-title") + " (" + homeName + ")";
        Inventory gui = Bukkit.createInventory(this, 27, title);

        List<String> icons = plugin.getConfig().getStringList("icons.available-icons");

        if (icons.isEmpty()) {
            icons = List.of("CHEST", "ENDER_CHEST", "GRASS_BLOCK", "COBBLESTONE");
        }

        for (String iconName : icons) {
            Material material = Material.matchMaterial(iconName);
            if (material != null) {
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§f" + iconName.replace("_", " "));
                item.setItemMeta(meta);
                gui.addItem(item);
            }
        }

        p.openInventory(gui);
    }
}
