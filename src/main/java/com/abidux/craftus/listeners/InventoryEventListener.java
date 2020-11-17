package com.abidux.craftus.listeners;


import com.abidux.craftus.CraftUs;
import com.abidux.craftus.controllers.InventoryMenuController;
import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.repository.PlayerRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

@Singleton
public class InventoryEventListener implements Listener {

    private final CraftUs plugin;
    private final InventoryMenuController menuController;
    private final PlayerRepository playerRepository;

    @Inject
    public InventoryEventListener(CraftUs plugin,
                                  PlayerRepository playerRepository,
                                  InventoryMenuController menuController) {
        this.plugin = plugin;
        this.playerRepository = playerRepository;
        this.menuController = menuController;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void onInventoryClickEvent(InventoryClickEvent event) {
        CraftUsPlayer craftUsPlayer = playerRepository.getPlayerForId(event.getWhoClicked().getUniqueId());
        if (craftUsPlayer == null) {
            return;
        }

        ItemStack button = event.getCurrentItem();
        if (button == null) {
            return;
        }

        menuController.onInventoryButtonClick(craftUsPlayer, button, event);
    }

    @EventHandler
    void onInventoryOpenEvent(InventoryOpenEvent event) {
        CraftUsPlayer craftUsPlayer = playerRepository.getPlayerForId(event.getPlayer().getUniqueId());
        if (craftUsPlayer == null) {
            return;
        }

        menuController.onInventoryOpened(craftUsPlayer, event);
    }

    @EventHandler
    void onInventoryCloseEvent(InventoryCloseEvent event) {
        CraftUsPlayer craftUsPlayer = playerRepository.getPlayerForId(event.getPlayer().getUniqueId());
        if (craftUsPlayer == null) {
            return;
        }

        menuController.closeMenuForPlayer(craftUsPlayer, event);

//        Player player = (Player) e.getPlayer();
//        if (player == null) {
//            return;
//        }
//
//        CraftUsProfile profile = CraftUs.profiles.get(player);
//        if (profile == null) {
//            return;
//        }
//        if (profile.getTask() != null && profile.getTask() instanceof MeteorCrewTask) {
//            ((MeteorCrewTask) profile.getTask()).getInventory().clear();
//        }
//        if (profile.getTask() != null) {
//            profile.setTask(null);
//        }
//        try {
//            if (e.getView().getTitle().equals("§9Create Maze")) {
//                MazeCrewTask task = new MazeCrewTask(e.getPlayer().getLocation(), e.getInventory());
//                InvClick.tasks.put(task.getStand(), task);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        if (!e.getView().getTitle().equals("§9Voting")) {
//            return;
//        }
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                Player player = (Player) e.getPlayer();
//                if (closed.contains(player)) {
//                    closed.remove(player);
//                    return;
//                }
//                CraftUsProfile profile = CraftUs.profiles.get(player);
//                if (profile == null) {
//                    return;
//                }
//                if (profile.getCurrentMatch() != null) {
//                    if (Matches.votingTime.containsKey(profile.getCurrentMatch())) {
//                        closed.add(player);
//                        e.getPlayer().closeInventory();
//                        e.getPlayer().openInventory(Matches.getVoting(profile.getCurrentMatch()));
//                    }
//                }
//            }
//        }.runTask(CraftUs.instance);
    }

}