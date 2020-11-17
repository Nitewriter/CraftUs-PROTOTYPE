package com.abidux.craftus.events;

import com.abidux.craftus.game.GameMap;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GameMapsLoadedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final List<GameMap> gameMaps;

    public GameMapsLoadedEvent(List<GameMap> gameMaps) {
        this.gameMaps = gameMaps;
    }

    @SuppressWarnings("unused")
    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    public List<GameMap> getGameMaps() {
        return gameMaps;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
