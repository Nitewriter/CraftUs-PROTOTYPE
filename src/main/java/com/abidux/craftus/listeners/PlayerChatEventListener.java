//package com.abidux.craftus.listeners;
//
//import com.abidux.craftus.CraftUs;
//import com.abidux.craftus.api.Matches;
//import com.abidux.craftus.controllers.GameMaps;
//import com.abidux.craftus.enums.DisplayMessage;
//import com.abidux.craftus.game.CraftUsProfile;
//import com.abidux.craftus.game.GameMap;
//import com.abidux.craftus.game.PlayerType;
//import org.bukkit.Sound;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.AsyncPlayerChatEvent;
//
//import java.util.ArrayList;
//
//public class PlayerChatEventListener implements Listener {
//
//    public static ArrayList<Player> players = new ArrayList<>();
//    public static ArrayList<Player> editingName = new ArrayList<>();
//
//    @EventHandler
//    void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
//        if (players.contains(event.getPlayer())) {
//            event.setCancelled(true);
//            players.remove(event.getPlayer());
//            GameMaps.createMap(event.getMessage().trim(), event.getPlayer().getLocation());
//            event.getPlayer().sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.MAP_CREATED);
//            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//            return;
//        }
//        if (editingName.contains(event.getPlayer())) {
//            event.setCancelled(true);
//            GameMap gameMap = InvClick.editing.get(event.getPlayer());
//            GameMaps.maps.remove(gameMap.getName().toLowerCase());
//            gameMap.setName(event.getMessage().trim());
//            GameMaps.maps.put(gameMap.getName().toLowerCase(), gameMap);
//            InvClick.editing.remove(event.getPlayer());
//            editingName.remove(event.getPlayer());
//            event.getPlayer().sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.MODIFIED);
//            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//            return;
//        }
//        CraftUsProfile profile = CraftUs.profiles.get(event.getPlayer());
//        if (profile.getCurrentMatch() != null) {
//            event.setCancelled(true);
//            if (!Matches.discussion.contains(profile.getCurrentMatch()) || (profile.getType().equals(
//                    PlayerType.DEAD_CREWMATE) || profile.getType().equals(PlayerType.DEAD_IMPOSTOR))) {
//                event.getPlayer().sendMessage(
//                        DisplayMessage.PREFIX.toString() + DisplayMessage.YOU_ARE_NOT_ALLOWED_TO_DO_THIS);
//                return;
//            }
//            profile.getCurrentMatch().getPlayers().forEach(player -> player.sendMessage(
//                    profile.getColor().getChatColor() + "[" + profile.getColor().toString() + "] " + event
//                    .getPlayer().getName() + ": §f" + event.getMessage()));
//        }
//    }
//
//}