package com.abidux.craftus.events;

import com.abidux.craftus.models.CraftUsPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FindMatchForPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final CraftUsPlayer player;

    public FindMatchForPlayerEvent(CraftUsPlayer player) {
        this.player = player;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CraftUsPlayer getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
