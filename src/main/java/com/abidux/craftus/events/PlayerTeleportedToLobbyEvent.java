package com.abidux.craftus.events;

import com.abidux.craftus.models.CraftUsPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerTeleportedToLobbyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final CraftUsPlayer craftUsPlayer;

    public PlayerTeleportedToLobbyEvent(CraftUsPlayer craftUsPlayer) {
        this.craftUsPlayer = craftUsPlayer;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CraftUsPlayer getCraftUsPlayer() {
        return craftUsPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
