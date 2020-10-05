package br.com.abidux.craftus.listeners;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.Maps;
import br.com.abidux.craftus.api.Matches;
import br.com.abidux.craftus.api.PlayerType;
import br.com.abidux.craftus.api.objects.CraftUsProfile;
import br.com.abidux.craftus.api.objects.Map;
import br.com.abidux.craftus.enums.Messages;

public class Chat implements Listener {
	
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Player> editingName = new ArrayList<Player>();
	
	@EventHandler
	void chat(AsyncPlayerChatEvent e) {
		if (players.contains(e.getPlayer())) {
			e.setCancelled(true);
			players.remove(e.getPlayer());
			Maps.createMap(e.getMessage().trim(), e.getPlayer().getLocation());
			e.getPlayer().sendMessage(Messages.PREFIX.toString() + Messages.MAP_CREATED);
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
			return;
		}
		if (editingName.contains(e.getPlayer())) {
			e.setCancelled(true);
			Map map = InvClick.editing.get(e.getPlayer());
			Maps.maps.remove(map.getName().toLowerCase());
			map.setName(e.getMessage().trim());
			Maps.maps.put(map.getName().toLowerCase(), map);
			InvClick.editing.remove(e.getPlayer());
			editingName.remove(e.getPlayer());
			e.getPlayer().sendMessage(Messages.PREFIX.toString()+Messages.MODIFIED);
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
			return;
		}
		CraftUsProfile profile = Main.profiles.get(e.getPlayer());
		if (profile.getCurrentMatch() != null) {
			e.setCancelled(true);
			if (!Matches.discussion.contains(profile.getCurrentMatch()) || (profile.getType().equals(PlayerType.DEAD_CREWMATE) || profile.getType().equals(PlayerType.DEAD_IMPOSTOR))) {
				e.getPlayer().sendMessage(Messages.PREFIX.toString()+Messages.YOU_ARE_NOT_ALLOWED_TO_DO_THIS);
				return;
			}
			profile.getCurrentMatch().getPlayers().stream().forEach(player -> player.sendMessage(profile.getColor().getChatColor()+"["+profile.getColor().toString()+"] "+e.getPlayer().getName()+": §f"+e.getMessage()));
		}
	}
	
}