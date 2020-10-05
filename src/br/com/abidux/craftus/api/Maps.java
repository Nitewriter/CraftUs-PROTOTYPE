package br.com.abidux.craftus.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.objects.Map;
import br.com.abidux.craftus.enums.HeadsItem;
import br.com.abidux.craftus.enums.Messages;

public class Maps {
	
	public static HashMap<String, Map> maps = new HashMap<String, Map>();
	public static Maps instance;
	private File file = new File(Main.instance.getDataFolder(), "maps.json");
	
	public Maps() {
		instance = this;
	}
	
	public static void createMap(String name, Location location) {
		maps.put(name.toLowerCase(), new Map(name, location));
	}
	
	public static void createFile() {
		if (!Maps.instance.file.exists())
			try {
				Maps.instance.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Maps.instance.file));
			StringBuilder builder = new StringBuilder();
			reader.lines().forEach(line -> builder.append(line));
			reader.close();
			if (builder.toString().isEmpty()) return;
			JSONObject obj = (JSONObject) new JSONParser().parse(builder.toString());
			JSONArray array = (JSONArray) obj.get("maps");
			for (int i = 0; i < array.size(); i++) {
				JSONObject map = (JSONObject) array.get(i);
				Location location = new Location(Bukkit.getWorld(map.get("world").toString()), (double) map.get("x"), (double) map.get("y"), (double) map.get("z"));
				location.setPitch((float)((double) map.get("pitch")));
				location.setYaw((float)((double) map.get("yaw")));
				maps.put(map.get("name").toString().toLowerCase(), new Map(map.get("name").toString(), location));
			}
			Bukkit.getConsoleSender().sendMessage(Messages.PREFIX.toString()+Maps.maps.size()+" maps loaded!");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void save() {
		try {
			JSONObject object = new JSONObject();
			JSONArray array = new JSONArray();
			for (Map map : maps.values()) {
				JSONObject obj = new JSONObject();
				obj.put("name", map.getName());
				obj.put("world", map.getSpawn().getWorld().getName());
				obj.put("x", map.getSpawn().getX());
				obj.put("y", map.getSpawn().getY());
				obj.put("z", map.getSpawn().getZ());
				obj.put("pitch", map.getSpawn().getPitch());
				obj.put("yaw", map.getSpawn().getYaw());
				array.add(obj);
			}
			object.put("maps", array);
			PrintWriter writer = new PrintWriter(Maps.instance.file);
			writer.print(object.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static ItemStack getItem(Map map) {
		ItemStack item = HeadsItem.EARTH.getHead();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§a"+map.getName());
		item.setItemMeta(meta);
		return item;
	}
	
}