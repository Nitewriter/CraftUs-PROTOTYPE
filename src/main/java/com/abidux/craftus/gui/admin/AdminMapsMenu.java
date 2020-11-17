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

public class AdminMapsMenu extends InventoryMenu {

    private final ItemStack homeButton;
    @Inject private InventoryMenuController menuController;

    public AdminMapsMenu() {
        super(InventoryMenuIdentifier.ADMIN_MAPS.getUniqueId(), "Admin Menu: Maps");

        homeButton = new ItemStack(Material.GOLDEN_CARROT);
        putButton(homeButton, 18);

        //        Inventory inv = Bukkit.createInventory(null, 6 * 9, DisplayMessage.PREFIX.toString() + "- Maps");
//        int page = this.page.get(player);
//        for (int i = 0; i < 45; i++) {
//            int index = (45 * page) + i;
//            if (!(GameMaps.maps.values().size() > index)) {
//                break;
//            }
//            GameMap gameMap = (GameMap) GameMaps.maps.values().toArray()[index];
//            inv.setItem(i, GameMaps.getItem(gameMap));
//        }
//        if (page > 0) {
//            inv.setItem(45, getPrevious());
//        }
//        if (GameMaps.maps.values().size() > (page + 1) * 45) {
//            inv.setItem(53, getNext());
//        }
        //    @SuppressWarnings("deprecation")
//    private ItemStack getNext() {
//        ItemStack next = new ItemStack(Material.LIME_CARPET, 1, DyeColor.LIME.getWoolData());
//        ItemMeta nextMeta = next.getItemMeta();
//        nextMeta.setDisplayName(DisplayMessage.NEXT.toString());
//        next.setItemMeta(nextMeta);
//        return next;
//    }
//
//    @SuppressWarnings("deprecation")
//    private ItemStack getPrevious() {
//        ItemStack previous = new ItemStack(Material.RED_CARPET, 1, DyeColor.RED.getWoolData());
//        ItemMeta previousMeta = previous.getItemMeta();
//        previousMeta.setDisplayName(DisplayMessage.PREVIOUS.toString());
//        previous.setItemMeta(previousMeta);
//        return previous;
//    }
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onButtonClick(CraftUsPlayer craftUsPlayer, ItemStack button, InventoryClickEvent event) {
        Player player = craftUsPlayer.getPlayer();
        if (button.isSimilar(homeButton)) {
            menuController.openMenuForPlayer(craftUsPlayer, InventoryMenuIdentifier.ADMIN_HOME);
            event.setCancelled(true);
        }

        player.sendMessage(getClass().getSimpleName() + " unrecognized button (" + event.getSlot() + "): " + button);
    }

}
