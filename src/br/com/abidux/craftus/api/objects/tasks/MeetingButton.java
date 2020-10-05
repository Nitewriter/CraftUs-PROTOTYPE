package br.com.abidux.craftus.api.objects.tasks;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.enums.HeadsItem;

public class MeetingButton {
	
	private ItemStack item;
	private Location location;
	public int cooldown = 90;
	private ArmorStand stand;
	public MeetingButton(Location location) {
		this.location = location;
		this.item = HeadsItem.MEETING_BUTTON.getHead();
		this.stand = createStand();
	}
	
	private ArmorStand createStand() {
		location.getChunk().load();
		ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		as.setCustomName("§eMeeting Button");
		as.setCustomNameVisible(true);
		as.setGravity(false);
		as.setVisible(false);
		as.setHelmet(item);
		as.setRemoveWhenFarAway(false);
		return as;
	}
	
	public ArmorStand getStand() {
		return stand;
	}
	
	public void save(String path, String type) {
		FileConfiguration stands = Main.instance.stands;
		stands.set(path+".x", location.getX());
		stands.set(path+".y", location.getY());
		stands.set(path+".z", location.getZ());
		stands.set(path+".pitch", location.getPitch());
		stands.set(path+".yaw", location.getYaw());
		stands.set(path+".world", location.getWorld().getName());
		stands.set(path+".type", type);
	}
	
}