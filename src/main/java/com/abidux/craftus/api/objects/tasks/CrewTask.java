package com.abidux.craftus.api.objects.tasks;

import com.abidux.craftus.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;


public class CrewTask {

    private final ItemStack item;
    private final Location location;
    private final ArmorStand stand;

    public CrewTask(Location location, ItemStack item) {
        this.location = location;
        this.item = item;
        stand = createStand();
    }

    private ArmorStand createStand() {
        location.getChunk().load();
        ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        as.setCustomName("�eTask");
        as.setCustomNameVisible(true);
        as.setGravity(false);
        as.getEquipment().setHelmet(item);
        as.setVisible(false);
        as.setRemoveWhenFarAway(false);
        return as;
    }

    public ArmorStand getStand() {
        return stand;
    }

    public void save(String path, String type) {
        FileConfiguration stands = Main.instance.stands;
        stands.set(path + ".x", location.getX());
        stands.set(path + ".y", location.getY());
        stands.set(path + ".z", location.getZ());
        stands.set(path + ".pitch", location.getPitch());
        stands.set(path + ".yaw", location.getYaw());
        stands.set(path + ".world", location.getWorld().getName());
        stands.set(path + ".type", type);
    }

}