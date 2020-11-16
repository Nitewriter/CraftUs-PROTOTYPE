package com.abidux.craftus.listeners;


import com.abidux.craftus.Main;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.PlayerType;
import com.abidux.craftus.api.objects.CraftUsProfile;
import com.abidux.craftus.api.objects.DeadPlayer;
import com.abidux.craftus.api.objects.Match;
import com.abidux.craftus.api.objects.tasks.*;
import com.abidux.craftus.commands.CraftUs;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteract implements Listener {

    @EventHandler
    void interact(EntityDamageByEntityEvent e) {
        e.setCancelled(true);

        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        if (player == null || damager == null) {
            return;
        }

        CraftUsProfile playerProfile = Main.profiles.get(player);
        Match playerMatch = playerProfile.getCurrentMatch();
        CraftUsProfile damagerProfile = Main.profiles.get(damager);
        Match damagerMatch = damagerProfile.getCurrentMatch();

        if (playerMatch == null || damagerMatch == null) {
            return;
        }

        if (damager.getItemInHand().equals(Matches.getImpostorSword()) && !(playerProfile.getType().equals(PlayerType.IMPOSTOR) || playerProfile.getType().equals(PlayerType.DEAD_CREWMATE) || playerProfile.getType().equals(PlayerType.DEAD_IMPOSTOR))) {
            Matches.killPlayer(playerProfile);
        }

        if (damagerMatch.getAliveCrewmates().isEmpty()) {
            damagerMatch.winner("�cImpostor");
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
                    CrewTask primaryCrewTask = InvClick.tasks.get(as);
                    if (primaryCrewTask instanceof ElectricCrewTask) {
                        boolean hasTask = false;
                        for (CrewTask crewTask : profile.crewTasks) {
                            if (crewTask instanceof ElectricCrewTask) {
                                hasTask = true;
                                break;
                            }
                        }
                        if (!hasTask) {
                            Main.actionbar(e.getPlayer(), "�cX");
                            return;
                        }
                        ElectricCrewTask task = (ElectricCrewTask) primaryCrewTask;
                        task.openTask(e.getPlayer());
                        profile.setTask(task);
                    } else if (primaryCrewTask instanceof MazeCrewTask) {
                        boolean hasTask = false;
                        for (CrewTask crewTask : profile.crewTasks) {
                            if (crewTask instanceof MazeCrewTask) {
                                hasTask = true;
                                break;
                            }
                        }
                        if (!hasTask) {
                            Main.actionbar(e.getPlayer(), "�cX");
                            return;
                        }
                        MazeCrewTask task = (MazeCrewTask) primaryCrewTask;
                        task.openTask(e.getPlayer());
                        profile.setTask(task);
                    } else if (primaryCrewTask instanceof MeteorCrewTask) {
                        boolean hasTask = false;
                        for (CrewTask crewTask : profile.crewTasks) {
                            if (crewTask instanceof MeteorCrewTask) {
                                hasTask = true;
                                break;
                            }
                        }
                        if (!hasTask) {
                            Main.actionbar(e.getPlayer(), "�cX");
                            return;
                        }
                        MeteorCrewTask task = (MeteorCrewTask) primaryCrewTask;
                        task.openTask(e.getPlayer());
                        profile.setTask(task);
                    } else if (primaryCrewTask instanceof CommsCrewTask) {
                        boolean hasTask = false;
                        for (CrewTask crewTask : profile.crewTasks) {
                            if (crewTask instanceof CommsCrewTask) {
                                hasTask = true;
                                break;
                            }
                        }
                        if (!hasTask) {
                            Main.actionbar(e.getPlayer(), "�cX");
                            return;
                        }
                        CommsCrewTask task = (CommsCrewTask) primaryCrewTask;
                        task.openTask(e.getPlayer());
                        profile.setTask(task);
                    } else if (primaryCrewTask instanceof WiresCrewTask) {
                        boolean hasTask = false;
                        for (CrewTask crewTask : profile.crewTasks) {
                            if (crewTask instanceof WiresCrewTask) {
                                hasTask = true;
                                break;
                            }
                        }
                        if (!hasTask) {
                            Main.actionbar(e.getPlayer(), "�cX");
                            return;
                        }
                        WiresCrewTask task = (WiresCrewTask) primaryCrewTask;
                        task.openTask(e.getPlayer());
                        profile.setTask(task);
                    } else if (primaryCrewTask instanceof LevelCrewTask) {
                        boolean hasTask = false;
                        for (CrewTask crewTask : profile.crewTasks) {
                            if (crewTask instanceof LevelCrewTask) {
                                hasTask = true;
                                break;
                            }
                        }
                        if (!hasTask) {
                            Main.actionbar(e.getPlayer(), "�cX");
                            return;
                        }
                        LevelCrewTask task = (LevelCrewTask) primaryCrewTask;
                        task.openTask(e.getPlayer());
                        profile.setTask(task);
                    }
                } else if (CraftUs.buttons.containsKey(as)) {
                    MeetingButton button = CraftUs.buttons.get(as);
                    if (button.coolDown <= 0) {
                        button.coolDown = 195;
                        profile.getCurrentMatch().getPlayers().forEach(player -> {
                            player.sendTitle("�cBODY REPORTED", "�c" + e.getPlayer().getName());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400 * 20, 250));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 250));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400 * 20, 1));
                            player.teleport(profile.getCurrentMatch().getSpawn());
                        });
                        Matches.discussion.add(profile.getCurrentMatch());
                        Matches.discussionTime.put(profile.getCurrentMatch(), 30);
                    } else {
                        Main.actionbar(e.getPlayer(), "�cX");
                    }
                } else {
                    System.out.println("Unknown armor stand clicked by " + e.getPlayer().getDisplayName());
                    return;
                }

                if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                    return;
                }

                for (int i = 0; i < profile.getCurrentMatch().deadPlayers.size(); i++) {
                    DeadPlayer deadPlayer = profile.getCurrentMatch().deadPlayers.get(i);
                    if (as.equals(deadPlayer.getBody()) || as.equals(deadPlayer.getStands().get(0))) {
                        for (int i2 = 0; i2 < profile.getCurrentMatch().deadPlayers.size(); i2++) {
                            DeadPlayer dp = profile.getCurrentMatch().deadPlayers.get(i2);
                            dp.getBody().remove();
                            dp.getStands().forEach(Entity::remove);
                        }
                        profile.getCurrentMatch().getPlayers().forEach(player -> {
                            player.sendTitle("�cBODY REPORTED", "�c" + e.getPlayer().getName());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400 * 20, 250));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 250));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400 * 20, 1));
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