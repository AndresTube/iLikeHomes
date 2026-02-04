package com.fendrixx.iLikeHomes.storage;

import org.bukkit.Location;
import java.util.Set;
import java.util.UUID;

public interface Storage {
    void saveHome(UUID uuid, String name, Location loc, String icon);

    void deleteHome(UUID uuid, String name);

    Location getHomeLocation(UUID uuid, String name);

    String getHomeIcon(UUID uuid, String name);

    Set<String> getHomeNames(UUID uuid);

    void reload();
}
