package br.com.abidux.craftus.api.objects.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.enums.HeadsItem;

public class MazeTask extends Task {
	
	private HashMap<Integer, Button> buttons = new HashMap<>();
	
	private Inventory inventory;
	public MazeTask(Location location, Inventory inventory) {
		super(location, HeadsItem.MAZE.getHead());
		this.inventory = inventory;
	}
	
	public void openTask(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 6*9, "§6Click Task");
		for (int i = 0; i < this.inventory.getSize(); i++) {
			ItemStack item = this.inventory.getItem(i);
			if (item != null && item.getType().equals(Material.STAINED_GLASS_PANE)) inventory.setItem(i, new Button(buttons, i).getButton());
		}
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
	
	@Override
	public void save(String path, String type) {
		super.save(path, type);
		List<Integer> itemsSlot = new ArrayList<Integer>();
		for (int i = 0; i < inventory.getSize(); i++)
			if (inventory.getItem(i) != null && inventory.getItem(i).getType().equals(Material.STAINED_GLASS_PANE))
				itemsSlot.add(i);
		Main.instance.stands.set(path+".invsize", inventory.getSize());
		Main.instance.stands.set(path+".inventory", itemsSlot);
	}
	
}