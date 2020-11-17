package com.abidux.craftus.listeners;


import com.abidux.craftus.CraftUs;
import com.abidux.craftus.controllers.TeleportController;
import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.models.PlayerSkin;
import com.abidux.craftus.repository.PlayerRepository;
import com.abidux.craftus.repository.PlayerSkinRepository;
import com.abidux.craftus.utils.PlayerSkinApplicator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.craftbukkit.v1_16_R3.CraftOfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

@Singleton
public class PlayerEventListener implements Listener {

    private final CraftUs plugin;
    private final PlayerRepository playerRepository;
    private final PlayerSkinRepository playerSkinRepository;
    private final TeleportController teleportController;

    @Inject
    public PlayerEventListener(CraftUs plugin,
                               PlayerRepository playerRepository,
                               PlayerSkinRepository playerSkinRepository,
                               TeleportController teleportController) {
        this.plugin = plugin;
        this.playerRepository = playerRepository;
        this.playerSkinRepository = playerSkinRepository;
        this.teleportController = teleportController;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerRepository.addPlayer(player);
        teleportController.teleportPlayerToLobby(player);

        PlayerSkin playerSkin = playerSkinRepository.getPlayerSkin(233730070);
        if (playerSkin != null) {
            PlayerSkinApplicator.changePlayerSkin(player, playerSkin);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerRepository.removePlayer(player);
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof ArmorStand) {
            event.setCancelled(true);
            System.out.println(player.getDisplayName() + " right clicked " + entity.getName());

            ArmorStand armorStand = (ArmorStand) entity;
            EntityEquipment equipment = armorStand.getEquipment();
            if (equipment != null) {
                ItemStack helmet = equipment.getHelmet();
                if (helmet != null) {
                    Location location = player.getLocation();
                    player.getWorld().dropItem(location, helmet.clone());

                    System.out.println("ArmorStand is wearing helmet: " + helmet.toString());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack itemStack = event.getItem();
        Block clickedBlock = event.getClickedBlock();

        if (itemStack != null && clickedBlock != null) {
            System.out.println("Player interacted with " + clickedBlock.toString());
            if (clickedBlock.getType() == Material.PLAYER_HEAD) {
                ItemStack drop = clickedBlock.getDrops().iterator().next();
                SkullMeta skullMeta = (SkullMeta) drop.getItemMeta();
                if (skullMeta != null) {
                    try {
                        Field getProfile = skullMeta.getClass().getDeclaredField("profile");
                        getProfile.setAccessible(true);
                        GameProfile attachedProfile = (GameProfile) getProfile.get(skullMeta);
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(attachedProfile.getId());
                        GameProfile profile = ((CraftOfflinePlayer) offlinePlayer).getProfile();

                        PropertyMap properties = profile.getProperties();
                        Collection<Property> textures = properties.get("textures");
                        Collection<Property> names = properties.get("skullNames");
                        Collection<Property> fooBars = properties.get("fooBars");
                        properties.removeAll("fooBars");
                        textures.addAll(names);
                        textures.addAll(fooBars);

                        for (Property property : textures) {
                            System.out.println("Property (" + property.getName() + "): " + property.getValue());
                        }

                        properties.put("fooBars", new Property("fooBars", "ITWORKS"));
                        Method setProfile = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                        setProfile.setAccessible(true);
                        setProfile.invoke(skullMeta, profile);
                        drop.setItemMeta(skullMeta);

                        Rotatable rotatable = (Rotatable) drop.getType().createBlockData();
                        rotatable.setRotation(BlockFace.WEST_NORTH_WEST);
                        Block originalBlock = clickedBlock.getLocation().getBlock();
                        originalBlock.setBlockData(rotatable);
                        originalBlock.getLocation().setYaw(event.getPlayer().getLocation().getYaw());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler
    void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        CraftUsPlayer craftUsPlayer = playerRepository.getPlayerForId(event.getPlayer().getUniqueId());
        if (craftUsPlayer == null) {
            return;
        }
        if (craftUsPlayer.getCurrentMatch() == null) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

    }

//    @SuppressWarnings("deprecation")
//    @EventHandler
//    void click(PlayerInteractAtEntityEvent e) {
//        CraftUsProfile profile = Main.profiles.get(e.getPlayer());
//        if (profile.getCurrentMatch() != null) {
//            e.setCancelled(true);
//            if (e.getRightClicked() instanceof ArmorStand) {
//                ArmorStand as = (ArmorStand) e.getRightClicked();
//                if (InvClick.tasks.containsKey(as)) {
//                    CrewTask primaryCrewTask = InvClick.tasks.get(as);
//                    if (primaryCrewTask instanceof ElectricCrewTask) {
//                        boolean hasTask = false;
//                        for (CrewTask crewTask : profile.crewTasks) {
//                            if (crewTask instanceof ElectricCrewTask) {
//                                hasTask = true;
//                                break;
//                            }
//                        }
//                        if (!hasTask) {
//                            Main.actionbar(e.getPlayer(), "§cX");
//                            return;
//                        }
//                        ElectricCrewTask task = (ElectricCrewTask) primaryCrewTask;
//                        task.openTask(e.getPlayer());
//                        profile.setTask(task);
//                    } else if (primaryCrewTask instanceof MazeCrewTask) {
//                        boolean hasTask = false;
//                        for (CrewTask crewTask : profile.crewTasks) {
//                            if (crewTask instanceof MazeCrewTask) {
//                                hasTask = true;
//                                break;
//                            }
//                        }
//                        if (!hasTask) {
//                            Main.actionbar(e.getPlayer(), "§cX");
//                            return;
//                        }
//                        MazeCrewTask task = (MazeCrewTask) primaryCrewTask;
//                        task.openTask(e.getPlayer());
//                        profile.setTask(task);
//                    } else if (primaryCrewTask instanceof MeteorCrewTask) {
//                        boolean hasTask = false;
//                        for (CrewTask crewTask : profile.crewTasks) {
//                            if (crewTask instanceof MeteorCrewTask) {
//                                hasTask = true;
//                                break;
//                            }
//                        }
//                        if (!hasTask) {
//                            Main.actionbar(e.getPlayer(), "§cX");
//                            return;
//                        }
//                        MeteorCrewTask task = (MeteorCrewTask) primaryCrewTask;
//                        task.openTask(e.getPlayer());
//                        profile.setTask(task);
//                    } else if (primaryCrewTask instanceof CommsCrewTask) {
//                        boolean hasTask = false;
//                        for (CrewTask crewTask : profile.crewTasks) {
//                            if (crewTask instanceof CommsCrewTask) {
//                                hasTask = true;
//                                break;
//                            }
//                        }
//                        if (!hasTask) {
//                            Main.actionbar(e.getPlayer(), "§cX");
//                            return;
//                        }
//                        CommsCrewTask task = (CommsCrewTask) primaryCrewTask;
//                        task.openTask(e.getPlayer());
//                        profile.setTask(task);
//                    } else if (primaryCrewTask instanceof WiresCrewTask) {
//                        boolean hasTask = false;
//                        for (CrewTask crewTask : profile.crewTasks) {
//                            if (crewTask instanceof WiresCrewTask) {
//                                hasTask = true;
//                                break;
//                            }
//                        }
//                        if (!hasTask) {
//                            Main.actionbar(e.getPlayer(), "§cX");
//                            return;
//                        }
//                        WiresCrewTask task = (WiresCrewTask) primaryCrewTask;
//                        task.openTask(e.getPlayer());
//                        profile.setTask(task);
//                    } else if (primaryCrewTask instanceof LevelCrewTask) {
//                        boolean hasTask = false;
//                        for (CrewTask crewTask : profile.crewTasks) {
//                            if (crewTask instanceof LevelCrewTask) {
//                                hasTask = true;
//                                break;
//                            }
//                        }
//                        if (!hasTask) {
//                            Main.actionbar(e.getPlayer(), "§cX");
//                            return;
//                        }
//                        LevelCrewTask task = (LevelCrewTask) primaryCrewTask;
//                        task.openTask(e.getPlayer());
//                        profile.setTask(task);
//                    }
//                } else if (CraftUs.buttons.containsKey(as)) {
//                    MeetingButton button = CraftUs.buttons.get(as);
//                    if (button.coolDown <= 0) {
//                        button.coolDown = 195;
//                        profile.getCurrentMatch().getPlayers().forEach(player -> {
//                            player.sendTitle("§cBODY REPORTED", "§c" + e.getPlayer().getName());
//                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400 * 20, 250));
//                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 250));
//                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400 * 20, 1));
//                            player.teleport(profile.getCurrentMatch().getSpawn());
//                        });
//                        Matches.discussion.add(profile.getCurrentMatch());
//                        Matches.discussionTime.put(profile.getCurrentMatch(), 30);
//                    } else {
//                        Main.actionbar(e.getPlayer(), "§cX");
//                    }
//                } else {
//                    System.out.println("Unknown armor stand clicked by " + e.getPlayer().getDisplayName());
//                    return;
//                }
//
//                if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
//                    return;
//                }
//
//                for (int i = 0; i < profile.getCurrentMatch().deadPlayers.size(); i++) {
//                    DeadPlayer deadPlayer = profile.getCurrentMatch().deadPlayers.get(i);
//                    if (as.equals(deadPlayer.getBody()) || as.equals(deadPlayer.getStands().get(0))) {
//                        for (int i2 = 0; i2 < profile.getCurrentMatch().deadPlayers.size(); i2++) {
//                            DeadPlayer dp = profile.getCurrentMatch().deadPlayers.get(i2);
//                            dp.getBody().remove();
//                            dp.getStands().forEach(Entity::remove);
//                        }
//                        profile.getCurrentMatch().getPlayers().forEach(player -> {
//                            player.sendTitle("§cBODY REPORTED", "§c" + e.getPlayer().getName());
//                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 86400 * 20, 250));
//                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 250));
//                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 86400 * 20, 1));
//                            player.teleport(profile.getCurrentMatch().getSpawn());
//                        });
//                        Matches.discussion.add(profile.getCurrentMatch());
//                        Matches.discussionTime.put(profile.getCurrentMatch(), 90);
//                        break;
//                    }
//                    profile.getCurrentMatch().deadPlayers.remove(deadPlayer);
//                }
//            }
//        }
//    }

}