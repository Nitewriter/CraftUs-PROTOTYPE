package com.abidux.craftus.api.objects.tasks;

import com.abidux.craftus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Button {

    public static Material isToggledMaterial = Material.GREEN_STAINED_GLASS_PANE;
    public static Material isNotToggledMaterial = Material.RED_STAINED_GLASS_PANE;

    private boolean toggled = false;

    public Button(HashMap<Integer, Button> buttonsMap, int slot) {
        buttonsMap.put(slot, this);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getButton() {
        Material material = toggled ? isToggledMaterial : isNotToggledMaterial;
        return new ItemBuilder().type(material).name("�7").build();
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

}