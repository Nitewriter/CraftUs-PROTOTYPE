package br.com.abidux.craftus.api.objects;

import org.bukkit.Location;

public class Map {
	
	private String name;
	private Location spawn;
	
	public Map(String name, Location spawn) {
		this.name = name;
		this.spawn = spawn;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public String getName() {
		return name;
	}
	
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}