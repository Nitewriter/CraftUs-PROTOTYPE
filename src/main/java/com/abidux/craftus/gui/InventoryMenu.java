package com.abidux.craftus.gui;

import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.utils.TextColorize;
import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class InventoryMenu {

    @Getter
    private final UUID uuid;

    @Getter
    private final String title;

    @Getter(AccessLevel.PROTECTED)
    private final HashMap<Integer, ItemStack> buttons = new HashMap<>();

    @Getter(AccessLevel.PROTECTED) @Inject Logger logger;

    public InventoryMenu(UUID uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }

    public static @Nullable UUID getUniqueId(InventoryView inventoryView) {
        String title = inventoryView.getTitle();
        String identityDelimiter = TextColorize.toInvisibleString("UUID::");
        String[] components = title.split(identityDelimiter);
        if (components.length < 2) {
            return null;
        }

        try {
            String identifier = TextColorize.toVisibleString(components[1]);
            return UUID.fromString(identifier);
        } catch (IllegalArgumentException exception) {
            String message = InventoryMenu.class.getSimpleName() + ".getUniqueId: " + exception.getLocalizedMessage();
            System.out.println(message);
            return null;
        }
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, getSize(), getIdentifiableTitle());
        for (Map.Entry<Integer, ItemStack> button : buttons.entrySet()) {
            inventory.setItem(button.getKey(), button.getValue());
        }

        return inventory;
    }

    protected int getSize() {
        return getRows() * 9;
    }

    protected String getIdentifiableTitle() {
        return title + TextColorize.toInvisibleString("UUID::" + uuid.toString());
    }

    abstract public int getRows();

    protected void putButton(ItemStack button, int inSlot) {
        buttons.put(inSlot, button);
    }

    public void onOpen(CraftUsPlayer craftUsPlayer, InventoryOpenEvent event) {
        logger.info(getClass().getSimpleName() + " opened");

        Player player = craftUsPlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
    }

    public void onClose(CraftUsPlayer craftUsPlayer, @Nullable InventoryCloseEvent event) {
        logger.info(getClass().getSimpleName() + " closed");

        Player player = craftUsPlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0.5f);
    }

    abstract public void onButtonClick(CraftUsPlayer craftUsPlayer, ItemStack button, InventoryClickEvent event);

}
