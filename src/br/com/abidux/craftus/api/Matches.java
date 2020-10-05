package br.com.abidux.craftus.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.objects.CraftUsProfile;
import br.com.abidux.craftus.api.objects.DeadPlayer;
import br.com.abidux.craftus.api.objects.Map;
import br.com.abidux.craftus.api.objects.Match;
import br.com.abidux.craftus.api.objects.tasks.MeetingButton;
import br.com.abidux.craftus.api.objects.tasks.Task;
import br.com.abidux.craftus.commands.CraftUs;
import br.com.abidux.craftus.enums.Color;
import br.com.abidux.craftus.enums.Messages;
import br.com.abidux.craftus.listeners.InvClick;
import br.com.abidux.craftus.utils.ItemBuilder;

public class Matches {
	
	public static List<Match> playingMatches = new ArrayList<Match>();
	public static List<Match> waitingMatches = new ArrayList<Match>();
	public static HashMap<Match, Integer> discussionTime = new HashMap<Match, Integer>();
	public static HashMap<Match, Integer> votingTime = new HashMap<Match, Integer>();
	public static ArrayList<Player> voted = new ArrayList<Player>();
	public static HashMap<Match, HashMap<Color, Integer>> votes = new HashMap<Match, HashMap<Color, Integer>>();
	public static List<Match> discussion = new ArrayList<Match>();
	public static List<Player> players = new ArrayList<Player>();
	
	private static ItemStack sword = new ItemBuilder().type(Material.IRON_SWORD).name("§cSword").build();
	public static ItemStack getImpostorSword() {
		return sword;
	}
	
	private static ItemStack shears = new ItemBuilder().type(Material.SHEARS).name("§cPliers").build();
	public static ItemStack getPliers() {
		return shears;
	}
	
	public static void load() {
		for (Map map : Maps.maps.values()) waitingMatches.add(new Match(map.getName(), map.getSpawn()));
	}
	
	public static Match getMatch() {
		if (waitingMatches.isEmpty()) return null;
		Match lastMatch = null;
		for (Match match : waitingMatches) {
			if (match.getPlayers().size() == 10) continue;
			if (lastMatch == null) {
				lastMatch = match;
				continue;
			}
			if (match.getPlayers().size() > lastMatch.getPlayers().size()) lastMatch = match;
		}
		return lastMatch;
	}
	
