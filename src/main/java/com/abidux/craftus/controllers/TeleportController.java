package com.abidux.craftus.controllers;

import com.abidux.craftus.config.CraftUsConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Singleton
public class TeleportController {
    private final CraftUsConfig config;

    @Inject
    public TeleportController(CraftUsConfig config) {
        this.config = config;
    }

    public void teleportPlayerToLobby(Player player) {
        Location location = config.getLobbyLocation();
        if (location == null) {
            player.sendMessage("No lobby has been set.");
            return;
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(location);
    }
}
