package com.fendrixx.iLikeHomes.commands;

import org.bukkit.Bukkit;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;
import com.fendrixx.iLikeHomes.Main;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

public class HomeSuggestions implements SuggestionProvider<BukkitCommandActor> {

    @Override
    public Collection<String> getSuggestions(ExecutionContext<BukkitCommandActor> context) {
        BukkitCommandActor actor = context.actor();
        if (actor.isPlayer()) {
            Main plugin = (Main) Bukkit.getPluginManager().getPlugin("iLikeHomes");
            if (plugin != null && plugin.getCacheManager() != null) {
                return new ArrayList<>(plugin.getCacheManager().getHomeNames(actor.uniqueId()));
            }
        }
        return Collections.emptyList();
    }
}
