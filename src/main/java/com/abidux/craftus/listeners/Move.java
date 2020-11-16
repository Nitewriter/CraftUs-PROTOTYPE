package com.abidux.craftus.listeners;


import com.abidux.craftus.Main;
import com.abidux.craftus.api.MatchState;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.objects.Match;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Move implements Listener {

    @EventHandler
    void move(PlayerMoveEvent e) {
        Match match = Main.profiles.get(e.getPlayer()).getCurrentMatch();
        if (match != null) {
			if (match.getState() == MatchState.WAITING_PLAYERS) {
				e.getPlayer().teleport(e.getFrom());
			}
		}
        for (Match m : Matches.discussion) {
			if (m.getPlayers().contains(e.getPlayer())) {
				e.getPlayer().teleport(e.getFrom());
			}
		}
    }

    @EventHandler
    void teleport(PlayerTeleportEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR) && !e.getCause().equals(TeleportCause.PLUGIN)) {
            e.getPlayer().setSpectatorTarget(null);
            e.setCancelled(true);
        }
    }

}