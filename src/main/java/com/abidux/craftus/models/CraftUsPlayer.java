package com.abidux.craftus.models;

import com.abidux.craftus.game.GameMatch;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CraftUsPlayer {
    private final Player player;
    private GameMatch currentGameMatch = null;

    public CraftUsPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public @Nullable GameMatch getCurrentMatch() {
        return currentGameMatch;
    }

    public void setCurrentMatch(GameMatch newGameMatch) {
        currentGameMatch = newGameMatch;
    }

    public boolean isNotPlayingMatch() {
        return currentGameMatch == null;
    }
}
