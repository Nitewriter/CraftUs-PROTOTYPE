package com.abidux.craftus.config;

import com.abidux.craftus.CraftUs;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

@Singleton
public class CraftUsConfig {
    private final CraftUs plugin;
    private Location lobbyLocation;

    @Inject
    public CraftUsConfig(CraftUs plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public void load() {
        plugin.reloadConfig();
        lobbyLocation = plugin.getConfig().getLocation("lobby");
    }

    public void save() {
        plugin.saveConfig();
    }

    public @Nullable Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location location) {
        lobbyLocation = location;
        plugin.getConfig().set("lobby", location);
    }
}
