package br.com.abidux.craftus.api.objects.tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.abidux.craftus.enums.HeadsItem;

public class MeteorTask extends Task {
	
	private int acertos = 0;
	private Inventory inventory;
	public MeteorTask(Location location) {
		super(location, HeadsItem.METEORITE.getHead());
		this.inventory = Bukkit.createInventory(null, 3*9, "§6Click Task");
	}
	
	public void openTask(Player player) {
		inventory.setItem(randomSlot(), HeadsItem.METEORITE.getHead());
		player.openInventory(inventory);
	}
	
	public int randomSlot() {
		return new Random().nextInt(inventory.getSize());
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public int getAcertos() {
		return acertos;
	}
	
	public void setAcertos(int acertos) {
		this.acertos = acertos;
	}
	
	public void addAcerto() {
		acertos++;
	}
	
}