package com.abidux.craftus.listeners;


import com.abidux.craftus.Main;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.objects.CraftUsProfile;
import com.abidux.craftus.api.objects.tasks.MazeCrewTask;
import com.abidux.craftus.api.objects.tasks.MeteorCrewTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class CloseInv implements Listener {

    private final ArrayList<Player> closed = new ArrayList<>();

    @EventHandler
    void close(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (player == null) {
            return;
        }

        CraftUsProfile profile = Main.profiles.get(player);
        if (profile == null) {
            return;
        }
        if (profile.getTask() != null && profile.getTask() instanceof MeteorCrewTask) {
            ((MeteorCrewTask) profile.getTask()).getInventory().clear();
        }
        if (profile.getTask() != null) {
            profile.setTask(null);
        }
        try {
            if (e.getInventory().getName().equals("�9Create Maze")) {
                MazeCrewTask task = new MazeCrewTask(e.getPlayer().getLocation(), e.getInventory());
                InvClick.tasks.put(task.getStand(), task);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!e.getInventory().getName().equals("�9Voting")) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = (Player) e.getPlayer();
                if (closed.contains(player)) {
                    closed.remove(player);
                    return;
                }
                CraftUsProfile profile = Main.profiles.get(player);
                if (profile == null) {
                    return;
                }
                if (profile.getCurrentMatch() != null) {
                    if (Matches.votingTime.containsKey(profile.getCurrentMatch())) {
                        closed.add(player);
                        e.getPlayer().closeInventory();
                        e.getPlayer().openInventory(Matches.getVoting(profile.getCurrentMatch()));
                    }
                }
            }
        }.runTask(Main.instance);
    }

}