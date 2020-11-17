package com.abidux.craftus.gui.admin;

import com.abidux.craftus.controllers.InventoryMenuController;
import com.abidux.craftus.enums.InventoryMenuIdentifier;
import com.abidux.craftus.gui.InventoryMenu;
import com.abidux.craftus.models.CraftUsPlayer;
import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminHomeMenu extends InventoryMenu {

    private final ItemStack createMapButton;
    private final ItemStack navigateMapsButton;

    @Inject private InventoryMenuController menuController;

    public AdminHomeMenu() {
        super(InventoryMenuIdentifier.ADMIN_HOME.getUniqueId(), "Admin Menu: Home");

        createMapButton = new ItemStack(Material.ACACIA_BOAT);
        navigateMapsButton = new ItemStack(Material.CHEST);

        putButton(createMapButton, 12);
        putButton(navigateMapsButton, 14);
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onButtonClick(CraftUsPlayer craftUsPlayer, ItemStack button, InventoryClickEvent event) {
        int clickedSlot = event.getSlot();
        Player player = craftUsPlayer.getPlayer();
        if (button.isSimilar(createMapButton)) {
            player.sendMessage(getTitle() + "(" + clickedSlot + "): Create Map");
            event.setCancelled(true);
        }

        if (button.isSimilar(navigateMapsButton)) {
            menuController.openMenuForPlayer(craftUsPlayer, InventoryMenuIdentifier.ADMIN_MAPS);
            event.setCancelled(true);
        }

        player.sendMessage(getClass().getSimpleName() + " unrecognized button (" + clickedSlot + "): " + button);
    }

}
