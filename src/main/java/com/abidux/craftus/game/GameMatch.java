package com.abidux.craftus.game;

import com.abidux.craftus.enums.DisplayMessage;
import com.abidux.craftus.enums.MatchState;
import com.abidux.craftus.enums.PlayerColor;
import com.abidux.craftus.events.GameMatchPlayerAttackedEvent;
import com.abidux.craftus.events.MatchUpdateEvent;
import com.abidux.craftus.events.PlayerTeleportedToLobbyEvent;
import com.abidux.craftus.game.items.DeadPlayer;
import com.abidux.craftus.game.tasks.CrewTask;
import com.abidux.craftus.gui.ActionBar;
import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.models.game.Crewmate;
import com.abidux.craftus.models.game.GamePlayer;
import com.abidux.craftus.models.game.Impostor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class GameMatch extends GameMap implements Listener {

    private static final Integer minimumPlayers = 1;
    private static final Integer maximumPlayers = 10;

    private final CoolDown matchStartCoolDown = new CoolDown(10);
    private final CoolDown meetingButtonCoolDown = new CoolDown(30);
    private final CoolDown discussionCoolDown = new CoolDown(60);
    private final CoolDown votingCoolDown = new CoolDown(30);
    private final CoolDown restartCoolDown = new CoolDown(30);

    private final HashMap<UUID, CraftUsPlayer> players = new HashMap<>();
    private final HashMap<UUID, Crewmate> crewmates = new HashMap<>();
    private final HashMap<UUID, Impostor> impostors = new HashMap<>();

    public List<DeadPlayer> deadPlayers = new ArrayList<>();
    private MatchState state = MatchState.WAITING_PLAYERS;

    public GameMatch(String name, Location location) {
        super(name, location);
    }

    @EventHandler
    public void onMatchUpdate(MatchUpdateEvent event) {
        updateCoolDowns();

        if (state.equals(MatchState.WAITING_PLAYERS)) {
            for (CraftUsPlayer player : players.values()) {
                MatchMessenger.sendPlayerStartGameCountdown(player.getPlayer(), matchStartCoolDown);
            }

            if (matchStartCoolDown.isCleared()) {
                start();
            }
        }
    }

    public void updateCoolDowns() {
        switch (state) {
            case WAITING_PLAYERS:
                if (players.size() >= minimumPlayers) {
                    matchStartCoolDown.update();
                } else {
                    matchStartCoolDown.reset();
                }
                break;
            case PLAYING:
                meetingButtonCoolDown.update();
                impostors.values().forEach(Impostor::updateCoolDowns);
                break;
            case DISCUSSING:
                discussionCoolDown.update();
                break;
            case VOTING:
                votingCoolDown.update();
                break;
            case RESTARTING:
                restartCoolDown.update();
                break;
        }
    }

    public void start() {
        setState(MatchState.PLAYING);

        Random random = new Random();
        List<CrewTask> availableTasks = new ArrayList<>();
        List<PlayerColor> availablePlayerColors = new ArrayList<>();
        Collections.addAll(availablePlayerColors, PlayerColor.values());
        CraftUsPlayer chosenImpostor = (CraftUsPlayer) players.values().toArray()[random.nextInt(players.size())];

        for (CraftUsPlayer craftUsPlayer : players.values()) {
            PlayerColor playerColor = availablePlayerColors.remove(random.nextInt(availablePlayerColors.size()));

            GamePlayer gamePlayer;
            Player player = craftUsPlayer.getPlayer();

            if (craftUsPlayer.equals(chosenImpostor)) {
                Impostor impostor = new Impostor(player, playerColor);
                impostor.getKillCoolDown().reset();
                impostor.giveSword();
                impostors.put(player.getUniqueId(), impostor);
                gamePlayer = impostor;
            } else {
                Crewmate crewmate = new Crewmate(player, playerColor);
                CrewTask task = availableTasks.get(random.nextInt(availableTasks.size()));
                crewmate.getCrewTasks().add(task);
                crewmates.put(player.getUniqueId(), crewmate);
                gamePlayer = crewmate;
            }

            setEquipment(gamePlayer);
            gamePlayer.givePliers();

            removePotionEffects(player);
            MatchMessenger.sendPlayerRole(gamePlayer);
            MatchMessenger.sendPlayerTasks(gamePlayer);
        }
    }

    private void setEquipment(GamePlayer player) {
        Player bukkitPlayer = player.getPlayer();
        PlayerColor playerColor = player.getColor();

        bukkitPlayer.getInventory().setHelmet(playerColor.getHead());
        bukkitPlayer.getInventory().setChestplate(playerColor.getChestPlate());
        bukkitPlayer.getInventory().setLeggings(playerColor.getLeggings());
        bukkitPlayer.getInventory().setBoots(playerColor.getBoots());
    }

    private void removePotionEffects(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    @EventHandler
    public void onGameMatchPlayerAttackedEvent(GameMatchPlayerAttackedEvent event) {
        if (event.getGameMatch() != this) {
            return;
        }

        if (state != MatchState.PLAYING || impostors.isEmpty() || crewmates.isEmpty()) {
            return;
        }

        Impostor impostor = impostors.get(event.getAttacker().getPlayer().getUniqueId());
        Crewmate crewmate = crewmates.get(event.getVictim().getPlayer().getUniqueId());
        if (impostor == null || crewmate == null) {
            return;
        }

        CoolDown killCoolDown = impostor.getKillCoolDown();
        if (!killCoolDown.isCleared()) {
            MatchMessenger.sendPlayerCoolDownMessage(impostor.getPlayer(), killCoolDown);
        } else {
            killCoolDown.reset();
            crewmate.setIsGhost(true);

            // TODO: Place dead body
        }
    }

    @EventHandler
    public void onPlayerTeleportedToLobby(PlayerTeleportedToLobbyEvent event) {
        CraftUsPlayer player = event.getCraftUsPlayer();
        if (!players.containsKey(player.getPlayer().getUniqueId())) {
            return;
        }

        handlePlayerLeave(player);
    }

    public void close() {
        HandlerList.unregisterAll(this);
        winner("None");
    }

    public boolean join(CraftUsPlayer craftUsPlayer) {
        if (!state.equals(MatchState.WAITING_PLAYERS)) {
            return false;
        }

        if (players.size() == maximumPlayers) {
            return false;
        }

        Player player = craftUsPlayer.getPlayer();
        System.out.println(player.getDisplayName() + " joined the match");
        players.put(craftUsPlayer.getPlayer().getUniqueId(), craftUsPlayer);
        craftUsPlayer.setCurrentMatch(this);
        player.getInventory().clear();
        addPotionEffects(player);
        player.teleport(getSpawn());
        players.values().forEach(p -> {
            String message = DisplayMessage.JOINED.toString()
                    .replace("%player%", player.getName())
                    .replace("%players%", String.valueOf(players.size()));
            ActionBar.sendMessage(p.getPlayer(), message);
        });

        return true;
    }

    private void addPotionEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400 * 20, 250));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 250));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400 * 20, 1));
    }

    private void handlePlayerLeave(CraftUsPlayer craftUsPlayer) {
        Player player = craftUsPlayer.getPlayer();
        removePotionEffects(player);
        removeEquipment(player);

        players.remove(player.getUniqueId());
//        if (Matches.voted.contains(player)) {
//            Matches.voted.remove(player);
//        } else if (players.contains(player) && remover) {
//        }
        if (players.isEmpty()) {
            winner("NONE");
            return;
        }


        players.values().forEach(p -> {
            String message = DisplayMessage.LEFT.toString()
                    .replace("%player%", player.getName())
                    .replace("%players%", String.valueOf(players.size()));
            ActionBar.sendMessage(p.getPlayer(), message);
        });
    }

    private void removeEquipment(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;

        switch (state) {
            case WAITING_PLAYERS:
                matchStartCoolDown.reset();
                break;
            case PLAYING:
                meetingButtonCoolDown.reset();
                break;
            case RESTARTING:
                matchStartCoolDown.clear();
                meetingButtonCoolDown.clear();
                discussionCoolDown.clear();
                votingCoolDown.clear();
                break;
        }
    }

    public void winner(String teamName) {
        setState(MatchState.RESTARTING);
        for (CraftUsPlayer player : players.values()) {
            MatchMessenger.sendPlayerWinner(player.getPlayer(), teamName);
            handlePlayerLeave(player);
        }
        players.clear();
        deadPlayers.forEach(dp -> {
            dp.getStands().forEach(Entity::remove);
            dp.getBody().remove();
        });
        deadPlayers.clear();
//        Matches.playingMatches.remove(this);
//        Matches.discussion.remove(this);
//        Matches.discussionTime.remove(this);
//        Matches.votingTime.remove(this);
//        Matches.votes.remove(this);
//        Matches.waitingMatches.add(this);
        setState(MatchState.WAITING_PLAYERS);
    }

}