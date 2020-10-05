package br.com.abidux.craftus.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class HologramBuilder {

    private Location location;
    public HologramBuilder(Location location) {
        this.location = location;
    }

    private String[] text;
    public HologramBuilder lines(String... text) {
        this.text = text;
        return this;
    }

    public static ArmorStand spawnArmorStand(Location location, String name) {
        ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setSmall(true);
        as.setCustomName(name);
        as.setCustomNameVisible(true);
        as.setVisible(false);
        as.setCanPickupItems(false);
        return as;
    }

    public ArrayList<ArmorStand> build() {
        ArrayList<ArmorStand> stands = new ArrayList<>();
        float decrease = 0.0f;
        for (int i = 0; i < text.length; i++) {
            stands.add(spawnArmorStand(this.location.clone().add(0, decrease, 0), this.text[i]));
            decrease -= 0.3f;
        }
        return stands;
    }

}