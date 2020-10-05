package br.com.abidux.craftus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import br.com.abidux.craftus.api.Maps;
import br.com.abidux.craftus.api.Matches;
import br.com.abidux.craftus.api.PlayerType;
import br.com.abidux.craftus.api.objects.CraftUsProfile;
import br.com.abidux.craftus.api.objects.tasks.CommsTask;
import br.com.abidux.craftus.api.objects.tasks.ElectricTask;
import br.com.abidux.craftus.api.objects.tasks.LevelTask;
import br.com.abidux.craftus.api.objects.tasks.MazeTask;
import br.com.abidux.craftus.api.objects.tasks.MeetingButton;
import br.com.abidux.craftus.api.objects.tasks.MeteorTask;
import br.com.abidux.craftus.api.objects.tasks.Task;
import br.com.abidux.craftus.api.objects.tasks.WiresTask;
import br.com.abidux.craftus.commands.CraftUs;
import br.com.abidux.craftus.commands.Lobby;
import br.com.abidux.craftus.commands.Search;
import br.com.abidux.craftus.enums.Color;
import br.com.abidux.craftus.enums.Heads;
import br.com.abidux.craftus.enums.HeadsItem;
import br.com.abidux.craftus.enums.Messages;
import br.com.abidux.craftus.listeners.Chat;
import br.com.abidux.craftus.listeners.CloseInv;
import br.com.abidux.craftus.listeners.DropItem;
import br.com.abidux.craftus.listeners.InvClick;
import br.com.abidux.craftus.listeners.Join;
import br.com.abidux.craftus.listeners.Move;
import br.com.abidux.craftus.listeners.PlayerInteract;
import br.com.abidux.craftus.listeners.QuitEvent;
import br.com.abidux.craftus.utils.SkullBuilder;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Main extends JavaPlugin {
	
	public static Main instance;
	public static Location lobby;
	public static HashMap<Player, CraftUsProfile> profiles = new HashMap<>();
	
	public File st = new File(getDataFolder(), "stands.yml");
	public FileConfiguration stands = YamlConfiguration.loadConfiguration(st);
	
	public static void saveStands() {
		try {
			instance.stands.save(instance.st);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onEnable() {
		instance = this;
		if (!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
		if (getConfig().isSet("lobby")) lobby = (Location) getConfig().get("lobby");
		load();
		loadHelmets();
		loadTeams();
		new Maps();
		Maps.createFile();
		Maps.load();
		Matches.load();
		if (st.exists()) loadTasks();
		Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
		Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);
		Bukkit.getPluginManager().registerEvents(new CloseInv(), this);
		Bukkit.getPluginManager().registerEvents(new InvClick(), this);
		Bukkit.getPluginManager().registerEvents(new DropItem(), this);
		Bukkit.getPluginManager().registerEvents(new Chat(), this);
		Bukkit.getPluginManager().registerEvents(new Join(), this);
		Bukkit.getPluginManager().registerEvents(new Move(), this);
		getCommand("craftus").setExecutor(new CraftUs());
		getCommand("search").setExecutor(new Search());
		getCommand("lobby").setExecutor(new Lobby());
		Matches.run();
	}
	
	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			CraftUsProfile profile = profiles.get(player);
			if (profile.getCurrentMatch() != null) profile.getCurrentMatch().leave(player, true);
			Matches.leave(profile);
		}
		Maps.save();
		int i = 0;
		for (Task task : InvClick.tasks.values()) {
			task.getStand().remove();
			task.save(String.valueOf(i), task.getClass().getName());
			i++;
		}
		for (MeetingButton button : CraftUs.buttons.values()) {
			button.getStand().remove();
			button.save(String.valueOf(i), button.getClass().getName());
			i++;
		}
		saveStands();
	}
	
	private void loadTasks() {
		for (String stand : stands.getConfigurationSection("").getKeys(false)) {
			String type = stands.getString(stand+".type");
			Location location = new Location(Bukkit.getWorld(stands.getString(stand+".world")), stands.getDouble(stand+".x"), stands.getDouble(stand+".y"), stands.getDouble(stand+".z"));
			location.setPitch((float)stands.getDouble(stand+".pitch"));
			location.setYaw((float)stands.getDouble(stand+".yaw"));
			if (type.equals("br.com.abidux.craftus.api.objects.tasks.CommsTask")) {
				CommsTask task = new CommsTask(location);
				InvClick.tasks.put(task.getStand(), task);
			}else if (type.equals("br.com.abidux.craftus.api.objects.tasks.ElectricTask")) {
				ElectricTask task = new ElectricTask(location);
				InvClick.tasks.put(task.getStand(), task);
			}else if (type.equals("br.com.abidux.craftus.api.objects.tasks.LevelTask")) {
				LevelTask task = new LevelTask(location);
				InvClick.tasks.put(task.getStand(), task);
			}else if (type.equals("br.com.abidux.craftus.api.objects.tasks.MazeTask")) {
				Inventory inventory = Bukkit.createInventory(null, stands.getInt(stand+".invsize"), "§6Click Task");
				for (int i : stands.getIntegerList(stand+".inventory")) inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
				MazeTask task = new MazeTask(location, inventory);
				InvClick.tasks.put(task.getStand(), task);
			}else if (type.equals("br.com.abidux.craftus.api.objects.tasks.MeteorTask")) {
				MeteorTask task = new MeteorTask(location);
				InvClick.tasks.put(task.getStand(), task);
			}else if (type.equals("br.com.abidux.craftus.api.objects.tasks.WiresTask")) {
				WiresTask task = new WiresTask(location);
				InvClick.tasks.put(task.getStand(), task);
			}else if (type.equals("br.com.abidux.craftus.api.objects.tasks.MeetingButton")) {
				MeetingButton button = new MeetingButton(location);
				CraftUs.buttons.put(button.getStand(), button);
			}
		}
	}
	
	public void load() {
		Messages.PREFIX.setContent(getMessage("prefix")+" ");
		Messages.YOU_ARE_NOT_ALLOWED_TO_DO_THIS.setContent(getMessage("messages.you_are_not_allowed_to_do_this"));
		Messages.CREATE.setContent(getMessage("messages.create"));
		Messages.CLICK_TO_CREATE.setContent(getMessage("messages.click_to_create"));
		Messages.SEARCH.setContent(getMessage("messages.search"));
		Messages.CLICK_TO_SEARCH.setContent(getMessage("messages.click_to_search"));
		Messages.SEND_NAME.setContent(getMessage("messages.send_name"));
		Messages.MAP_CREATED.setContent(getMessage("messages.map_created"));
		Messages.CLICK_TO_EDIT.setContent(getMessage("messages.click_to_edit"));
		Messages.PREVIOUS.setContent(getMessage("messages.previous"));
		Messages.NEXT.setContent(getMessage("messages.next"));
		Messages.EDIT_MAP.setContent(getMessage("messages.edit_map"));
		Messages.MODIFIED.setContent(getMessage("messages.modified"));
		Messages.JOINED.setContent(getMessage("messages.joined"));
		Messages.LEFT.setContent(getMessage("messages.left"));
		Messages.IMPOSTOR_TITLE.setContent(getMessage("messages.impostor_title"));
		Messages.IMPOSTOR_SUBTITLE.setContent(getMessage("messages.impostor_subtitle"));
		Messages.CREWMATE_TITLE.setContent(getMessage("messages.crewmate_title"));
		Messages.CREWMATE_SUBTITLE.setContent(getMessage("messages.crewmate_subtitle"));
		HeadsItem.PLUS.setItem(new SkullBuilder(Heads.PLUS.getUrl()).name(Messages.CREATE.toString()).lore(Messages.CLICK_TO_CREATE.toString()).build());
		HeadsItem.BOOK.setItem(new SkullBuilder(Heads.BOOK.getUrl()).name(Messages.SEARCH.toString()).lore(Messages.CLICK_TO_SEARCH.toString()).build());
		HeadsItem.EARTH.setItem(new SkullBuilder(Heads.EARTH.getUrl()).name("?").lore(Messages.CLICK_TO_EDIT.toString()).build());
		HeadsItem.BED.setItem(new SkullBuilder(Heads.BED.getUrl()).name("§aSetSpawn").build());
		HeadsItem.X.setItem(new SkullBuilder(Heads.X.getUrl()).name("§cDelete").build());
		HeadsItem.RADIO.setItem(new SkullBuilder(Heads.RADIO.getUrl()).name("§7Comms").build());
		HeadsItem.ELECTRIC.setItem(new SkullBuilder(Heads.ELECTRIC.getUrl()).name("§7Electric").build());
		HeadsItem.MAZE.setItem(new SkullBuilder(Heads.MAZE.getUrl()).name("§7Maze").build());
		HeadsItem.METEORITE.setItem(new SkullBuilder(Heads.METEORITE.getUrl()).name("§7Meteor").build());
		HeadsItem.WAVE.setItem(new SkullBuilder(Heads.WAVE.getUrl()).name("§7Fix Energy Level").build());
		HeadsItem.MYSTERY_BOX.setItem(new SkullBuilder(Heads.MYSTERY_BOX.getUrl()).name("§7Connect Wires").build());
		HeadsItem.MEETING_BUTTON.setItem(new SkullBuilder(Heads.MEETING_BUTTON.getUrl()).name("§eMeeting Button").build());
	}
	
	private void loadHelmets() {
		Color.BLACK.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRlMzJjNzY0ZjNmMjhkMjcyOWIxNjFlZTM1MGZhMDQzZWI3ZDI5MjExMzIzYWVmMzRjMzZjOTM4ZDEwOGExMCJ9fX0=").name(Color.BLACK.getChatColor()+"Black").build());
		Color.BLUE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZjODU1MGE3ODE2NmRhMWQ1YmIxNmJjZmQ2OTI2NTk3NWM5ZTk0NDg5YzU1MzI2NGI0Y2Y1ZWNjODE3MjcxMyJ9fX0=").name(Color.BLUE.getChatColor()+"Blue").build());
		Color.BROWN.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmNjk5M2RlY2ZjYzExYmZkODQ3MTgwYmFkNDMyN2Y0ZTIyOGYxMDkyNzhlZjk0YjFhNTk1ZjQ5NjJmMjI4NiJ9fX0=").name(Color.BROWN.getChatColor()+"Brown").build());
		Color.CYAN.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk4ODcyOGRlZTUwODQxMDdiNDdmM2VkMzA5NjE4NWM5ZDhlNjIyOTliMTFiMDA2NWUzZmIwMWZlOGNlNjBhOCJ9fX0=").name(Color.CYAN.getChatColor()+"Cyan").build());
		Color.GREEN.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNlYTdmOGI3ZTFlMTM5OGJhYjIyNzc0OTYzMzMyZDcyZGIzNzhmNGRiMDYxN2E2Yzk2ZGNhYzg5NTNjMDdlNCJ9fX0=").name(Color.GREEN.getChatColor()+"Green").build());
		Color.LIME.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGVhZDg4ODcwMDcwMzRhZDA4MmEyMjFkNzRmOGU0ZDljNzI0OGRiZWUyNTJhZmQwYjY4N2E1OTA4NjczMDU1MyJ9fX0=").name(Color.LIME.getChatColor()+"Lime").build());
		Color.ORANGE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRjNDBiMjBiYzJhOWQ1ZGRhN2EwYTc5ZjYzYjBiZDc5M2IyY2M5M2RlM2RkNDgwMTM5ZDg1M2MzYTMzODEyNSJ9fX0=").name(Color.ORANGE.getChatColor()+"Orange").build());
		Color.PINK.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgxMjUyMTc3MWE3Y2I4NDFhNWZmYTIzNzU2NjEwM2MyZjllNGRjMzQ0NTJlNzY2MTgyOWQwYjA5MjEyZTNmNCJ9fX0=").name(Color.PINK.getChatColor()+"Pink").build());
		Color.PURPLE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTgwNGNiY2NjNTZiZjk5MjZhNjk2MjY0MzZjNjE4M2QwNGJhNzE4MzY5NTliMzJlZGJiYzAwMzE3YTcxYTUwZiJ9fX0=").name(Color.PURPLE.getChatColor()+"Purple").build());
		Color.RED.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdjMTRlYjg3ZGM2NDQ0YWU2MjVmMTIyY2YzYWU5NmZjOGEyODZhYmI2OWRjYzc4ZWU5NWNkNDQzMjMyYTA1YyJ9fX0=").name(Color.RED.getChatColor()+"Red").build());
		Color.WHITE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTcwMWFiZjQ0YWRhN2YyOWRiYTliYzkzMDE2MzBiYjIzNWRjZWFmMGYwZGVmYTVhMzc4YmRjYjBiMDFkYjU4YSJ9fX0=").name(Color.WHITE.getChatColor()+"White").build());
		Color.YELLOW.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFiZjg0ODc3OWNhODFlZDlmNGJjODhmOTM0NTEzZGQ4Y2Q2NzEyMzAxZGIzNjk1MDg4MDFhZGNhMDk4Y2QwMCJ9fX0=").name(Color.YELLOW.getChatColor()+"Yellow").build());
		InvClick.itens.put(Color.BLACK.getHead(), player -> Matches.vote(profiles.get(player), Color.BLACK));
		InvClick.itens.put(Color.BLUE.getHead(), player -> Matches.vote(profiles.get(player), Color.BLUE));
		InvClick.itens.put(Color.BROWN.getHead(), player -> Matches.vote(profiles.get(player), Color.BROWN));
		InvClick.itens.put(Color.CYAN.getHead(), player -> Matches.vote(profiles.get(player), Color.CYAN));
		InvClick.itens.put(Color.GREEN.getHead(), player -> Matches.vote(profiles.get(player), Color.GREEN));
		InvClick.itens.put(Color.LIME.getHead(), player -> Matches.vote(profiles.get(player), Color.LIME));
		InvClick.itens.put(Color.ORANGE.getHead(), player -> Matches.vote(profiles.get(player), Color.ORANGE));
		InvClick.itens.put(Color.PINK.getHead(), player -> Matches.vote(profiles.get(player), Color.PINK));
		InvClick.itens.put(Color.PURPLE.getHead(), player -> Matches.vote(profiles.get(player), Color.PURPLE));
		InvClick.itens.put(Color.RED.getHead(), player -> Matches.vote(profiles.get(player), Color.RED));
		InvClick.itens.put(Color.WHITE.getHead(), player -> Matches.vote(profiles.get(player), Color.WHITE));
		InvClick.itens.put(Color.YELLOW.getHead(), player -> Matches.vote(profiles.get(player), Color.YELLOW));
	}
	
	private void loadTeams() {
		Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
		for (Team team : sb.getTeams()) team.unregister();
		Team crewmate = sb.registerNewTeam("crewmate");
		Team impostor = sb.registerNewTeam("impostor");
		PlayerType.CREWMATE.setTeam(crewmate);
		PlayerType.DEAD_CREWMATE.setTeam(crewmate);
		PlayerType.IMPOSTOR.setTeam(impostor);
		PlayerType.DEAD_IMPOSTOR.setTeam(impostor);
	}
	
	private String getMessage(String path) {
		return getConfig().getString(path).replace("&", "§");
	}
	
	public static void actionbar(Player player, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\""+message+"\"}"), (byte)2);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
}