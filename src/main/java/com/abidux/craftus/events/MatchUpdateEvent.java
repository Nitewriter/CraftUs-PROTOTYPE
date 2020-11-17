package com.abidux.craftus.events;

import com.abidux.craftus.runnables.MatchUpdateRunnable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MatchUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final MatchUpdateRunnable runnable;

    public MatchUpdateEvent(MatchUpdateRunnable runnable) {
        this.runnable = runnable;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @SuppressWarnings("unused")
    public MatchUpdateRunnable getRunnable() {
        return runnable;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
