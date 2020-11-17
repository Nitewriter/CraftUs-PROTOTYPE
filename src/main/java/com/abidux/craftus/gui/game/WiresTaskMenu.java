package com.abidux.craftus.gui.game;

import com.abidux.craftus.enums.InventoryMenuIdentifier;
import com.abidux.craftus.gui.InventoryMenu;
import com.abidux.craftus.models.CraftUsPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WiresTaskMenu extends InventoryMenu {

    public WiresTaskMenu() {
        super(InventoryMenuIdentifier.GAME_WIRES_TASK.getUniqueId(), "Wires Task");
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override public void onButtonClick(CraftUsPlayer craftUsPlayer, ItemStack button, InventoryClickEvent event) {
        getLogger().info(getClass().getSimpleName() + " not implemented");
    }

}