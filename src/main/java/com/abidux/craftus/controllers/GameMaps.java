package com.abidux.craftus.controllers;

import com.abidux.craftus.enums.DisplayMessage;
import com.abidux.craftus.enums.HeadItem;
import com.abidux.craftus.events.GameMapsLoadedEvent;
import com.abidux.craftus.game.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameMaps {

    public static HashMap<String, GameMap> maps = new HashMap<>();
    public static GameMaps instance;
    private final File file;

    public GameMaps(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "maps.json");
        instance = this;
    }

    public static void createMap(String name, Location location) {
        maps.put(name.toLowerCase(), new GameMap(name, location));
    }

    public static void createFile() {
        if (!GameMaps.instance.file.exists()) {
            try {
                if (GameMaps.instance.file.createNewFile()) {
                    System.out.println("Maps file created.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(GameMaps.instance.file));
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(builder::append);
            reader.close();
            if (builder.toString().isEmpty()) {
                return;
            }
            JSONObject obj = (JSONObject) new JSONParser().parse(builder.toString());
            JSONArray array = (JSONArray) obj.get("maps");
            for (Object o : array) {
                JSONObject map = (JSONObject) o;
                Location location = new Location(
                        Bukkit.getWorld(map.get("world").toString()), (double) map.get("x"), (double) map.get("y"),
                        (double) map.get("z")
                );
                location.setPitch((float) ((double) map.get("pitch")));
                location.setYaw((float) ((double) map.get("yaw")));
                maps.put(map.get("name").toString().toLowerCase(), new GameMap(map.get("name").toString(), location));
            }
            Bukkit.getConsoleSender().sendMessage(
                    DisplayMessage.PREFIX.toString() + GameMaps.maps.size() + " maps loaded!");

            List<GameMap> gameMaps = new ArrayList<>(maps.values());
            Event event = new GameMapsLoadedEvent(gameMaps);
            Bukkit.getPluginManager().callEvent(event);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void save() {
        try {
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            for (GameMap gameMap : maps.values()) {
                JSONObject obj = new JSONObject();
                obj.put("name", gameMap.getName());
                obj.put("world", gameMap.getSpawn().getWorld().getName());
                obj.put("x", gameMap.getSpawn().getX());
                obj.put("y", gameMap.getSpawn().getY());
                obj.put("z", gameMap.getSpawn().getZ());
                obj.put("pitch", gameMap.getSpawn().getPitch());
                obj.put("yaw", gameMap.getSpawn().getYaw());
                array.add(obj);
            }
            object.put("maps", array);
            System.out.println("Writing json maps: " + object.toJSONString());
            PrintWriter writer = new PrintWriter(GameMaps.instance.file);
            writer.print(object.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ItemStack getItem(GameMap gameMap) {
        ItemStack item = HeadItem.EARTH.getHead();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a" + gameMap.getName());
        item.setItemMeta(meta);
        return item;
    }

}