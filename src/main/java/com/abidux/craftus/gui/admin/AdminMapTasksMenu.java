package com.abidux.craftus.gui.admin;

import com.abidux.craftus.enums.InventoryMenuIdentifier;
import com.abidux.craftus.gui.InventoryMenu;
import com.abidux.craftus.models.CraftUsPlayer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AdminMapTasksMenu extends InventoryMenu {

    public AdminMapTasksMenu() {
        super(InventoryMenuIdentifier.ADMIN_MAP_TASKS.getUniqueId(), "Admin Menu: Map Tasks");

        ItemStack placeHolder = new ItemStack(Material.PLAYER_HEAD);
        putButton(placeHolder, ButtonSlot.ELECTRIC.getSlot());
        putButton(placeHolder, ButtonSlot.MAZE.getSlot());
        putButton(placeHolder, ButtonSlot.METEORITE.getSlot());
        putButton(placeHolder, ButtonSlot.RADIO.getSlot());
        putButton(placeHolder, ButtonSlot.MYSTERY_BOX.getSlot());
        putButton(placeHolder, ButtonSlot.WAVE.getSlot());
    }

    @Override
    public int getRows() {
        return 4;
    }

    @Override
    public void onButtonClick(CraftUsPlayer craftUsPlayer, ItemStack button, InventoryClickEvent event) {
        ButtonSlot buttonSlot = ButtonSlot.fromSlot(event.getSlot());
        if (buttonSlot == null) {
            return;
        }

        event.setCancelled(true);
        String message = "Map Task Menu: ";
        switch (buttonSlot) {
            case MAZE:
                message += "Maze";
                break;
            case WAVE:
                message += "Wave";
                break;
            case RADIO:
                message += "Radio";
                break;
            case ELECTRIC:
                message += "Electric";
                break;
            case METEORITE:
                message += "Meteorite";
                break;
            case MYSTERY_BOX:
                message += "Mystery Box";
                break;
        }

        Player player = craftUsPlayer.getPlayer();
        player.sendMessage(message);
    }

    private enum ButtonSlot {
        ELECTRIC(12),
        MAZE(13),
        METEORITE(14),
        RADIO(21),
        MYSTERY_BOX(22),
        WAVE(23);

        @Getter private final int slot;

        ButtonSlot(int slot) {
            this.slot = slot;
        }

        static @Nullable ButtonSlot fromSlot(int slot) {
            switch (slot) {
                case 12:
                    return ELECTRIC;
                case 13:
                    return MAZE;
                case 14:
                    return METEORITE;
                case 21:
                    return RADIO;
                case 22:
                    return MYSTERY_BOX;
                case 23:
                    return WAVE;
                default:
                    return null;
            }
        }
    }

}
