package br.com.abidux.craftus.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.objects.CraftUsProfile;

public class Join implements Listener {
	
	@EventHandler
	void join(PlayerJoinEvent e) {
		Main.profiles.put(e.getPlayer(), new CraftUsProfile(e.getPlayer()));
	}
	
}