package com.abidux.craftus.game.items;

import com.abidux.craftus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Equipment {

    public static final ItemStack sword = new ItemBuilder().type(Material.IRON_SWORD).name("§cSword").build();
    public static final ItemStack pliers = new ItemBuilder().type(Material.SHEARS).name("§cPliers").build();

}
