package br.com.abidux.craftus.listeners;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.objects.CraftUsProfile;

import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItem implements Listener {
	
	@EventHandler
	void drop(PlayerDropItemEvent e) {
		CraftUsProfile profile = Main.profiles.get(e.getPlayer());
		if (profile.getCurrentMatch() != null) e.setCancelled(true);
	}
	
}