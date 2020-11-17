//package com.abidux.craftus.listeners;
//
//
//import com.abidux.craftus.CraftUs;
//import com.abidux.craftus.api.Matches;
//import com.abidux.craftus.controllers.GameMaps;
//import com.abidux.craftus.enums.DisplayMessage;
//import com.abidux.craftus.enums.HeadItem;
//import com.abidux.craftus.game.CraftUsProfile;
//import com.abidux.craftus.game.GameMap;
//import com.abidux.craftus.game.items.Button;
//import com.abidux.craftus.game.tasks.*;
//import org.bukkit.Bukkit;
//import org.bukkit.DyeColor;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.entity.ArmorStand;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.function.Consumer;
//import java.util.stream.Collectors;
//
//
//public class InvClick implements Listener {
//
//    public static HashMap<ArmorStand, CrewTask> tasks = new HashMap<>();
//    public static HashMap<ItemStack, Consumer<Player>> items = new HashMap<>();
//    public static HashMap<Player, GameMap> editing = new HashMap<>();
//    private final HashMap<Player, Integer> page = new HashMap<>();
//
//    @SuppressWarnings("deprecation")
//    @EventHandler
//    void click(InventoryClickEvent e) {
//        if (e.getCurrentItem() == null) {
//            return;
//        }
//
//        try {
//            Player player = (Player) e.getWhoClicked();
//            CraftUsProfile profile = CraftUs.profiles.get(player);
//            if (profile.getCurrentMatch() != null && e.getClickedInventory().equals(player.getInventory())) {
//                e.setCancelled(true);
//                return;
//            }
//            if (e.getView().getTitle().equals(DisplayMessage.PREFIX.toString())) {
//                e.setCancelled(true);
//                if (e.getCurrentItem().equals(HeadItem.PLUS.getHead())) {
//                    e.getWhoClicked().closeInventory();
//                    Chat.players.add(player);
//                    e.getWhoClicked().sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.SEND_NAME);
//                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
//                } else if (e.getCurrentItem().equals(HeadItem.BOOK.getHead())) {
//                    page.put(player, 0);
//                    openSearch(player);
//                }
//            } else if (e.getView().getTitle().equals(DisplayMessage.PREFIX.toString() + "- Maps")) {
//                e.setCancelled(true);
//                if (e.getCurrentItem().equals(getPrevious())) {
//                    page.put(player, page.get(player) - 1);
//                    openSearch(player);
//                } else if (e.getCurrentItem().equals(getNext())) {
//                    page.put(player, page.get(player) + 1);
//                    openSearch(player);
//                } else if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
//                    String displayName = e.getCurrentItem().getItemMeta().getDisplayName();
//                    String mapName = displayName.substring(2, displayName.length()).toLowerCase();
//                    Inventory inv = Bukkit.createInventory(
//                            null, 3 * 9, DisplayMessage.PREFIX.toString() + DisplayMessage.EDIT_MAP);
//                    inv.setItem(11, HeadItem.BED.getHead());
//                    inv.setItem(13, getChangeName());
//                    inv.setItem(15, HeadItem.X.getHead());
//                    editing.put(player, GameMaps.maps.get(mapName));
//                    player.openInventory(inv);
//                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
//                }
//            } else if (e.getView().getTitle().equals(DisplayMessage.PREFIX.toString() + DisplayMessage.EDIT_MAP)) {
//                if (e.getCurrentItem().equals(HeadItem.BED.getHead())) {
//                    editing.get(player).setSpawn(player.getLocation());
//                    player.closeInventory();
//                    editing.remove(player);
//                    player.sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.MODIFIED);
//                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//                } else if (e.getCurrentItem().equals(HeadItem.X.getHead())) {
//                    GameMaps.maps.remove(editing.get(player).getName().toLowerCase());
//                    player.closeInventory();
//                    player.sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.MODIFIED);
//                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//                } else if (e.getCurrentItem().equals(getChangeName())) {
//                    player.closeInventory();
//                    player.sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.SEND_NAME);
//                    Chat.editingName.add(player);
//                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
//                }
//            } else if (e.getView().getTitle().equals("§9Voting")) {
//                e.setCancelled(true);
//                if (items.containsKey(e.getCurrentItem()) && !Matches.voted.contains((Player) e.getWhoClicked())) {
//                    items.get(e.getCurrentItem()).accept((Player) e.getWhoClicked());
//                    Matches.voted.add((Player) e.getWhoClicked());
//                }
//            } else if (e.getView().getTitle().equals("§9Tasks")) {
//                e.setCancelled(true);
//                if (e.getCurrentItem().equals(HeadItem.ELECTRIC.getHead())) {
//                    ElectricCrewTask task = new ElectricCrewTask(player.getLocation());
//                    tasks.put(task.getStand(), task);
//                    e.getWhoClicked().closeInventory();
//                } else if (e.getCurrentItem().equals(HeadItem.MAZE.getHead())) {
//                    Inventory inv = Bukkit.createInventory(null, 6 * 9, "§9Create Maze");
//                    inv.setItem(0, new ItemStack(Button.isNotToggledMaterial, 64));
//                    e.getWhoClicked().closeInventory();
//                    player.openInventory(inv);
//                } else if (e.getCurrentItem().equals(HeadItem.METEORITE.getHead())) {
//                    MeteorCrewTask task = new MeteorCrewTask(player.getLocation());
//                    tasks.put(task.getStand(), task);
//                    e.getWhoClicked().closeInventory();
//                } else if (e.getCurrentItem().equals(HeadItem.RADIO.getHead())) {
//                    CommsCrewTask task = new CommsCrewTask(player.getLocation());
//                    tasks.put(task.getStand(), task);
//                    e.getWhoClicked().closeInventory();
//                } else if (e.getCurrentItem().equals(HeadItem.MYSTERY_BOX.getHead())) {
//                    WiresCrewTask task = new WiresCrewTask(player.getLocation());
//                    tasks.put(task.getStand(), task);
//                    e.getWhoClicked().closeInventory();
//                } else if (e.getCurrentItem().equals(HeadItem.WAVE.getHead())) {
//                    LevelCrewTask task = new LevelCrewTask(player.getLocation());
//                    tasks.put(task.getStand(), task);
//                    e.getWhoClicked().closeInventory();
//                }
//            } else if (e.getView().getTitle().equals("§6Click Task")) {
//                e.setCancelled(true);
//                CrewTask task = CraftUs.profiles.get(player).getTask();
//                if (task == null) {
//                    return;
//                }
//                if (task instanceof ElectricCrewTask) {
//                    ElectricCrewTask electricCrewTask = (ElectricCrewTask) task;
//                    Button button = electricCrewTask.getButton(e.getSlot());
//                    if (button == null) {
//                        return;
//                    }
//                    button.setToggled(!button.isToggled());
//                    electricCrewTask.updateButton(player, e.getSlot());
//                    List<Button> buttons = electricCrewTask.getButtons().stream().filter(
//                            btn -> !btn.isToggled()).collect(Collectors.toList());
//                    if (buttons.isEmpty()) {
//                        player.closeInventory();
//                        for (CrewTask t : profile.crewTasks) {
//                            if (t instanceof ElectricCrewTask) {
//                                profile.crewTasks.remove(t);
//                                if (profile.getCurrentMatch() != null && profile.crewTasks.size() == 0) {
//                                    player.sendMessage("§aTasks completed!");
//                                }
//                                verifyTasks(profile);
//                                break;
//                            }
//                        }
//                    }
//                } else if (task instanceof MazeCrewTask) {
//                    MazeCrewTask mazeCrewTask = (MazeCrewTask) task;
//                    Button button = mazeCrewTask.getButton(e.getSlot());
//                    if (button == null) {
//                        return;
//                    }
//                    button.setToggled(!button.isToggled());
//                    mazeCrewTask.updateButton(player, e.getSlot());
//                    List<Button> buttons = mazeCrewTask.getButtons().stream().filter(btn -> !btn.isToggled()).collect(
//                            Collectors.toList());
//                    if (buttons.isEmpty()) {
//                        player.closeInventory();
//                        for (CrewTask t : profile.crewTasks) {
//                            if (t instanceof MazeCrewTask) {
//                                profile.crewTasks.remove(t);
//                                if (profile.getCurrentMatch() != null && profile.crewTasks.size() == 0) {
//                                    player.sendMessage("§aTasks completed!");
//                                }
//                                verifyTasks(profile);
//                                break;
//                            }
//                        }
//                    }
//                } else if (task instanceof MeteorCrewTask) {
//                    MeteorCrewTask meteorCrewTask = (MeteorCrewTask) task;
//
//                    if (e.getCurrentItem().equals(HeadItem.METEORITE.getHead())) {
//                        meteorCrewTask.addMeteor();
//                        player.getOpenInventory().setItem(e.getSlot(), null);
//                        if (meteorCrewTask.getMeteors() >= 20) {
//                            e.getWhoClicked().closeInventory();
//                            meteorCrewTask.getInventory().clear();
//                            meteorCrewTask.setMeteors(0);
//                            for (CrewTask t : profile.crewTasks) {
//                                if (t instanceof MeteorCrewTask) {
//                                    profile.crewTasks.remove(t);
//                                    if (profile.getCurrentMatch() != null && profile.crewTasks.size() == 0) {
//                                        player.sendMessage("§aTasks completed!");
//                                    }
//                                    verifyTasks(profile);
//                                    break;
//                                }
//                            }
//                            return;
//                        }
//                        player.getOpenInventory().setItem(meteorCrewTask.randomSlot(), HeadItem.METEORITE.getHead());
//                        player.updateInventory();
//                    }
//                }
//            } else if (e.getView().getTitle().equals("§9Comms Task")) {
//                e.setCancelled(true);
//                CrewTask task = CraftUs.profiles.get(player).getTask();
//                if (task == null) {
//                    return;
//                }
//                if (task instanceof CommsCrewTask) {
//                    CommsCrewTask commsCrewTask = (CommsCrewTask) task;
//                    commsCrewTask.changeItem(player, e.getSlot());
//                    boolean won = true;
//                    int[] slots = {28, 30, 32, 34};
//                    for (int slot : slots) {
//                        if (!player.getOpenInventory().getItem(13).equals(player.getOpenInventory().getItem(slot))) {
//                            won = false;
//                            break;
//                        }
//                    }
//                    if (won) {
//                        player.closeInventory();
//                        for (CrewTask t : profile.crewTasks) {
//                            if (t instanceof CommsCrewTask) {
//                                profile.crewTasks.remove(t);
//                                if (profile.getCurrentMatch() != null && profile.crewTasks.size() == 0) {
//                                    player.sendMessage("§aTasks completed!");
//                                }
//                                verifyTasks(profile);
//                                break;
//                            }
//                        }
//                    }
//                }
//            } else if (e.getView().getTitle().equals("§9Wires Task")) {
//                e.setCancelled(true);
//                CrewTask task = CraftUs.profiles.get(player).getTask();
//                if (task == null) {
//                    return;
//                }
//                if (task instanceof WiresCrewTask) {
//                    WiresCrewTask wiresCrewTask = (WiresCrewTask) task;
//                    wiresCrewTask.select(player, e.getSlot());
//                    if (wiresCrewTask.won(player)) {
//                        player.closeInventory();
//                        for (CrewTask t : profile.crewTasks) {
//                            if (t instanceof WiresCrewTask) {
//                                profile.crewTasks.remove(t);
//                                if (profile.getCurrentMatch() != null && profile.crewTasks.size() == 0) {
//                                    player.sendMessage("§aTasks completed!");
//                                }
//                                verifyTasks(profile);
//                                break;
//                            }
//                        }
//                    }
//                }
//            } else if (e.getView().getTitle().equals("§9LevelTask")) {
//                e.setCancelled(true);
//                CrewTask task = CraftUs.profiles.get(player).getTask();
//                if (task == null) {
//                    return;
//                }
//                if (task instanceof LevelCrewTask) {
//                    if (e.getCurrentItem().getEnchantments().containsKey(Enchantment.DURABILITY)) {
//                        e.getWhoClicked().closeInventory();
//                        for (CrewTask t : profile.crewTasks) {
//                            if (t instanceof LevelCrewTask) {
//                                profile.crewTasks.remove(t);
//                                if (profile.getCurrentMatch() != null && profile.crewTasks.size() == 0) {
//                                    player.sendMessage("§aTasks completed!");
//                                }
//                                verifyTasks(profile);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void verifyTasks(CraftUsProfile profile) {
//        boolean matchHasTask = false;
//        for (Player p : profile.getCurrentMatch().getPlayers()) {
//            CraftUsProfile prof = CraftUs.profiles.get(p);
//            if (prof.crewTasks.size() > 0) {
//                matchHasTask = true;
//                break;
//            }
//        }
//        if (!matchHasTask) {
//            profile.getCurrentMatch().winner("§bCrewmate");
//        }
//    }
//
//    private void openSearch(Player player) {
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
//        player.openInventory(inv);
//        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
//    }
//
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
//
//    private ItemStack getChangeName() {
//        ItemStack mapItem = HeadItem.EARTH.getHead();
//        ItemMeta meta = mapItem.getItemMeta();
//        meta.setDisplayName("§eChange Name");
//        mapItem.setItemMeta(meta);
//        return mapItem;
//    }
//
//}