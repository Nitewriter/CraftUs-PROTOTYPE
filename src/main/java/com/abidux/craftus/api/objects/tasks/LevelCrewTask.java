package com.abidux.craftus.api.objects.tasks;

import com.abidux.craftus.enums.HeadItem;
import com.abidux.craftus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class LevelCrewTask extends CrewTask {

    private final int[] slots = {10, 11, 12, 13, 14, 15, 16};

    public LevelCrewTask(Location location) {
        super(location, HeadItem.WAVE.getHead());
    }

    @SuppressWarnings("deprecation")
    public void openTask(Player player) {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(new ItemBuilder().type(Material.WOOL).durability(DyeColor.LIGHT_BLUE.getWoolData()).name("�bButton").build());
        items.add(new ItemBuilder().type(Material.WOOL).durability(DyeColor.MAGENTA.getWoolData()).name("�dButton").build());
        items.add(new ItemBuilder().type(Material.WOOL).durability(DyeColor.ORANGE.getWoolData()).name("�6Button").build());
        items.add(new ItemBuilder().type(Material.WOOL).durability(DyeColor.RED.getWoolData()).name("�cButton").flags(ItemFlag.HIDE_ENCHANTS).enchantment(Enchantment.DURABILITY, 1).build());
        items.add(new ItemBuilder().type(Material.WOOL).durability(DyeColor.LIME.getWoolData()).name("�aButton").build());
        items.add(new ItemBuilder().type(Material.WOOL).durability(DyeColor.BLUE.getWoolData()).name("�1Button").build());
        items.add(new ItemBuilder().type(Material.WOOL).durability(DyeColor.PURPLE.getWoolData()).name("�5Button").build());
        Collections.shuffle(items);
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "�9LevelTask");
        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(slots[i], items.get(i));
        }
        player.openInventory(inventory);
    }

}