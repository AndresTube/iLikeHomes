package com.fendrixx.iLikeHomes.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

    private FileConfiguration config;
    private File file;

    // constructor: obtains the config and file
    public ConfigHandler(FileConfiguration config, File file) {
        this.config = config;
        this.file = file;
    }

    // method to save location
    public void saveHome(UUID uuid, String name, Location loc) {
        String path = "players." + uuid + "." + name;

        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", loc.getX());
        config.set(path + ".y", loc.getY());
        config.set(path + ".z", loc.getZ());
        config.set(path + ".yaw", loc.getYaw());
        config.set(path + ".pitch", loc.getPitch());

        saveHomeFile();
    }

    public Set<String> getHomeNames(UUID uuid) {
        String path = "players." + uuid;

        // get the section
        ConfigurationSection section = config.getConfigurationSection(path);

        // if no section, return empty set
        if (section == null) {
            return new HashSet<>();
        }

        // get keys/home names
        return section.getKeys(false);
    }

    private void saveHomeFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getHomeLocation(UUID uniqueId, String displayName) {
        String path = "players." + uniqueId + "." + displayName;

        // get the section
        ConfigurationSection section = config.getConfigurationSection(path);

        // if no section, return null
        if (section == null) {
            return null;
        }

        // get location
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");

        World world = Bukkit.getWorld(section.getString("world"));

        if (world == null) {
            return null; // bip bip error system
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    public int getHomeCount(UUID uuid) {
        if (config.getConfigurationSection("players." + uuid.toString()) == null) {
            return 0;
        }
        return config.getConfigurationSection("players." + uuid.toString()).getKeys(false).size();
    }

    public List<String> getHomeList(UUID uuid) {
        List<String> homes = new ArrayList<>();
        String path = "players." + uuid.toString();

        if (config.getConfigurationSection(path) != null) {
            homes.addAll(config.getConfigurationSection(path).getKeys(false));
        }
        return homes;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteHome(UUID uuid, String homeName) {
        config.set("players." + uuid.toString() + "." + homeName, null);
        saveConfig();
    }
}