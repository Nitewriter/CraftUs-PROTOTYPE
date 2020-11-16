package com.abidux.craftus.listeners;


import com.abidux.craftus.Main;
import com.abidux.craftus.api.objects.CraftUsProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    void join(PlayerJoinEvent e) {
        Main.profiles.put(e.getPlayer(), new CraftUsProfile(e.getPlayer()));
    }

}