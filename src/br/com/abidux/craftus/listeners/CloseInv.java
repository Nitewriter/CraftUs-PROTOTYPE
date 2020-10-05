package br.com.abidux.craftus.listeners;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.Matches;
import br.com.abidux.craftus.api.objects.CraftUsProfile;
import br.com.abidux.craftus.api.objects.tasks.MazeTask;
import br.com.abidux.craftus.api.objects.tasks.MeteorTask;

public class CloseInv implements Listener {
	
	private ArrayList<Player> closed = new ArrayList<Player>();
	
	@EventHandler
	void close(InventoryCloseEvent e) {
		CraftUsProfile profile = Main.profiles.get(e.getPlayer());
		if (profile == null) return;
		if (profile.getTask() != null && profile.getTask() instanceof MeteorTask) ((MeteorTask)profile.getTask()).getInventory().clear();
		if (profile.getTask() != null) profile.setTask(null);
		try {
			if (e.getInventory().getName().equals("§9Create Maze")) {
				MazeTask task = new MazeTask(e.getPlayer().getLocation(), e.getInventory());
				InvClick.tasks.put(task.getStand(), task);
			}
		}catch (Exception ex) {}
		if (!e.getInventory().getName().equals("§9Voting")) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				Player player = (Player)e.getPlayer();
				if (closed.contains(player)) {
					closed.remove(player);
					return;
				}
				CraftUsProfile profile = Main.profiles.get(player);
				if (profile == null) return;
				if (profile.getCurrentMatch() != null)
					if (Matches.votingTime.containsKey(profile.getCurrentMatch())) {
						closed.add(player);
						e.getPlayer().closeInventory();
						e.getPlayer().openInventory(Matches.getVoting(profile.getCurrentMatch()));
					}
			}
		}.runTask(Main.instance);
	}
	
}