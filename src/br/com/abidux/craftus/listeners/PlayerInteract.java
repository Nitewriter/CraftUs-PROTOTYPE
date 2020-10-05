package br.com.abidux.craftus.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.Matches;
import br.com.abidux.craftus.api.PlayerType;
import br.com.abidux.craftus.api.objects.CraftUsProfile;
import br.com.abidux.craftus.api.objects.DeadPlayer;
import br.com.abidux.craftus.api.objects.tasks.CommsTask;
import br.com.abidux.craftus.api.objects.tasks.ElectricTask;
import br.com.abidux.craftus.api.objects.tasks.LevelTask;
import br.com.abidux.craftus.api.objects.tasks.MazeTask;
import br.com.abidux.craftus.api.objects.tasks.MeetingButton;
import br.com.abidux.craftus.api.objects.tasks.MeteorTask;
import br.com.abidux.craftus.api.objects.tasks.Task;
import br.com.abidux.craftus.api.objects.tasks.WiresTask;
import br.com.abidux.craftus.commands.CraftUs;

public class PlayerInteract implements Listener {
	
	@EventHandler
	void interact(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			CraftUsProfile player = Main.profiles.get((Player)e.getEntity());
			CraftUsProfile damager = Main.profiles.get((Player)e.getDamager());
			if (player.getCurrentMatch() != null || damager.getCurrentMatch() != null) e.setCancelled(true);
			if (damager.getPlayer().getItemInHand().equals(Matches.getImpostorSword()) && !(player.getType().equals(PlayerType.IMPOSTOR) || player.getType().equals(PlayerType.DEAD_CREWMATE) || player.getType().equals(PlayerType.DEAD_IMPOSTOR)))
				Matches.killPlayer(player);
			if (damager.getCurrentMatch().getAliveCrewmates().isEmpty()) {
				damager.getCurrentMatch().winner("§cImpostor");
				return;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	void click(PlayerInteractAtEntityEvent e) {
		CraftUsProfile profile = Main.profiles.get(e.getPlayer());
		if (profile.getCurrentMatch() != null) {
			e.setCancelled(true);
			if (e.getRightClicked() instanceof ArmorStand) {
				ArmorStand as = (ArmorStand) e.getRightClicked();
				if (InvClick.tasks.containsKey(as)) {
					Task primaryTask = InvClick.tasks.get(as);
					if (primaryTask instanceof ElectricTask) {
						boolean hasTask = false;
						for (Task task : profile.tasks) {
							if (task instanceof ElectricTask) {
								hasTask = true;
								break;
							}
						}
						if (!hasTask) {
							Main.actionbar(e.getPlayer(), "§cX");
							return;
						}
						ElectricTask task = (ElectricTask)primaryTask;
						task.openTask(e.getPlayer());
						profile.setTask(task);
					}else if (primaryTask instanceof MazeTask) {
						boolean hasTask = false;
						for (Task task : profile.tasks) {
							if (task instanceof MazeTask) {
								hasTask = true;
								break;
							}
						}
						if (!hasTask) {
							Main.actionbar(e.getPlayer(), "§cX");
							return;
						}
						MazeTask task = (MazeTask)primaryTask;
						task.openTask(e.getPlayer());
						profile.setTask(task);
					}else if (primaryTask instanceof MeteorTask) {
						boolean hasTask = false;
						for (Task task : profile.tasks) {
							if (task instanceof MeteorTask) {
								hasTask = true;
								break;
							}
						}
						if (!hasTask) {
							Main.actionbar(e.getPlayer(), "§cX");
							return;
						}
						MeteorTask task = (MeteorTask)primaryTask;
						task.openTask(e.getPlayer());
						profile.setTask(task);
					}else if (primaryTask instanceof CommsTask) {
						boolean hasTask = false;
						for (Task task : profile.tasks) {
							if (task instanceof CommsTask) {
								hasTask = true;
								break;
							}
						}
						if (!hasTask) {
							Main.actionbar(e.getPlayer(), "§cX");
							return;
						}
						CommsTask task = (CommsTask)primaryTask;
						task.openTask(e.getPlayer());
						profile.setTask(task);
					}else if (primaryTask instanceof WiresTask) {
						boolean hasTask = false;
						for (Task task : profile.tasks) {
							if (task instanceof WiresTask) {
								hasTask = true;
								break;
							}
						}
						if (!hasTask) {
							Main.actionbar(e.getPlayer(), "§cX");
							return;
						}
						WiresTask task = (WiresTask)primaryTask;
						task.openTask(e.getPlayer());
						profile.setTask(task);
					}else if (primaryTask instanceof LevelTask) {
						boolean hasTask = false;
						for (Task task : profile.tasks) {
							if (task instanceof LevelTask) {
								hasTask = true;
								break;
							}
						}
						if (!hasTask) {
							Main.actionbar(e.getPlayer(), "§cX");
							return;
						}
						LevelTask task = (LevelTask)primaryTask;
						task.openTask(e.getPlayer());
						profile.setTask(task);
					}
				}else if (CraftUs.buttons.containsKey(as)) {
					MeetingButton button = CraftUs.buttons.get(as);
					if (button.cooldown <= 0) {
						button.cooldown = 195;
						profile.getCurrentMatch().getPlayers().stream().forEach(player -> {
							player.sendTitle("§cBODY REPORTED", "§c"+e.getPlayer().getName());
							player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400*20, 250));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400*20, 250));
							player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400*20, 1));
							player.teleport(profile.getCurrentMatch().getSpawn());
						});
						Matches.discussion.add(profile.getCurrentMatch());
						Matches.discussionTime.put(profile.getCurrentMatch(), 90);
					}else Main.actionbar(e.getPlayer(), "§cX");
				}
				if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) return;
				for (int i = 0; i < profile.getCurrentMatch().deadPlayers.size(); i++) {
					DeadPlayer deadPlayer = profile.getCurrentMatch().deadPlayers.get(i);
					if (as.equals(deadPlayer.getBody()) || as.equals(deadPlayer.getStands().get(0))) {
						for (int i2 = 0; i2 < profile.getCurrentMatch().deadPlayers.size(); i2++) {
							DeadPlayer dp = profile.getCurrentMatch().deadPlayers.get(i2);
							dp.getBody().remove();
							dp.getStands().stream().forEach(stand -> stand.remove());
						}
						profile.getCurrentMatch().getPlayers().stream().forEach(player -> {
							player.sendTitle("§cBODY REPORTED", "§c"+e.getPlayer().getName());
							player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400*20, 250));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400*20, 250));
							player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400*20, 1));
							player.teleport(profile.getCurrentMatch().getSpawn());
						});
						Matches.discussion.add(profile.getCurrentMatch());
						Matches.discussionTime.put(profile.getCurrentMatch(), 90);
						break;
					}
					profile.getCurrentMatch().deadPlayers.remove(deadPlayer);
				}
			}
		}
	}
	
}