package com.abidux.craftus.runnables;

import com.abidux.craftus.events.MatchUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchUpdateRunnable extends BukkitRunnable {
    @Override
    public void run() {
        Event event = new MatchUpdateEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }
}
