package com.abidux.craftus.commands;

import com.abidux.craftus.Main;
import com.abidux.craftus.api.GameMaps;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.objects.CraftUsProfile;
import com.abidux.craftus.api.objects.tasks.MeetingButton;
import com.abidux.craftus.enums.DisplayMessage;
import com.abidux.craftus.enums.HeadItem;
import com.abidux.craftus.listeners.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class CraftUs implements CommandExecutor {

    public static HashMap<ArmorStand, MeetingButton> buttons = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("craftus")) {
            if (sender instanceof Player && sender.hasPermission("craftus.admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        Main.instance.reloadConfig();
                        Main.instance.load();
                        sender.sendMessage("�9[CraftUs] �aReloaded.");
                        return true;
                    } else if (args[0].equalsIgnoreCase("setlobby")) {
                        Main.lobby = ((Player) sender).getLocation();
                        Main.instance.getConfig().set("lobby", ((Player) sender).getLocation());
                        Main.instance.saveConfig();
                        sender.sendMessage("�9[CraftUs] �aSuccess!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("start")) {
                        CraftUsProfile profile = Main.profiles.get(sender);
                        if (profile.getCurrentMatch() == null) {
                            sender.sendMessage("�9[CraftUs] �cYou need to join in a match first.");
                            return true;
                        }
                        Matches.startMatch(profile.getCurrentMatch());
                        sender.sendMessage("�9[CraftUs] �aStarted!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("tasks")) {
                        Inventory inventory = Bukkit.createInventory(null, 4 * 9, "�9Tasks");
                        inventory.setItem(12, HeadItem.ELECTRIC.getHead());
                        inventory.setItem(13, HeadItem.MAZE.getHead());
                        inventory.setItem(14, HeadItem.METEORITE.getHead());
                        inventory.setItem(21, HeadItem.RADIO.getHead());
                        inventory.setItem(22, HeadItem.MYSTERY_BOX.getHead());
                        inventory.setItem(23, HeadItem.WAVE.getHead());
                        ((Player) sender).openInventory(inventory);
                        return true;
                    } else if (args[0].equalsIgnoreCase("setbutton")) {
                        MeetingButton button = new MeetingButton(((Player) sender).getLocation());
                        buttons.put(button.getStand(), button);
                        return true;
                    } else if (args[0].equalsIgnoreCase("save")) {
                        GameMaps.save();
                        Main.saveStands(false);
                        return true;
                    }
                }
                Player player = (Player) sender;
                Chat.editingName.remove(player);
                Inventory inv = Bukkit.createInventory(null, 3 * 9, DisplayMessage.PREFIX.toString());
                inv.setItem(12, HeadItem.PLUS.getHead());
                inv.setItem(14, HeadItem.BOOK.getHead());
                player.openInventory(inv);
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                return true;
            } else {
                sender.sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.YOU_ARE_NOT_ALLOWED_TO_DO_THIS);
            }
        }
        return false;
    }

}