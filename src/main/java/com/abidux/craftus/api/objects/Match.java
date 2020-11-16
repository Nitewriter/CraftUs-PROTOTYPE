package com.abidux.craftus.api.objects;

import com.abidux.craftus.Main;
import com.abidux.craftus.api.MatchState;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.PlayerType;
import com.abidux.craftus.enums.DisplayMessage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Match extends GameMap {

    private final ArrayList<Player> players = new ArrayList<>();
    private MatchState state = MatchState.WAITING_PLAYERS;
    public ArrayList<DeadPlayer> deadPlayers = new ArrayList<>();

    public Match(String name, Location location) {
        super(name, location);
    }

    public List<Player> getImpostors() {
        return players.stream().filter(player -> Main.profiles.get(player).getType().equals(PlayerType.IMPOSTOR)).collect(Collectors.toList());
    }

    public void join(Player player) {
        if (players.size() < 10 && state.equals(MatchState.WAITING_PLAYERS)) {
            System.out.println(player.getDisplayName() + " joined the match");
            players.add(player);
            player.getInventory().clear();
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400 * 20, 250));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 250));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400 * 20, 1));
            Main.profiles.get(player).setCurrentMatch(this);
            player.teleport(getSpawn());
            players.forEach(p -> Main.actionbar(p, DisplayMessage.JOINED.toString().replace("%player%", player.getName()).replace("%players%", String.valueOf(players.size()))));
        }
    }

    public void leave(Player player, boolean remover) {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        if (Matches.voted.contains(player)) {
            Matches.voted.remove(player);
        } else if (players.contains(player) && remover) {
            players.remove(player);
        }
        players.forEach(p -> Main.actionbar(p, DisplayMessage.LEFT.toString().replace("%player%", player.getName()).replace("%players%", String.valueOf(players.size()))));
    }

    public List<Player> getAliveCrewmates() {
        return players.stream().filter(player -> Main.profiles.get(player).getType().equals(PlayerType.CREWMATE)).collect(Collectors.toList());
    }

    public List<Player> getAliveImpostors() {
        return players.stream().filter(player -> Main.profiles.get(player).getType().equals(PlayerType.IMPOSTOR)).collect(Collectors.toList());
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @SuppressWarnings("deprecation")
    public void winner(String teamName) {
        setState(MatchState.RESTARTING);
        for (Player player : players) {
            player.sendTitle("�eWinner", teamName);
            leave(player, false);
            Matches.leave(Main.profiles.get(player));
        }
        players.clear();
        deadPlayers.forEach(dp -> {
            dp.getStands().forEach(Entity::remove);
            dp.getBody().remove();
        });
        deadPlayers.clear();
        Matches.playingMatches.remove(this);
        Matches.discussion.remove(this);
        Matches.discussionTime.remove(this);
        Matches.votingTime.remove(this);
        Matches.votes.remove(this);
        Matches.waitingMatches.add(this);
        setState(MatchState.WAITING_PLAYERS);
    }

}