package com.abidux.craftus.gui.admin;

import com.abidux.craftus.enums.InventoryMenuIdentifier;
import com.abidux.craftus.gui.InventoryMenu;
import com.abidux.craftus.models.CraftUsPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminMapEditMenu extends InventoryMenu {

    public AdminMapEditMenu() {
        super(InventoryMenuIdentifier.ADMIN_MAP_EDIT.getUniqueId(), "Admin Menu: Map Edit");

//        inv.setItem(11, HeadItem.BED.getHead());
//                    inv.setItem(13, getChangeName());
//                    inv.setItem(15, HeadItem.X.getHead());
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onButtonClick(CraftUsPlayer craftUsPlayer, ItemStack button, InventoryClickEvent event) {
        getLogger().info(getClass().getSimpleName() + " not implemented");
    }

}
