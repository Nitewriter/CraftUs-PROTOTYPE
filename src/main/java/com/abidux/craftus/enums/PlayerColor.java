package com.abidux.craftus.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

public enum PlayerColor {

    RED("�4", Arrays.asList(255, 0, 0), null),
    BLUE("�1", Arrays.asList(93, 0, 255), null),
    GREEN("�2", Arrays.asList(16, 170, 0), null),
    PINK("�d", Arrays.asList(255, 0, 252), null),
    ORANGE("�6", Arrays.asList(242, 154, 0), null),
    YELLOW("�e", Arrays.asList(242, 239, 0), null),
    BLACK("�0", Arrays.asList(45, 45, 45), null),
    WHITE("�f", Arrays.asList(241, 241, 241), null),
    PURPLE("�5", Arrays.asList(130, 0, 230), null),
    BROWN("�c", Arrays.asList(84, 64, 0), null),
    CYAN("�b", Arrays.asList(0, 255, 255), null),
    LIME("�a", Arrays.asList(0, 255, 0), null);

    private final String color;
    private final List<Integer> rgb;
    private ItemStack head;

    PlayerColor(String color, List<Integer> rgb, ItemStack head) {
        this.color = color;
        this.rgb = rgb;
        this.head = head;
    }

    public String getChatColor() {
        return color;
    }

    private ItemStack getLeatherItem(Material material) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(org.bukkit.Color.fromRGB(rgb.get(0), rgb.get(1), rgb.get(2)));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getHead() {
        return head;
    }

    public void setHead(ItemStack head) {
        this.head = head;
    }

    public ItemStack getChestPlate() {
        return getLeatherItem(Material.LEATHER_CHESTPLATE);
    }

    public ItemStack getLeggings() {
        return getLeatherItem(Material.LEATHER_LEGGINGS);
    }

    public ItemStack getBoots() {
        return getLeatherItem(Material.LEATHER_BOOTS);
    }

}