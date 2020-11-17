package com.abidux.craftus.gui.game;

import com.abidux.craftus.enums.InventoryMenuIdentifier;
import com.abidux.craftus.gui.InventoryMenu;
import com.abidux.craftus.models.CraftUsPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MeteorTaskMenu extends InventoryMenu {

    public MeteorTaskMenu() {
        super(InventoryMenuIdentifier.GAME_METEOR_TASK.getUniqueId(), "Meteor Task");
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override public void onButtonClick(CraftUsPlayer craftUsPlayer, ItemStack button, InventoryClickEvent event) {
        getLogger().info(getClass().getSimpleName() + " not implemented");
    }

}
