package com.abidux.craftus.game.tasks;

import com.abidux.craftus.enums.HeadItem;
import com.abidux.craftus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CommsCrewTask extends CrewTask {

    ItemStack[] icons = new ItemStack[5];

    public CommsCrewTask(Location location) {
        super(location, HeadItem.RADIO.getHead());
        icons[0] = new ItemBuilder().type(Material.DIAMOND_SWORD).build();
        icons[1] = new ItemBuilder().type(Material.BOW).build();
        icons[2] = new ItemBuilder().type(Material.ARROW).build();
        icons[3] = new ItemBuilder().type(Material.CACTUS).build();
        icons[4] = new ItemBuilder().type(Material.LAVA_BUCKET).build();
    }

    public void openTask(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, "§9Comms Task");
        inventory.setItem(13, getRandom());
        inventory.setItem(28, getRandom());
        inventory.setItem(30, getRandom());
        inventory.setItem(32, getRandom());
        inventory.setItem(34, getRandom());
        player.openInventory(inventory);
    }

    private ItemStack getRandom() {
        return icons[new Random().nextInt(icons.length)];
    }

    public void changeItem(Player player, int slot) {
        InventoryView inventory = player.getOpenInventory();
        if (slot == 13 || inventory.getItem(slot) == null) {
            return;
        }
        for (int i = 0; i < icons.length; i++) {
            if (icons[i].equals(inventory.getItem(slot))) {
                inventory.setItem(slot, icons.length > i + 1 ? icons[i + 1] : icons[0]);
                break;
            }
        }
        player.updateInventory();
    }

}