	private static HashMap<Match, Integer> secondsToStart = new HashMap<>();
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
					for (int i = 0; i < players.size(); i++) Main.actionbar(players.get(i), "§e#"+(i+1));
				}
				try {
					for (Match match : votingTime.keySet()) {
						if (votingTime.containsKey(match)) {
							votingTime.put(match, votingTime.get(match)-1);
							match.getPlayers().stream().forEach(player -> Main.actionbar(player, "§e"+votingTime.get(match)+"s"));
							if (votingTime.get(match) == 0) {
								votingTime.remove(match);
								match.getPlayers().stream().forEach(player -> {
									player.removePotionEffect(PotionEffectType.SLOW);
									player.removePotionEffect(PotionEffectType.JUMP);
									player.removePotionEffect(PotionEffectType.INVISIBILITY);
									player.closeInventory();
									voted.remove(player);
									votes.remove(match);
								});
								Color color = null;
								int voteCount = 0;
								for (Entry<Color, Integer> entry : votes.get(match).entrySet()) {
									if (entry.getValue() > voteCount) {
										voteCount = entry.getValue();
										color = entry.getKey();
									}else if (entry.getValue() == voteCount) {
										color = null;
										voteCount = 0;
										break;
									}
								}
								push(match, color);
								if (match.getAliveImpostors().size() == 0) {
									match.winner("§bCrewmate");
									return;
								}else if (match.getAliveCrewmates().size() == 2) {
									match.winner("§cImpostor");
									return;
								}
							}
						}
					}
				}catch (ConcurrentModificationException | NullPointerException ex) {
					return;
				}
			}
		}.runTaskTimer(Main.instance, 0, 20);
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					for (MeetingButton button : CraftUs.buttons.values())
						if (button.cooldown > 0) {
							button.cooldown--;
						}
					if (waitingMatches.isEmpty() && discussion.isEmpty() && votingTime.isEmpty()) return;
					for (Match match : waitingMatches) {
						if (match == null) continue;
						if (match.getPlayers().size() >= 7) {
							if (secondsToStart.containsKey(match)) secondsToStart.put(match, secondsToStart.get(match)-1);
							else secondsToStart.put(match, 60);
							int seconds = secondsToStart.get(match);
							if (seconds <= 0) startMatch(match);
							for (int i = 0; i < match.getPlayers().size(); i++) Main.actionbar(match.getPlayers().get(i), "§e"+seconds+"s");
						}else if (secondsToStart.containsKey(match)) secondsToStart.remove(match);
					}
					for (Match match : discussion) {
						if (discussionTime.containsKey(match)) {
							discussionTime.put(match, discussionTime.get(match)-1);
							for (int i = 0; i < match.getPlayers().size(); i++) Main.actionbar(match.getPlayers().get(i), "§e"+discussionTime.get(match)+"s");
							if (discussionTime.get(match) == 0) {
								discussion.remove(match);
								discussionTime.remove(match);
								match.getPlayers().stream().forEach(player -> player.openInventory(getVoting(match)));
								votingTime.put(match, 15);
							}
						}
					}
				}catch (ConcurrentModificationException ex) {
					return;
				}
			}
		}.runTaskTimerAsynchronously(Main.instance, 0, 20);
	}
	
	@SuppressWarnings("deprecation")
	public static void leave(CraftUsProfile profile) {
		if (profile.getType() != null && profile.getType().getTeam() != null) profile.getType().getTeam().removePlayer(profile.getPlayer());
		profile.setType(null);
		profile.setColor(null);
		profile.getPlayer().getInventory().clear();
		if (Main.lobby != null) profile.getPlayer().teleport(Main.lobby);
		profile.getPlayer().getInventory().setHelmet(null);
		profile.getPlayer().getInventory().setChestplate(null);
		profile.getPlayer().getInventory().setLeggings(null);
		profile.getPlayer().getInventory().setBoots(null);
		profile.setCurrentMatch(null);
	}
	
	@SuppressWarnings("deprecation")
	public static void startMatch(Match match) {
		if (!match.getState().equals(MatchState.WAITING_PLAYERS)) return;
		int impostor = 2;
		if (match.getPlayers().size() < impostor) impostor = match.getPlayers().size();
		waitingMatches.remove(match);
		secondsToStart.remove(match);
		playingMatches.add(match);
		match.setState(MatchState.PLAYING);
		List<Color> availableColors = new ArrayList<Color>();
		for (int i = 0; i < Color.values().length; i++) availableColors.add(Color.values()[i]);
		for (Player player : match.getPlayers()) {
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.JUMP);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			Color randomColor = availableColors.get(new Random().nextInt(availableColors.size()));
			CraftUsProfile profile = Main.profiles.get(player);
			profile.setCurrentMatch(match);
			profile.setColor(randomColor);
			profile.setType(PlayerType.CREWMATE);
			player.getInventory().clear();
			player.getInventory().setHelmet(randomColor.getHead());
			player.getInventory().setChestplate(randomColor.getChestplate());
			player.getInventory().setLeggings(randomColor.getLeggings());
			player.getInventory().setBoots(randomColor.getBoots());
			availableColors.remove(randomColor);
		}
		Collections.shuffle(match.getPlayers());
		for (int i = 0; i < impostor; i++)
			Main.profiles.get(match.getPlayers().get(i)).setType(PlayerType.IMPOSTOR);
		for (Player player : match.getPlayers()) {
			CraftUsProfile profile = Main.profiles.get(player);
			if (profile.getType().equals(PlayerType.IMPOSTOR)) continue;
			player.sendMessage("§9Tasks:");
			for (int i = 0; i < 5; i++) {
				List<Task> taskList = InvClick.tasks.values().stream().collect(Collectors.toList());
				Collections.shuffle(taskList);
				profile.tasks.add(taskList.get(0));
				String className = taskList.get(0).getClass().getName();
				player.sendMessage("§9 * " + className.substring(className.lastIndexOf(".")+1, className.length()));
			}
		}
		for (Player player : match.getPlayers()) {
			CraftUsProfile profile = Main.profiles.get(player);
			player.getInventory().setHeldItemSlot(4);
			if (profile.getType().equals(PlayerType.CREWMATE)) {
				player.sendTitle(Messages.CREWMATE_TITLE.toString(), Messages.CREWMATE_SUBTITLE.toString());
				profile.getType().getTeam().addPlayer(player);
			}else {
				player.sendTitle(Messages.IMPOSTOR_TITLE.toString(), Messages.IMPOSTOR_SUBTITLE.toString());
				profile.getType().getTeam().addPlayer(player);
				player.getInventory().setItem(8, getImpostorSword());
			}
			player.getInventory().setItem(4, getPliers());
		}
		StringBuilder impostors = new StringBuilder();
		match.getImpostors().stream().forEach(player -> impostors.append(player.getName()+","));
		match.getImpostors().stream().forEach(player -> player.sendMessage("§cImpostors: "+impostors.substring(0, impostors.length()-1)));
	}
	
	public static void killPlayer(CraftUsProfile profile) {
		profile.getCurrentMatch().deadPlayers.add(new DeadPlayer().build(profile));
		if (profile.getCurrentMatch().getAliveCrewmates().size() == 2) {
			profile.getCurrentMatch().winner("§cImpostor");
			return;
		}
	}
	
	public static Inventory getVoting(Match match) {
		Inventory inventory = Bukkit.createInventory(null, 2*9, "§9Voting");
		List<Color> colors = match.getPlayers().stream().filter(player -> Main.profiles.get(player).getType().equals(PlayerType.IMPOSTOR) || Main.profiles.get(player).getType().equals(PlayerType.CREWMATE)).map(player -> Main.profiles.get(player).getColor()).collect(Collectors.toList());
		if (colors.contains(Color.BLACK)) inventory.setItem(1, Color.BLACK.getHead());
		if (colors.contains(Color.BLUE)) inventory.setItem(2, Color.BLUE.getHead());
		if (colors.contains(Color.BROWN)) inventory.setItem(3, Color.BROWN.getHead());
		if (colors.contains(Color.CYAN)) inventory.setItem(4, Color.CYAN.getHead());
		if (colors.contains(Color.GREEN)) inventory.setItem(5, Color.GREEN.getHead());
		if (colors.contains(Color.LIME)) inventory.setItem(6, Color.LIME.getHead());
		if (colors.contains(Color.ORANGE)) inventory.setItem(7, Color.ORANGE.getHead());
		if (colors.contains(Color.PINK)) inventory.setItem(11, Color.PINK.getHead());
		if (colors.contains(Color.PURPLE)) inventory.setItem(12, Color.PURPLE.getHead());
		if (colors.contains(Color.RED)) inventory.setItem(13, Color.RED.getHead());
		if (colors.contains(Color.WHITE)) inventory.setItem(14, Color.WHITE.getHead());
		if (colors.contains(Color.YELLOW)) inventory.setItem(15, Color.YELLOW.getHead());
		return inventory;
	}
	
	public static void vote(CraftUsProfile profile, Color color) {
		if (profile.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) return;
		HashMap<Color, Integer> voteCount = new HashMap<Color, Integer>();
		voteCount.put(color, voteCount.containsKey(color) ? voteCount.get(color)+1 : 1);
		votes.put(profile.getCurrentMatch(), voteCount);
	}
	
	public static void push(Match match, Color color) {
		if (color == null) return;
		for (Player player : match.getPlayers()) {
			CraftUsProfile profile = Main.profiles.get(player);
			if (profile.getColor().equals(color)) {
				profile.setType(profile.getType().equals(PlayerType.CREWMATE) ? PlayerType.DEAD_CREWMATE : PlayerType.DEAD_IMPOSTOR);
				profile.getPlayer().setGameMode(GameMode.SPECTATOR);
				match.getPlayers().forEach(p -> Main.actionbar(p, color.getChatColor()+color+" was ejected."));
				break;
			}
		}
	}
	
}