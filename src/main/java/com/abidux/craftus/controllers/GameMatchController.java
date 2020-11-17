package com.abidux.craftus.controllers;

import com.abidux.craftus.CraftUs;
import com.abidux.craftus.enums.MatchState;
import com.abidux.craftus.events.FindMatchForPlayerEvent;
import com.abidux.craftus.events.GameMapsLoadedEvent;
import com.abidux.craftus.game.GameMap;
import com.abidux.craftus.game.GameMatch;
import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.runnables.MatchUpdateRunnable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class GameMatchController implements Listener {

    private final CraftUs plugin;
    private final HashMap<String, GameMatch> matches = new HashMap<>();

    private MatchUpdateRunnable updateRunnable;

    @Inject
    public GameMatchController(CraftUs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFindMatchForPlayer(FindMatchForPlayerEvent event) {
        System.out.println("MatchMakerController.onFindMatchForPlayer");
        findMatch(event.getPlayer());
    }

    private void findMatch(CraftUsPlayer player) {
        Optional<GameMatch> existingMatch = getWaitingMatches()
                .stream()
                .findFirst();

        if (existingMatch.isPresent()) {
            GameMatch gameMatch = existingMatch.get();
            player.setCurrentMatch(gameMatch);
            if (!gameMatch.join(player)) {
                player.getPlayer().sendMessage("Sorry, we are unable to join you to '" + gameMatch.getName() + "'");
            }
        } else {
            player.getPlayer().sendMessage("No available matches to join. " + matches.size());
        }
    }

    private List<GameMatch> getWaitingMatches() {
        return matches.values().stream()
                .filter((m) -> m.getState() == MatchState.WAITING_PLAYERS)
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onGameMapsLoaded(GameMapsLoadedEvent event) {
        System.out.println("MatchMakerController.onGameMapsLoaded");

        buildMatches(event.getGameMaps());
    }

    private void buildMatches(List<GameMap> gameMaps) {
        if (!matches.isEmpty()) {
            closeMatches();
        }

        for (GameMap gameMap : gameMaps) {
            String mapName = gameMap.getName();
            if (matches.containsKey(mapName)) {
                continue;
            }

            System.out.println("Building match for map '" + mapName + "'");
            GameMatch gameMatch = new GameMatch(mapName, gameMap.getSpawn());
            matches.put(mapName, gameMatch);
            Bukkit.getPluginManager().registerEvents(gameMatch, plugin);
        }

        System.out.println("Built " + matches.size() + "matches");
        startUpdating();
    }

    private void closeMatches() {
        stopUpdating();

        for (GameMatch gameMatch : matches.values()) {
            System.out.println("Closing match for map '" + gameMatch.getName() + "'");
            gameMatch.close();
        }

        System.out.println("Clearing matches");
        matches.clear();
    }

    public void startUpdating() {
        if (updateRunnable != null) {
            return;
        }

        updateRunnable = new MatchUpdateRunnable();
        updateRunnable.runTaskTimer(plugin, 10, 20);
    }

    public void stopUpdating() {
        if (updateRunnable == null) {
            return;
        }

        updateRunnable.cancel();
        updateRunnable = null;
    }

}
