package br.com.abidux.craftus.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.Maps;
import br.com.abidux.craftus.api.Matches;
import br.com.abidux.craftus.api.objects.CraftUsProfile;
import br.com.abidux.craftus.api.objects.Map;
import br.com.abidux.craftus.api.objects.tasks.Button;
import br.com.abidux.craftus.api.objects.tasks.CommsTask;
import br.com.abidux.craftus.api.objects.tasks.ElectricTask;
import br.com.abidux.craftus.api.objects.tasks.LevelTask;
import br.com.abidux.craftus.api.objects.tasks.MazeTask;
import br.com.abidux.craftus.api.objects.tasks.MeteorTask;
import br.com.abidux.craftus.api.objects.tasks.Task;
import br.com.abidux.craftus.api.objects.tasks.WiresTask;
import br.com.abidux.craftus.enums.HeadsItem;
import br.com.abidux.craftus.enums.Messages;

public class InvClick implements Listener {
	
	public static HashMap<ArmorStand, Task> tasks = new HashMap<>();
	public static HashMap<ItemStack, Consumer<Player>> itens = new HashMap<>();
	public static HashMap<Player, Map> editing = new HashMap<>();
	private HashMap<Player, Integer> page = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	void click(InventoryClickEvent e) {
		try {
			Player player = (Player)e.getWhoClicked();
			CraftUsProfile profile = Main.profiles.get(player);
			if (profile.getCurrentMatch() != null && e.getClickedInventory().equals(player.getInventory())) {
				e.setCancelled(true);
				return;
			}
			if (e.getClickedInventory().getName().equals(Messages.PREFIX.toString())) {
				e.setCancelled(true);
				if (e.getCurrentItem().equals(HeadsItem.PLUS.getHead())) {
					e.getWhoClicked().closeInventory();
					Chat.players.add(player);
					e.getWhoClicked().sendMessage(Messages.PREFIX.toString() + Messages.SEND_NAME);
					player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
					return;
				}else if (e.getCurrentItem().equals(HeadsItem.BOOK.getHead())) {
					page.put(player, 0);
					openSearch(player);
				}
			}else if (e.getClickedInventory().getName().equals(Messages.PREFIX.toString() + "- Maps")) {
				e.setCancelled(true);
				if (e.getCurrentItem().equals(getPrevious())) {
					page.put(player, page.get(player)-1);
					openSearch(player);
					return;
				}else if (e.getCurrentItem().equals(getNext())) {
					page.put(player, page.get(player)+1);
					openSearch(player);
					return;
				}else if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
					String displayName = e.getCurrentItem().getItemMeta().getDisplayName();
					String mapName = displayName.substring(2, displayName.length()).toLowerCase();
					Inventory inv = Bukkit.createInventory(null, 3*9, Messages.PREFIX.toString()+Messages.EDIT_MAP);
					inv.setItem(11, HeadsItem.BED.getHead());
					inv.setItem(13, getChangeName());
					inv.setItem(15, HeadsItem.X.getHead());
					editing.put(player, Maps.maps.get(mapName));
					player.openInventory(inv);
					player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
					return;
				}
			}else if (e.getClickedInventory().getName().equals(Messages.PREFIX.toString()+Messages.EDIT_MAP)) {
				if (e.getCurrentItem().equals(HeadsItem.BED.getHead())) {
					editing.get(player).setSpawn(player.getLocation());
					player.closeInventory();
					editing.remove(player);
					player.sendMessage(Messages.PREFIX.toString()+Messages.MODIFIED);
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
					return;
				}else if(e.getCurrentItem().equals(HeadsItem.X.getHead())) {
					Maps.maps.remove(editing.get(player).getName().toLowerCase());
					player.closeInventory();
					player.sendMessage(Messages.PREFIX.toString()+Messages.MODIFIED);
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
					return;
				}else if(e.getCurrentItem().equals(getChangeName())) {
					player.closeInventory();
					player.sendMessage(Messages.PREFIX.toString()+Messages.SEND_NAME);
					Chat.editingName.add(player);
					player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
					return;
				}
			}else if (e.getClickedInventory().getName().equals("§9Voting")) {
				e.setCancelled(true);
				if (itens.containsKey(e.getCurrentItem()) && !Matches.voted.contains((Player)e.getWhoClicked())) {
					itens.get(e.getCurrentItem()).accept((Player)e.getWhoClicked());
					Matches.voted.add((Player)e.getWhoClicked());
				}
			}else if (e.getClickedInventory().getName().equals("§9Tasks")) {
				e.setCancelled(true);
				if (e.getCurrentItem().equals(HeadsItem.ELECTRIC.getHead())) {
					ElectricTask task = new ElectricTask(player.getLocation());
					tasks.put(task.getStand(), task);
					e.getWhoClicked().closeInventory();
				}else if (e.getCurrentItem().equals(HeadsItem.MAZE.getHead())) {
					Inventory inv = Bukkit.createInventory(null, 6*9, "§9Create Maze");
					inv.setItem(0, new ItemStack(Material.STAINED_GLASS_PANE, 64, DyeColor.RED.getWoolData()));
					e.getWhoClicked().closeInventory();
					player.openInventory(inv);
				}else if (e.getCurrentItem().equals(HeadsItem.METEORITE.getHead())) {
					MeteorTask task = new MeteorTask(player.getLocation());
					tasks.put(task.getStand(), task);
					e.getWhoClicked().closeInventory();
					return;
				}else if (e.getCurrentItem().equals(HeadsItem.RADIO.getHead())) {
					CommsTask task = new CommsTask(player.getLocation());
					tasks.put(task.getStand(), task);
					e.getWhoClicked().closeInventory();
				}else if (e.getCurrentItem().equals(HeadsItem.MYSTERY_BOX.getHead())) {
					WiresTask task = new WiresTask(player.getLocation());
					tasks.put(task.getStand(), task);
					e.getWhoClicked().closeInventory();
				}else if (e.getCurrentItem().equals(HeadsItem.WAVE.getHead())) {
					LevelTask task = new LevelTask(player.getLocation());
					tasks.put(task.getStand(), task);
					e.getWhoClicked().closeInventory();
				}
			}else if (e.getClickedInventory().getName().equals("§6Click Task")) {
				e.setCancelled(true);
				Task ptask = Main.profiles.get(player).getTask();
				if (ptask == null) return;
				if (ptask instanceof ElectricTask) {
					ElectricTask task = (ElectricTask)ptask;
					Button button = task.getButton(e.getSlot());
					if (button == null) return;
					button.setToggled(!button.isToggled());
					task.updateButton(player, e.getSlot());
					List<Button> buttons = task.getButtons().stream().filter(btn -> !btn.isToggled()).collect(Collectors.toList());
					if (buttons.isEmpty()) {
						player.closeInventory();
						for (Task t : profile.tasks) {
							if (t instanceof ElectricTask) {
								profile.tasks.remove(t);
								if (profile.getCurrentMatch() != null && profile.tasks.size() == 0) player.sendMessage("§aTasks completed!");
								verifyTasks(profile);
								break;
							}
						}
					}
				}else if (ptask instanceof MazeTask) {
					MazeTask task = (MazeTask)ptask;
					Button button = task.getButton(e.getSlot());
					if (button == null) return;
					button.setToggled(!button.isToggled());
					task.updateButton(player, e.getSlot());
					List<Button> buttons = task.getButtons().stream().filter(btn -> !btn.isToggled()).collect(Collectors.toList());
					if (buttons.isEmpty()) {
						player.closeInventory();
						for (Task t : profile.tasks) {
							if (t instanceof MazeTask) {
								profile.tasks.remove(t);
								if (profile.getCurrentMatch() != null && profile.tasks.size() == 0) player.sendMessage("§aTasks completed!");
								verifyTasks(profile);
								break;
							}
						}
					}
				}else if (ptask instanceof MeteorTask) {
					MeteorTask task = (MeteorTask)ptask;
					if (e.getCurrentItem().equals(HeadsItem.METEORITE.getHead())) {
						task.addAcerto();
						player.getOpenInventory().setItem(e.getSlot(), null);
						if (task.getAcertos() >= 20) {
							e.getWhoClicked().closeInventory();
							task.getInventory().clear();
							task.setAcertos(0);
							for (Task t : profile.tasks) {
								if (t instanceof MeteorTask) {
									profile.tasks.remove(t);
									if (profile.getCurrentMatch() != null && profile.tasks.size() == 0) player.sendMessage("§aTasks completed!");
									verifyTasks(profile);
									break;
								}
							}
							return;
						}
						player.getOpenInventory().setItem(task.randomSlot(), HeadsItem.METEORITE.getHead());
						player.updateInventory();
					}
				}
			}else if (e.getClickedInventory().getName().equals("§9Comms Task")) {
				e.setCancelled(true);
				Task ptask = Main.profiles.get(player).getTask();
				if (ptask == null) return;
				if (ptask instanceof CommsTask) {
					CommsTask task = (CommsTask)ptask;
					task.changeItem(player, e.getSlot());
					boolean won = true;
					int[] slots = {28, 30, 32, 34};
					for (int slot : slots) {
						if (!player.getOpenInventory().getItem(13).equals(player.getOpenInventory().getItem(slot))) {
							won = false;
							break;
						}
					}
					if (won) {
						player.closeInventory();
						for (Task t : profile.tasks) {
							if (t instanceof CommsTask) {
								profile.tasks.remove(t);
								if (profile.getCurrentMatch() != null && profile.tasks.size() == 0) player.sendMessage("§aTasks completed!");
								verifyTasks(profile);
								break;
							}
						}
					}
				}
			}else if (e.getClickedInventory().getName().equals("§9Wires Task")) {
				e.setCancelled(true);
				Task ptask = Main.profiles.get(player).getTask();
				if (ptask == null) return;
				if (ptask instanceof WiresTask) {
					WiresTask task = (WiresTask)ptask;
					task.select(player, e.getSlot());
					if (task.won(player)) {
						player.closeInventory();
						for (Task t : profile.tasks) {
							if (t instanceof WiresTask) {
								profile.tasks.remove(t);
								if (profile.getCurrentMatch() != null && profile.tasks.size() == 0) player.sendMessage("§aTasks completed!");
								verifyTasks(profile);
								break;
							}
						}
					}
				}
			}else if (e.getClickedInventory().getName().equals("§9LevelTask")) {
				e.setCancelled(true);
				Task ptask = Main.profiles.get(player).getTask();
				if (ptask == null) return;
				if (ptask instanceof LevelTask) {
					if (e.getCurrentItem().getEnchantments().containsKey(Enchantment.DURABILITY)) {
						e.getWhoClicked().closeInventory();
						for (Task t : profile.tasks) {
							if (t instanceof LevelTask) {
								profile.tasks.remove(t);
								if (profile.getCurrentMatch() != null && profile.tasks.size() == 0) player.sendMessage("§aTasks completed!");
								verifyTasks(profile);
								break;
							}
						}
					}
				}
			}
		} catch (Exception ex) {}
	}
	
	private void verifyTasks(CraftUsProfile profile) {
		boolean matchHasTask = false;
		for (Player p : profile.getCurrentMatch().getPlayers()) {
			CraftUsProfile prof = Main.profiles.get(p);
			if (prof.tasks.size() > 0) {
				matchHasTask = true;
				break;
			}
		}
		if (!matchHasTask) {
			profile.getCurrentMatch().winner("§bCrewmate");
			return;
		}
	}
	
	private void openSearch(Player player) {
		Inventory inv = Bukkit.createInventory(null, 6*9, Messages.PREFIX.toString() + "- Maps");
		int page = this.page.get(player);
		for (int i = 0; i < 45; i++) {
			int index = (45*page)+i;
			if (!(Maps.maps.values().size() > index)) break;
			Map map = (Map) Maps.maps.values().toArray()[index];
			inv.setItem(i, Maps.getItem(map));
		}
		if (page > 0) inv.setItem(45, getPrevious());
		if (Maps.maps.values().size() > (page + 1) * 45) inv.setItem(53, getNext());
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getNext() {
		ItemStack next = new ItemStack(Material.CARPET, 1, DyeColor.LIME.getWoolData());
		ItemMeta nmeta = next.getItemMeta();
		nmeta.setDisplayName(Messages.NEXT.toString());
		next.setItemMeta(nmeta);
		return next;
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getPrevious() {
		ItemStack previous = new ItemStack(Material.CARPET, 1, DyeColor.RED.getWoolData());
		ItemMeta pmeta = previous.getItemMeta();
		pmeta.setDisplayName(Messages.PREVIOUS.toString());
		previous.setItemMeta(pmeta);
		return previous;
	}
	
	private ItemStack getChangeName() {
		ItemStack mapItem = HeadsItem.EARTH.getHead();
		ItemMeta meta = mapItem.getItemMeta();
		meta.setDisplayName("§eChange Name");
		mapItem.setItemMeta(meta);
		return mapItem;
	}
	
}