package com.abidux.craftus.api;

import com.abidux.craftus.Main;
import com.abidux.craftus.api.objects.CraftUsProfile;
import com.abidux.craftus.api.objects.DeadPlayer;
import com.abidux.craftus.api.objects.GameMap;
import com.abidux.craftus.api.objects.Match;
import com.abidux.craftus.api.objects.tasks.CrewTask;
import com.abidux.craftus.api.objects.tasks.MeetingButton;
import com.abidux.craftus.commands.CraftUs;
import com.abidux.craftus.enums.DisplayMessage;
import com.abidux.craftus.enums.PlayerColor;
import com.abidux.craftus.listeners.InvClick;
import com.abidux.craftus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Matches {

    public static List<Match> playingMatches = new ArrayList<>();
    public static List<Match> waitingMatches = new ArrayList<>();
    public static HashMap<Match, Integer> discussionTime = new HashMap<>();
    public static HashMap<Match, Integer> votingTime = new HashMap<>();
    public static ArrayList<Player> voted = new ArrayList<>();
    public static HashMap<Match, HashMap<PlayerColor, Integer>> votes = new HashMap<>();
    public static List<Match> discussion = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();

    private static final ItemStack sword = new ItemBuilder().type(Material.IRON_SWORD).name("�cSword").build();

    public static ItemStack getImpostorSword() {
        return sword;
    }

    private static final ItemStack shears = new ItemBuilder().type(Material.SHEARS).name("�cPliers").build();

    public static ItemStack getPliers() {
        return shears;
    }

    public static void load() {
        for (GameMap gameMap : GameMaps.maps.values()) {
            String mapName = gameMap.getName();
            System.out.println("Creating new match for " + mapName);
            waitingMatches.add(new Match(mapName, gameMap.getSpawn()));
        }
    }

    public static Match getMatch() {
        if (waitingMatches.isEmpty()) {
            return null;
        }
        Match lastMatch = null;
        for (Match match : waitingMatches) {
            if (match.getPlayers().size() == 10) {
                continue;
            }
            if (lastMatch == null) {
                lastMatch = match;
                continue;
            }
            if (match.getPlayers().size() > lastMatch.getPlayers().size()) {
                lastMatch = match;
            }
        }
        return lastMatch;
    }

    private static final HashMap<Match, Integer> secondsToStart = new HashMap<>();

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!players.isEmpty()) {
                    Player player = players.get(0);
                    Match match = getMatch();
                    if (match != null) {
                        match.join(player);
                        players.remove(player);
                    }
                    for (int i = 0; i < players.size(); i++) {
                        Main.actionbar(players.get(i), "�e#" + (i + 1));
                    }
                }
                try {
                    for (Match match : votingTime.keySet()) {
                        if (votingTime.containsKey(match)) {
                            votingTime.put(match, votingTime.get(match) - 1);
                            match.getPlayers().forEach(player -> Main.actionbar(player, "�e" + votingTime.get(match) + "s"));
                            if (votingTime.get(match) == 0) {
                                votingTime.remove(match);
                                match.getPlayers().forEach(player -> {
                                    player.removePotionEffect(PotionEffectType.SLOW);
                                    player.removePotionEffect(PotionEffectType.JUMP);
                                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                                    player.closeInventory();
                                    voted.remove(player);
                                });

                                PlayerColor ejectedPlayer = null;
                                int highestCountSeen = 0;
                                System.out.println("Processing votes " + votes.get(match).size());
                                for (Map.Entry<PlayerColor, Integer> entry : votes.get(match).entrySet()) {
                                    PlayerColor color = entry.getKey();
                                    int voteCount = entry.getValue();

                                    if (voteCount > 2) {
                                        if (voteCount > highestCountSeen) {
                                            highestCountSeen = voteCount;
                                            ejectedPlayer = color;
                                            System.out.println("Player selected for ejection " + color);
                                        } else if (voteCount == highestCountSeen && ejectedPlayer != color) {
                                            ejectedPlayer = null;
                                            System.out.println("Player saved from ejection " + color);
                                        }
                                    }
                                }

                                votes.remove(match);
                                push(match, ejectedPlayer);

                                if (match.getAliveImpostors().size() == 0) {
                                    match.winner("�bCrewmate");
                                    return;
                                } else if (match.getAliveCrewmates().size() <= match.getAliveImpostors().size()) {
                                    match.winner("�cImpostor");
                                    return;
                                }
                            }
                        }
                    }
                } catch (ConcurrentModificationException | NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskTimer(Main.instance, 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (MeetingButton button : CraftUs.buttons.values()) {
                        if (button.coolDown > 0) {
                            button.coolDown--;
                        }
                    }
                    if (waitingMatches.isEmpty() && discussion.isEmpty() && votingTime.isEmpty()) {
                        return;
                    }
                    for (Match match : waitingMatches) {
                        if (match == null) {
                            continue;
                        }
                        if (match.getPlayers().size() >= 4) {
                            if (secondsToStart.containsKey(match)) {
                                secondsToStart.put(match, secondsToStart.get(match) - 1);
                            } else {
                                secondsToStart.put(match, 30);
                            }
                            int seconds = secondsToStart.get(match);
                            if (seconds <= 0) {
                                startMatch(match);
                            }
                            for (int i = 0; i < match.getPlayers().size(); i++) {
                                Main.actionbar(match.getPlayers().get(i), "�e" + seconds + "s");
                            }
                        } else {
                            secondsToStart.remove(match);
                        }
                    }
                    for (Match match : discussion) {
                        if (discussionTime.containsKey(match)) {
                            discussionTime.put(match, discussionTime.get(match) - 1);
                            for (int i = 0; i < match.getPlayers().size(); i++) {
                                Main.actionbar(match.getPlayers().get(i), "�e" + discussionTime.get(match) + "s");
                            }
                            if (discussionTime.get(match) == 0) {
                                discussion.remove(match);
                                discussionTime.remove(match);
                                match.getPlayers().forEach(player -> player.openInventory(getVoting(match)));
                                votingTime.put(match, 10);
                            }
                        }
                    }
                } catch (ConcurrentModificationException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(Main.instance, 0, 20);
    }

    @SuppressWarnings("deprecation")
    public static void leave(CraftUsProfile profile) {
        if (profile.getType() != null && profile.getType().getTeam() != null) {
            profile.getType().getTeam().removePlayer(profile.getPlayer());
        }
        profile.setType(null);
        profile.setColor(null);
        profile.getPlayer().getInventory().clear();
        profile.getPlayer().setGameMode(GameMode.ADVENTURE);
        if (Main.lobby != null) {
            profile.getPlayer().teleport(Main.lobby);
        }
        profile.getPlayer().getInventory().setHelmet(null);
        profile.getPlayer().getInventory().setChestplate(null);
        profile.getPlayer().getInventory().setLeggings(null);
        profile.getPlayer().getInventory().setBoots(null);
        profile.setCurrentMatch(null);
    }

    @SuppressWarnings("deprecation")
    public static void startMatch(Match match) {
        if (!match.getState().equals(MatchState.WAITING_PLAYERS)) {
            return;
        }
        int impostor = 1;
        if (match.getPlayers().size() < impostor) {
            impostor = match.getPlayers().size();
        }
        waitingMatches.remove(match);
        secondsToStart.remove(match);
        playingMatches.add(match);
        match.setState(MatchState.PLAYING);
        List<PlayerColor> availablePlayerColors = new ArrayList<>();
        Collections.addAll(availablePlayerColors, PlayerColor.values());

        for (Player player : match.getPlayers()) {
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            PlayerColor randomPlayerColor = availablePlayerColors.get(new Random().nextInt(availablePlayerColors.size()));
            CraftUsProfile profile = Main.profiles.get(player);
            profile.setCurrentMatch(match);
            profile.setColor(randomPlayerColor);
            profile.setType(PlayerType.CREWMATE);
            player.getInventory().clear();
            player.getInventory().setHelmet(randomPlayerColor.getHead());
            player.getInventory().setChestplate(randomPlayerColor.getChestPlate());
            player.getInventory().setLeggings(randomPlayerColor.getLeggings());
            player.getInventory().setBoots(randomPlayerColor.getBoots());
            availablePlayerColors.remove(randomPlayerColor);
        }
        Collections.shuffle(match.getPlayers());

        for (int i = 0; i < impostor; i++) {
            Main.profiles.get(match.getPlayers().get(i)).setType(PlayerType.IMPOSTOR);
        }

        for (Player player : match.getPlayers()) {
            CraftUsProfile profile = Main.profiles.get(player);
            if (profile.getType().equals(PlayerType.IMPOSTOR)) {
                continue;
            }
            player.sendMessage("�9Tasks:");
            for (int i = 0; i < 5; i++) {
                List<CrewTask> crewTaskList = new ArrayList<>(InvClick.tasks.values());
                Collections.shuffle(crewTaskList);
                profile.crewTasks.add(crewTaskList.get(0));
                String className = crewTaskList.get(0).getClass().getName();
                player.sendMessage("�9 * " + className.substring(className.lastIndexOf(".") + 1));
            }
        }

        for (Player player : match.getPlayers()) {
            CraftUsProfile profile = Main.profiles.get(player);
            player.getInventory().setHeldItemSlot(4);
            if (profile.getType().equals(PlayerType.CREWMATE)) {
                player.sendTitle(DisplayMessage.CREWMATE_TITLE.toString(), DisplayMessage.CREWMATE_SUBTITLE.toString());
                profile.getType().getTeam().addPlayer(player);
            } else {
                player.sendTitle(DisplayMessage.IMPOSTOR_TITLE.toString(), DisplayMessage.IMPOSTOR_SUBTITLE.toString());
                profile.getType().getTeam().addPlayer(player);
                player.getInventory().setItem(8, getImpostorSword());
            }
            player.getInventory().setItem(4, getPliers());
        }
        StringBuilder impostors = new StringBuilder();
        match.getImpostors().forEach(player -> impostors.append(player.getName()).append(","));
        match.getImpostors().forEach(player -> player.sendMessage("�cImpostors: " + impostors.substring(0, impostors.length() - 1)));
    }

    public static void killPlayer(CraftUsProfile profile) {
        Match match = profile.getCurrentMatch();
        if (match == null) {
            return;
        }

        match.deadPlayers.add(new DeadPlayer().build(profile));
        if (match.getAliveCrewmates().size() == match.getAliveImpostors().size()) {
            match.winner("�cImpostor");
        }
    }

    public static Inventory getVoting(Match match) {
        Inventory inventory = Bukkit.createInventory(null, 2 * 9, "�9Voting");
        List<PlayerColor> playerColors = match.getPlayers().stream().filter(player -> Main.profiles.get(player).getType().equals(PlayerType.IMPOSTOR) || Main.profiles.get(player).getType().equals(PlayerType.CREWMATE)).map(player -> Main.profiles.get(player).getColor()).collect(Collectors.toList());
        if (playerColors.contains(PlayerColor.BLACK)) {
            inventory.setItem(1, PlayerColor.BLACK.getHead());
        }
        if (playerColors.contains(PlayerColor.BLUE)) {
            inventory.setItem(2, PlayerColor.BLUE.getHead());
        }
        if (playerColors.contains(PlayerColor.BROWN)) {
            inventory.setItem(3, PlayerColor.BROWN.getHead());
        }
        if (playerColors.contains(PlayerColor.CYAN)) {
            inventory.setItem(4, PlayerColor.CYAN.getHead());
        }
        if (playerColors.contains(PlayerColor.GREEN)) {
            inventory.setItem(5, PlayerColor.GREEN.getHead());
        }
        if (playerColors.contains(PlayerColor.LIME)) {
            inventory.setItem(6, PlayerColor.LIME.getHead());
        }
        if (playerColors.contains(PlayerColor.ORANGE)) {
            inventory.setItem(7, PlayerColor.ORANGE.getHead());
        }
        if (playerColors.contains(PlayerColor.PINK)) {
            inventory.setItem(11, PlayerColor.PINK.getHead());
        }
        if (playerColors.contains(PlayerColor.PURPLE)) {
            inventory.setItem(12, PlayerColor.PURPLE.getHead());
        }
        if (playerColors.contains(PlayerColor.RED)) {
            inventory.setItem(13, PlayerColor.RED.getHead());
        }
        if (playerColors.contains(PlayerColor.WHITE)) {
            inventory.setItem(14, PlayerColor.WHITE.getHead());
        }
        if (playerColors.contains(PlayerColor.YELLOW)) {
            inventory.setItem(15, PlayerColor.YELLOW.getHead());
        }
        return inventory;
    }

    public static void vote(CraftUsProfile profile, PlayerColor playerColor) {
        if (profile.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            System.out.println("Ignoring spector vote by " + profile.getPlayer().getDisplayName());
            return;
        }

        HashMap<PlayerColor, Integer> voteCounts = votes.getOrDefault(profile.getCurrentMatch(), new HashMap<>());
        int countForPlayer = voteCounts.getOrDefault(playerColor, 0);
        voteCounts.put(playerColor, countForPlayer + 1);
        votes.put(profile.getCurrentMatch(), voteCounts);
        System.out.println(profile.getPlayer().getDisplayName() + " voted to eject " + playerColor);
    }

    public static void push(Match match, PlayerColor playerColor) {
        if (playerColor == null) {
            return;
        }
        for (Player player : match.getPlayers()) {
            CraftUsProfile profile = Main.profiles.get(player);
            if (profile.getColor().equals(playerColor)) {
                profile.setType(profile.getType().equals(PlayerType.CREWMATE) ? PlayerType.DEAD_CREWMATE : PlayerType.DEAD_IMPOSTOR);
                profile.getPlayer().setGameMode(GameMode.SPECTATOR);
                match.getPlayers().forEach(p -> Main.actionbar(p, playerColor.getChatColor() + playerColor + " was ejected."));
                break;
            }
        }
    }

}