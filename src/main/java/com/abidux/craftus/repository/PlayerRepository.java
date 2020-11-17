package com.abidux.craftus.repository;

import com.abidux.craftus.models.CraftUsPlayer;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

@Singleton
public class PlayerRepository extends AbstractModule {
    private final HashMap<UUID, CraftUsPlayer> players = new HashMap<>();

    public @Nullable CraftUsPlayer getPlayerForId(UUID id) {
        return players.get(id);
    }

    public void addPlayer(Player player) {
        CraftUsPlayer craftUsPlayer = new CraftUsPlayer(player);
        players.put(player.getUniqueId(), craftUsPlayer);
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }
}
