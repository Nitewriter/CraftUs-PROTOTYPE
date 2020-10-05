package br.com.abidux.craftus.api.objects.tasks;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.abidux.craftus.enums.HeadsItem;

public class ElectricTask extends Task {
	
	private HashMap<Integer, Button> buttons = new HashMap<>();
	
	public ElectricTask(Location location) {
		super(location, HeadsItem.ELECTRIC.getHead());
	}
	
	public void openTask(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 3*9, "§6Click Task");
		inventory.setItem(10, new Button(buttons, 10).getButton());
		inventory.setItem(12, new Button(buttons, 12).getButton());
		inventory.setItem(14, new Button(buttons, 14).getButton());
		inventory.setItem(16, new Button(buttons, 16).getButton());
		player.openInventory(inventory);
	}
	
	public void updateButton(Player player, int slot) {
		player.getOpenInventory().setItem(slot, getButton(slot).getButton());
		player.updateInventory();
	}
	
	public Button getButton(int slot) {
		if (!buttons.containsKey(slot)) return null;
		return buttons.get(slot);
	}
	
	public Collection<Button> getButtons() {
		return buttons.values();
	}
	
}