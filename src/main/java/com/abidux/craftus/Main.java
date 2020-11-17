package com.abidux.craftus;

import com.abidux.craftus.api.GameMaps;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.PlayerType;
import com.abidux.craftus.api.objects.CraftUsProfile;
import com.abidux.craftus.api.objects.tasks.*;
import com.abidux.craftus.commands.CraftUs;
import com.abidux.craftus.commands.Lobby;
import com.abidux.craftus.commands.Search;
import com.abidux.craftus.enums.DisplayMessage;
import com.abidux.craftus.enums.HeadArt;
import com.abidux.craftus.enums.HeadItem;
import com.abidux.craftus.enums.PlayerColor;
import com.abidux.craftus.listeners.*;
import com.abidux.craftus.utils.SkullBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static Main instance;
    public static Location lobby;
    public static HashMap<Player, CraftUsProfile> profiles = new HashMap<>();

    public File st = new File(getDataFolder(), "stands.yml");
    public FileConfiguration stands = YamlConfiguration.loadConfiguration(st);

    public static void saveStands(Boolean shouldCleanUp) {
        int i = 0;
        for (CrewTask crewTask : InvClick.tasks.values()) {
            if (shouldCleanUp) {
                crewTask.getStand().remove();
            }
            crewTask.save(String.valueOf(i), crewTask.getClass().getName());
            i++;
        }
        for (MeetingButton button : CraftUs.buttons.values()) {
            if (shouldCleanUp) {
                button.getStand().remove();
            }
            button.save(String.valueOf(i), button.getClass().getName());
            i++;
        }

        try {
            instance.stands.save(instance.st);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void actionbar(Player player, String message) {
        TextComponent textComponent = new TextComponent(message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        if (getConfig().isSet("lobby")) {
            lobby = (Location) getConfig().get("lobby");
        }
        load();
        loadHelmets();
        loadTeams();
        new GameMaps();
        GameMaps.createFile();
        GameMaps.load();
        Matches.load();
        if (st.exists()) {
            loadTasks();
        }
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
            if (profile.getCurrentMatch() != null) {
                profile.getCurrentMatch().leave(player, true);
            }
            Matches.leave(profile);
        }
        GameMaps.save();
        saveStands(true);
    }

    private void loadTasks() {
        for (String stand : stands.getConfigurationSection("").getKeys(false)) {
            String type = stands.getString(stand + ".type");
            Location location = new Location(Bukkit.getWorld(stands.getString(stand + ".world")), stands.getDouble(stand + ".x"), stands.getDouble(stand + ".y"), stands.getDouble(stand + ".z"));
            location.setPitch((float) stands.getDouble(stand + ".pitch"));
            location.setYaw((float) stands.getDouble(stand + ".yaw"));
            switch (type) {
                case "com.abidux.craftus.api.objects.tasks.CommsCrewTask": {
                    CommsCrewTask task = new CommsCrewTask(location);
                    InvClick.tasks.put(task.getStand(), task);
                    break;
                }
                case "com.abidux.craftus.api.objects.tasks.ElectricCrewTask": {
                    ElectricCrewTask task = new ElectricCrewTask(location);
                    InvClick.tasks.put(task.getStand(), task);
                    break;
                }
                case "com.abidux.craftus.api.objects.tasks.LevelCrewTask": {
                    LevelCrewTask task = new LevelCrewTask(location);
                    InvClick.tasks.put(task.getStand(), task);
                    break;
                }
                case "com.abidux.craftus.api.objects.tasks.MazeCrewTask": {
                    Inventory inventory = Bukkit.createInventory(null, stands.getInt(stand + ".invsize"), "�6Click Task");
                    for (int i : stands.getIntegerList(stand + ".inventory")) {
                        inventory.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
                    }
                    MazeCrewTask task = new MazeCrewTask(location, inventory);
                    InvClick.tasks.put(task.getStand(), task);
                    break;
                }
                case "com.abidux.craftus.api.objects.tasks.MeteorCrewTask": {
                    MeteorCrewTask task = new MeteorCrewTask(location);
                    InvClick.tasks.put(task.getStand(), task);
                    break;
                }
                case "com.abidux.craftus.api.objects.tasks.WiresCrewTask": {
                    WiresCrewTask task = new WiresCrewTask(location);
                    InvClick.tasks.put(task.getStand(), task);
                    break;
                }
                case "com.abidux.craftus.api.objects.tasks.MeetingButton":
                    MeetingButton button = new MeetingButton(location);
                    CraftUs.buttons.put(button.getStand(), button);
                    break;
                default:
                    System.out.println("Unknown armor stand type: " + type);
                    break;
            }
        }
    }

    public void load() {
        DisplayMessage.PREFIX.setContent(getMessage("prefix") + " ");
        DisplayMessage.YOU_ARE_NOT_ALLOWED_TO_DO_THIS.setContent(getMessage("messages.you_are_not_allowed_to_do_this"));
        DisplayMessage.CREATE.setContent(getMessage("messages.create"));
        DisplayMessage.CLICK_TO_CREATE.setContent(getMessage("messages.click_to_create"));
        DisplayMessage.SEARCH.setContent(getMessage("messages.search"));
        DisplayMessage.CLICK_TO_SEARCH.setContent(getMessage("messages.click_to_search"));
        DisplayMessage.SEND_NAME.setContent(getMessage("messages.send_name"));
        DisplayMessage.MAP_CREATED.setContent(getMessage("messages.map_created"));
        DisplayMessage.CLICK_TO_EDIT.setContent(getMessage("messages.click_to_edit"));
        DisplayMessage.PREVIOUS.setContent(getMessage("messages.previous"));
        DisplayMessage.NEXT.setContent(getMessage("messages.next"));
        DisplayMessage.EDIT_MAP.setContent(getMessage("messages.edit_map"));
        DisplayMessage.MODIFIED.setContent(getMessage("messages.modified"));
        DisplayMessage.JOINED.setContent(getMessage("messages.joined"));
        DisplayMessage.LEFT.setContent(getMessage("messages.left"));
        DisplayMessage.IMPOSTOR_TITLE.setContent(getMessage("messages.impostor_title"));
        DisplayMessage.IMPOSTOR_SUBTITLE.setContent(getMessage("messages.impostor_subtitle"));
        DisplayMessage.CREWMATE_TITLE.setContent(getMessage("messages.crewmate_title"));
        DisplayMessage.CREWMATE_SUBTITLE.setContent(getMessage("messages.crewmate_subtitle"));
        HeadItem.PLUS.setItem(new SkullBuilder(HeadArt.PLUS.getUrl()).name(DisplayMessage.CREATE.toString()).lore(DisplayMessage.CLICK_TO_CREATE.toString()).build());
        HeadItem.BOOK.setItem(new SkullBuilder(HeadArt.BOOK.getUrl()).name(DisplayMessage.SEARCH.toString()).lore(DisplayMessage.CLICK_TO_SEARCH.toString()).build());
        HeadItem.EARTH.setItem(new SkullBuilder(HeadArt.EARTH.getUrl()).name("?").lore(DisplayMessage.CLICK_TO_EDIT.toString()).build());
        HeadItem.BED.setItem(new SkullBuilder(HeadArt.BED.getUrl()).name("�aSetSpawn").build());
        HeadItem.X.setItem(new SkullBuilder(HeadArt.X.getUrl()).name("�cDelete").build());
        HeadItem.RADIO.setItem(new SkullBuilder(HeadArt.RADIO.getUrl()).name("�7Comms").build());
        HeadItem.ELECTRIC.setItem(new SkullBuilder(HeadArt.ELECTRIC.getUrl()).name("�7Electric").build());
        HeadItem.MAZE.setItem(new SkullBuilder(HeadArt.MAZE.getUrl()).name("�7Maze").build());
        HeadItem.METEORITE.setItem(new SkullBuilder(HeadArt.METEORITE.getUrl()).name("�7Meteor").build());
        HeadItem.WAVE.setItem(new SkullBuilder(HeadArt.WAVE.getUrl()).name("�7Fix Energy Level").build());
        HeadItem.MYSTERY_BOX.setItem(new SkullBuilder(HeadArt.MYSTERY_BOX.getUrl()).name("�7Connect Wires").build());
        HeadItem.MEETING_BUTTON.setItem(new SkullBuilder(HeadArt.MEETING_BUTTON.getUrl()).name("�eMeeting Button").build());
    }

    private void loadHelmets() {
        PlayerColor.BLACK.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRlMzJjNzY0ZjNmMjhkMjcyOWIxNjFlZTM1MGZhMDQzZWI3ZDI5MjExMzIzYWVmMzRjMzZjOTM4ZDEwOGExMCJ9fX0=").name(PlayerColor.BLACK.getChatColor() + "Black").build());
        PlayerColor.BLUE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZjODU1MGE3ODE2NmRhMWQ1YmIxNmJjZmQ2OTI2NTk3NWM5ZTk0NDg5YzU1MzI2NGI0Y2Y1ZWNjODE3MjcxMyJ9fX0=").name(PlayerColor.BLUE.getChatColor() + "Blue").build());
        PlayerColor.BROWN.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmNjk5M2RlY2ZjYzExYmZkODQ3MTgwYmFkNDMyN2Y0ZTIyOGYxMDkyNzhlZjk0YjFhNTk1ZjQ5NjJmMjI4NiJ9fX0=").name(PlayerColor.BROWN.getChatColor() + "Brown").build());
        PlayerColor.CYAN.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk4ODcyOGRlZTUwODQxMDdiNDdmM2VkMzA5NjE4NWM5ZDhlNjIyOTliMTFiMDA2NWUzZmIwMWZlOGNlNjBhOCJ9fX0=").name(PlayerColor.CYAN.getChatColor() + "Cyan").build());
        PlayerColor.GREEN.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNlYTdmOGI3ZTFlMTM5OGJhYjIyNzc0OTYzMzMyZDcyZGIzNzhmNGRiMDYxN2E2Yzk2ZGNhYzg5NTNjMDdlNCJ9fX0=").name(PlayerColor.GREEN.getChatColor() + "Green").build());
        PlayerColor.LIME.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGVhZDg4ODcwMDcwMzRhZDA4MmEyMjFkNzRmOGU0ZDljNzI0OGRiZWUyNTJhZmQwYjY4N2E1OTA4NjczMDU1MyJ9fX0=").name(PlayerColor.LIME.getChatColor() + "Lime").build());
        PlayerColor.ORANGE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRjNDBiMjBiYzJhOWQ1ZGRhN2EwYTc5ZjYzYjBiZDc5M2IyY2M5M2RlM2RkNDgwMTM5ZDg1M2MzYTMzODEyNSJ9fX0=").name(PlayerColor.ORANGE.getChatColor() + "Orange").build());
        PlayerColor.PINK.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgxMjUyMTc3MWE3Y2I4NDFhNWZmYTIzNzU2NjEwM2MyZjllNGRjMzQ0NTJlNzY2MTgyOWQwYjA5MjEyZTNmNCJ9fX0=").name(PlayerColor.PINK.getChatColor() + "Pink").build());
        PlayerColor.PURPLE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTgwNGNiY2NjNTZiZjk5MjZhNjk2MjY0MzZjNjE4M2QwNGJhNzE4MzY5NTliMzJlZGJiYzAwMzE3YTcxYTUwZiJ9fX0=").name(PlayerColor.PURPLE.getChatColor() + "Purple").build());
        PlayerColor.RED.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdjMTRlYjg3ZGM2NDQ0YWU2MjVmMTIyY2YzYWU5NmZjOGEyODZhYmI2OWRjYzc4ZWU5NWNkNDQzMjMyYTA1YyJ9fX0=").name(PlayerColor.RED.getChatColor() + "Red").build());
        PlayerColor.WHITE.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTcwMWFiZjQ0YWRhN2YyOWRiYTliYzkzMDE2MzBiYjIzNWRjZWFmMGYwZGVmYTVhMzc4YmRjYjBiMDFkYjU4YSJ9fX0=").name(PlayerColor.WHITE.getChatColor() + "White").build());
        PlayerColor.YELLOW.setHead(new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFiZjg0ODc3OWNhODFlZDlmNGJjODhmOTM0NTEzZGQ4Y2Q2NzEyMzAxZGIzNjk1MDg4MDFhZGNhMDk4Y2QwMCJ9fX0=").name(PlayerColor.YELLOW.getChatColor() + "Yellow").build());
        InvClick.items.put(PlayerColor.BLACK.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.BLACK));
        InvClick.items.put(PlayerColor.BLUE.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.BLUE));
        InvClick.items.put(PlayerColor.BROWN.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.BROWN));
        InvClick.items.put(PlayerColor.CYAN.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.CYAN));
        InvClick.items.put(PlayerColor.GREEN.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.GREEN));
        InvClick.items.put(PlayerColor.LIME.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.LIME));
        InvClick.items.put(PlayerColor.ORANGE.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.ORANGE));
        InvClick.items.put(PlayerColor.PINK.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.PINK));
        InvClick.items.put(PlayerColor.PURPLE.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.PURPLE));
        InvClick.items.put(PlayerColor.RED.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.RED));
        InvClick.items.put(PlayerColor.WHITE.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.WHITE));
        InvClick.items.put(PlayerColor.YELLOW.getHead(), player -> Matches.vote(profiles.get(player), PlayerColor.YELLOW));
    }

    private void loadTeams() {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team team : sb.getTeams()) {
            team.unregister();
        }
        Team crewmate = sb.registerNewTeam("crewmate");
        Team impostor = sb.registerNewTeam("impostor");
        PlayerType.CREWMATE.setTeam(crewmate);
        PlayerType.DEAD_CREWMATE.setTeam(crewmate);
        PlayerType.IMPOSTOR.setTeam(impostor);
        PlayerType.DEAD_IMPOSTOR.setTeam(impostor);
    }

    private String getMessage(String path) {
        return getConfig().getString(path).replace("&", "�");
    }

}