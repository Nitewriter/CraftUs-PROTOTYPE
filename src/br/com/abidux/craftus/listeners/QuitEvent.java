package br.com.abidux.craftus.listeners;

import org.bukkit.event.player.PlayerQuitEvent;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.Matches;
import br.com.abidux.craftus.api.objects.CraftUsProfile;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

public class QuitEvent implements Listener {
	
	@EventHandler
	void quit(PlayerQuitEvent e) {
		CraftUsProfile profile = Main.profiles.get(e.getPlayer());
		if (profile.getCurrentMatch() != null) profile.getCurrentMatch().leave(e.getPlayer(), true);
		Matches.leave(profile);
		Main.profiles.remove(e.getPlayer());
	}
	
}