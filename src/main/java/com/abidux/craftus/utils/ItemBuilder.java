package com.abidux.craftus.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemBuilder {

    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private ArrayList<String> lore;
    private int durability = -1;
    private Material material;
    private ItemFlag[] flags;
    private int amount = 1;
    private String name;

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(String... lines) {
        ArrayList<String> lore = new ArrayList<>();
        Collections.addAll(lore, lines);
        this.lore = lore;
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder type(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        this.flags = flags;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(this.material);
        item.setAmount(this.amount);
        if (!this.enchantments.isEmpty()) {
            item.addUnsafeEnchantments(this.enchantments);
        }
        if (this.durability != -1) {
            item.setDurability((short) this.durability);
        }
        ItemMeta meta = item.getItemMeta();
        if (this.flags != null) {
            meta.addItemFlags(this.flags);
        }
        if (this.name != null) {
            meta.setDisplayName(this.name);
        }
        if (this.lore != null) {
            meta.setLore(this.lore);
        }
        item.setItemMeta(meta);
        return item;
    }

}