package com.abidux.craftus.events;

import com.abidux.craftus.game.GameMatch;
import com.abidux.craftus.models.CraftUsPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameMatchPlayerAttackedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final CraftUsPlayer attacker;
    private final CraftUsPlayer victim;
    private final GameMatch gameMatch;

    public GameMatchPlayerAttackedEvent(CraftUsPlayer attacker, CraftUsPlayer victim, GameMatch gameMatch) {
        this.attacker = attacker;
        this.victim = victim;
        this.gameMatch = gameMatch;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CraftUsPlayer getAttacker() {
        return attacker;
    }

    public CraftUsPlayer getVictim() {
        return victim;
    }

    public GameMatch getGameMatch() {
        return gameMatch;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
