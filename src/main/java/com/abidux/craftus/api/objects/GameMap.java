package com.abidux.craftus.api.objects;

import org.bukkit.Location;

public class GameMap {

    private String name;
    private Location spawn;

    public GameMap(String name, Location spawn) {
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