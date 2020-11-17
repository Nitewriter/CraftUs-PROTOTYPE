package com.abidux.craftus.controllers;

import com.abidux.craftus.CraftUs;
import com.abidux.craftus.CraftUsBinderModule;
import com.abidux.craftus.enums.InventoryMenuIdentifier;
import com.abidux.craftus.gui.InventoryMenu;
import com.abidux.craftus.gui.admin.AdminHomeMenu;
import com.abidux.craftus.gui.admin.AdminMapEditMenu;
import com.abidux.craftus.gui.admin.AdminMapTasksMenu;
import com.abidux.craftus.gui.admin.AdminMapsMenu;
import com.abidux.craftus.gui.game.CommsTaskMenu;
import com.abidux.craftus.gui.game.ElectricTaskMenu;
import com.abidux.craftus.gui.game.LevelTaskMenu;
import com.abidux.craftus.gui.game.MazeTaskMenu;
import com.abidux.craftus.gui.game.MeteorTaskMenu;
import com.abidux.craftus.gui.game.WiresTaskMenu;
import com.abidux.craftus.models.CraftUsPlayer;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

@Singleton
public class InventoryMenuController {

    private final CraftUs plugin;
    private final HashMap<UUID, InventoryMenu> openMenus = new HashMap<>();

    @Inject
    public InventoryMenuController(CraftUs plugin) {
        this.plugin = plugin;
    }

    public void openMenuForPlayer(CraftUsPlayer craftUsPlayer, InventoryMenuIdentifier menuIdentifier) {
        InventoryMenu currentMenu = getOpenMenuForPlayer(craftUsPlayer);
        if (currentMenu != null) {
            currentMenu.onClose(craftUsPlayer, null);
        }

        InventoryMenu menu = createMenu(menuIdentifier);
        Player player = craftUsPlayer.getPlayer();
        openMenus.put(player.getUniqueId(), menu);

        Inventory inventory = menu.createInventory();
        player.openInventory(inventory);
    }

    public @Nullable InventoryMenu getOpenMenuForPlayer(CraftUsPlayer craftUsPlayer) {
        Player player = craftUsPlayer.getPlayer();
        return openMenus.get(player.getUniqueId());
    }

    private @NotNull InventoryMenu createMenu(InventoryMenuIdentifier menuIdentifier) {
        Injector injector = CraftUsBinderModule.getDefaultInjector();
        switch (menuIdentifier) {
            case ADMIN_HOME:
                return injector.getInstance(AdminHomeMenu.class);
            case ADMIN_MAPS:
                return injector.getInstance(AdminMapsMenu.class);
            case ADMIN_MAP_EDIT:
                return injector.getInstance(AdminMapEditMenu.class);
            case ADMIN_MAP_TASKS:
                return injector.getInstance(AdminMapTasksMenu.class);
            case GAME_COMMS_TASK:
                return injector.getInstance(CommsTaskMenu.class);
            case GAME_ELECTRIC_TASK:
                return injector.getInstance(ElectricTaskMenu.class);
            case GAME_LEVEL_TASK:
                return injector.getInstance(LevelTaskMenu.class);
            case GAME_MAZE_TASK:
                return injector.getInstance(MazeTaskMenu.class);
            case GAME_METEOR_TASK:
                return injector.getInstance(MeteorTaskMenu.class);
            case GAME_WIRES_TASK:
                return injector.getInstance(WiresTaskMenu.class);
            default:
                throw new IllegalArgumentException(menuIdentifier.name());
        }
    }

    public void closeMenuForPlayer(CraftUsPlayer craftUsPlayer, InventoryCloseEvent event) {
        InventoryView inventoryView = event.getView();
        InventoryMenu menu = getIdentifiedOpenMenuForPlayer(craftUsPlayer, inventoryView);
        if (menu == null) {
            return;
        }

        UUID uuid = craftUsPlayer.getPlayer().getUniqueId();
        menu.onClose(craftUsPlayer, event);
        openMenus.remove(uuid);
    }

    public @Nullable InventoryMenu getIdentifiedOpenMenuForPlayer(CraftUsPlayer craftUsPlayer,
                                                                  InventoryView inventoryView) {
        UUID menuUUID = InventoryMenu.getUniqueId(inventoryView);
        if (menuUUID == null) {
            return null;
        }

        InventoryMenu menu = getOpenMenuForPlayer(craftUsPlayer);
        if (menu == null || !menuUUID.equals(menu.getUuid())) {
            return null;
        }

        return menu;
    }

    public void onInventoryOpened(CraftUsPlayer craftUsPlayer, InventoryOpenEvent event) {
        InventoryView inventoryView = event.getView();
        InventoryMenu menu = getIdentifiedOpenMenuForPlayer(craftUsPlayer, inventoryView);
        if (menu == null) {
            return;
        }

        menu.onOpen(craftUsPlayer, event);
    }

    public void onInventoryButtonClick(CraftUsPlayer craftUsPlayer,
                                       ItemStack button,
                                       InventoryClickEvent event) {
        InventoryView inventoryView = event.getView();
        InventoryMenu menu = getIdentifiedOpenMenuForPlayer(craftUsPlayer, inventoryView);
        if (menu == null) {
            return;
        }

        menu.onButtonClick(craftUsPlayer, button, event);
    }

}
