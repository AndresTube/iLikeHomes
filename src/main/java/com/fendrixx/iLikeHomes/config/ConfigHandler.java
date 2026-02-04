package com.fendrixx.iLikeHomes.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;

import com.fendrixx.iLikeHomes.storage.Storage;

public class ConfigHandler {

    private final Storage storage;

    public ConfigHandler(Storage storage) {
        this.storage = storage;
    }

    public void saveHome(UUID uuid, String name, Location loc, String icon) {
        storage.saveHome(uuid, name, loc, icon);
    }

    public Set<String> getHomeNames(UUID uuid) {
        return storage.getHomeNames(uuid);
    }

    public Location getHomeLocation(UUID uuid, String name) {
        return storage.getHomeLocation(uuid, name);
    }

    public String getHomeIcon(UUID uuid, String name) {
        return storage.getHomeIcon(uuid, name);
    }

    public int getHomeCount(UUID uuid) {
        return storage.getHomeNames(uuid).size();
    }

    public List<String> getHomeList(UUID uuid) {
        return new ArrayList<>(storage.getHomeNames(uuid));
    }

    public void deleteHome(UUID uuid, String homeName) {
        storage.deleteHome(uuid, homeName);
    }

    public void reload() {
        storage.reload();
    }
}