package com.abidux.craftus.game.tasks;

import com.abidux.craftus.enums.HeadItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Random;

public class MeteorCrewTask extends CrewTask {

    private final Inventory inventory;
    private int meteors = 0;

    public MeteorCrewTask(Location location) {
        super(location, HeadItem.METEORITE.getHead());
        inventory = Bukkit.createInventory(null, 3 * 9, "§6Click Task");
    }

    public void openTask(Player player) {
        inventory.setItem(randomSlot(), HeadItem.METEORITE.getHead());
        player.openInventory(inventory);
    }

    public int randomSlot() {
        return new Random().nextInt(inventory.getSize());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getMeteors() {
        return meteors;
    }

    public void setMeteors(int meteors) {
        this.meteors = meteors;
    }

    public void addMeteor() {
        meteors++;
    }

}