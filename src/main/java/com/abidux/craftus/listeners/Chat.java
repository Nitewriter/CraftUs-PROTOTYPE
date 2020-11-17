package com.abidux.craftus.listeners;

import com.abidux.craftus.Main;
import com.abidux.craftus.api.GameMaps;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.PlayerType;
import com.abidux.craftus.api.objects.CraftUsProfile;
import com.abidux.craftus.api.objects.GameMap;
import com.abidux.craftus.enums.DisplayMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class Chat implements Listener {

    public static ArrayList<Player> players = new ArrayList<>();
    public static ArrayList<Player> editingName = new ArrayList<>();

    @EventHandler
    void chat(AsyncPlayerChatEvent e) {
        if (players.contains(e.getPlayer())) {
            e.setCancelled(true);
            players.remove(e.getPlayer());
            GameMaps.createMap(e.getMessage().trim(), e.getPlayer().getLocation());
            e.getPlayer().sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.MAP_CREATED);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }
        if (editingName.contains(e.getPlayer())) {
            e.setCancelled(true);
            GameMap gameMap = InvClick.editing.get(e.getPlayer());
            GameMaps.maps.remove(gameMap.getName().toLowerCase());
            gameMap.setName(e.getMessage().trim());
            GameMaps.maps.put(gameMap.getName().toLowerCase(), gameMap);
            InvClick.editing.remove(e.getPlayer());
            editingName.remove(e.getPlayer());
            e.getPlayer().sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.MODIFIED);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return;
        }
        CraftUsProfile profile = Main.profiles.get(e.getPlayer());
        if (profile.getCurrentMatch() != null) {
            e.setCancelled(true);
            if (!Matches.discussion.contains(profile.getCurrentMatch()) || (profile.getType().equals(PlayerType.DEAD_CREWMATE) || profile.getType().equals(PlayerType.DEAD_IMPOSTOR))) {
                e.getPlayer().sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.YOU_ARE_NOT_ALLOWED_TO_DO_THIS);
                return;
            }
            profile.getCurrentMatch().getPlayers().forEach(player -> player.sendMessage(profile.getColor().getChatColor() + "[" + profile.getColor().toString() + "] " + e.getPlayer().getName() + ": �f" + e.getMessage()));
        }
    }

}