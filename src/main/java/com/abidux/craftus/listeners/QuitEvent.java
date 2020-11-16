package com.abidux.craftus.listeners;


import com.abidux.craftus.Main;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.objects.CraftUsProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    void quit(PlayerQuitEvent e) {
        CraftUsProfile profile = Main.profiles.get(e.getPlayer());
        if (profile.getCurrentMatch() != null) {
            profile.getCurrentMatch().leave(e.getPlayer(), true);
        }
        Matches.leave(profile);
        Main.profiles.remove(e.getPlayer());
    }

}