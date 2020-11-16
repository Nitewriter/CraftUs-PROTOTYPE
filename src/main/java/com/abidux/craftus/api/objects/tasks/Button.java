package com.abidux.craftus.api.objects.tasks;

import com.abidux.craftus.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Button {

    private boolean toggled = false;

    public Button(HashMap<Integer, Button> buttonsMap, int slot) {
        buttonsMap.put(slot, this);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getButton() {
        return new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(toggled ? DyeColor.LIME.getWoolData() : DyeColor.RED.getWoolData()).name("�7").build();
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

}