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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WiresCrewTask extends CrewTask {

    private final HashMap<Player, DyeColor> selectedColor = new HashMap<>();
    private final HashMap<DyeColor, ItemStack> color = new HashMap<>();

    @SuppressWarnings("deprecation")
    public WiresCrewTask(Location location) {
        super(location, HeadItem.MYSTERY_BOX.getHead());
        color.put(DyeColor.LIGHT_BLUE, new ItemBuilder().type(Material.WOOL).name("�bWire").durability(DyeColor.LIGHT_BLUE.getWoolData()).build());
        color.put(DyeColor.RED, new ItemBuilder().type(Material.WOOL).name("�cWire").durability(DyeColor.RED.getWoolData()).build());
        color.put(DyeColor.ORANGE, new ItemBuilder().type(Material.WOOL).name("�6Wire").durability(DyeColor.ORANGE.getWoolData()).build());
        color.put(DyeColor.MAGENTA, new ItemBuilder().type(Material.WOOL).name("�dWire").durability(DyeColor.MAGENTA.getWoolData()).build());
    }

    public void openTask(Player player) {
        ItemStack[] itens = getWires();
        Inventory inventory = Bukkit.createInventory(null, 4 * 9, "�9Wires Task");
        int[] slots = {1, 7, 10, 16, 19, 25, 28, 34};
        for (int i = 0; i < 8; i++) {
            inventory.setItem(slots[i], itens[i]);
        }
        player.openInventory(inventory);
    }

    @SuppressWarnings("deprecation")
    public void select(Player player, int slot) {
        InventoryView inventory = player.getOpenInventory();
        ItemStack item = inventory.getItem(slot);
        if (item == null) {
            return;
        }
        if (selectedColor.containsKey(player)) {
            DyeColor color = selectedColor.get(player);
            if (item.getDurability() == color.getWoolData()) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
                selectedColor.remove(player);
                return;
            }
        } else {
            DyeColor color = DyeColor.getByWoolData((byte) item.getDurability());
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            selectedColor.put(player, color);
            return;
        }
    }

    public boolean won(Player player) {
        int[] slots = {1, 7, 10, 16, 19, 25, 28, 34};
        boolean won = true;
        for (int slot : slots) {
            if (!player.getOpenInventory().getItem(slot).getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)) {
                won = false;
                break;
            }
        }
        return won;
    }

    private ItemStack[] getWires() {
        ItemStack[] items = new ItemStack[8];
        List<DyeColor> colors = new ArrayList<>();
        colors.add(DyeColor.LIGHT_BLUE);
        colors.add(DyeColor.LIGHT_BLUE);
        colors.add(DyeColor.RED);
        colors.add(DyeColor.RED);
        colors.add(DyeColor.ORANGE);
        colors.add(DyeColor.ORANGE);
        colors.add(DyeColor.MAGENTA);
        colors.add(DyeColor.MAGENTA);
        for (int i = 0; i < 8; i++) {
            DyeColor randomColor = colors.get(new Random().nextInt(colors.size()));
            ItemStack item = color.get(randomColor);
            items[i] = item;
            colors.remove(randomColor);
        }
        return items;
    }

}