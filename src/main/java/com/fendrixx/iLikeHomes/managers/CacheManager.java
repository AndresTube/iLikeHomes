package com.fendrixx.iLikeHomes.managers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private final Map<UUID, Set<String>> homeNameCache = new ConcurrentHashMap<>();

    public void setHomeNames(UUID uuid, Set<String> names) {
        homeNameCache.put(uuid, new HashSet<>(names));
    }

    public Set<String> getHomeNames(UUID uuid) {
        return homeNameCache.getOrDefault(uuid, Collections.emptySet());
    }

    public void addHomeName(UUID uuid, String name) {
        homeNameCache.computeIfAbsent(uuid, k -> new HashSet<>()).add(name);
    }

    public void removeHomeName(UUID uuid, String name) {
        Set<String> names = homeNameCache.get(uuid);
        if (names != null) {
            names.remove(name);
        }
    }

    public void invalidate(UUID uuid) {
        homeNameCache.remove(uuid);
    }

    public void clearAll() {
        homeNameCache.clear();
    }
}
