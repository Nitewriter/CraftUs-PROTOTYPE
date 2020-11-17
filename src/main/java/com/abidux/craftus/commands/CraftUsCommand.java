package com.abidux.craftus.commands;

import com.abidux.craftus.CraftUs;
import com.abidux.craftus.config.CraftUsConfig;
import com.abidux.craftus.controllers.InventoryMenuController;
import com.abidux.craftus.enums.CraftUsSubCommand;
import com.abidux.craftus.enums.InventoryMenuIdentifier;
import com.abidux.craftus.exceptions.PlayerCommandException;
import com.abidux.craftus.exceptions.RestrictedCommandException;
import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.repository.PlayerRepository;
import com.abidux.craftus.utils.CommandAssertion;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class CraftUsCommand implements CommandExecutor {

    public static String command = "craftus";
    public static String permission = command + ".admin";

    public final CraftUs plugin;
    public final CraftUsConfig config;
    public final PlayerRepository playerRepository;
    public final InventoryMenuController menuController;

    @Inject
    public CraftUsCommand(CraftUs plugin,
                          CraftUsConfig config,
                          PlayerRepository playerRepository,
                          InventoryMenuController menuController) {
        this.plugin = plugin;
        this.config = config;
        this.playerRepository = playerRepository;
        this.menuController = menuController;
    }

    public void register() {
        PluginCommand pluginCommand = plugin.getCommand(command);
        if (pluginCommand == null) {
            System.out.println("Failed to register command executor for '" + command + "' command");
        }

        assert pluginCommand != null;
        pluginCommand.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @Nullable String label,
                             @Nullable String[] args) {
        try {
            Player player = CommandAssertion.assertAdmin(sender, command, permission);
            if (args == null || args.length == 0) {
                return handleBaseCommand(player);
            } else {
                String subCommandLabel = args[0];
                return handleSubCommand(player, subCommandLabel);
            }
        } catch (PlayerCommandException exception) {
            plugin.getLogger().warning(exception.getMessage());
        } catch (RestrictedCommandException exception) {
            Player player = (Player) sender;
            player.sendMessage(exception.getMessage());
        }

        return false;

//        if (cmd.getName().equalsIgnoreCase("craftus")) {
//            if (sender instanceof Player && sender.hasPermission("craftus.admin")) {
//                if (args.length == 1) {
//                    if (args[0].equalsIgnoreCase("reload")) {
//                        com.abidux.craftus.CraftUs.instance.reloadConfig();
//                        com.abidux.craftus.CraftUs.instance.load();
//                        sender.sendMessage("§9[CraftUs] §aReloaded.");
//                        return true;
//                    } else if (args[0].equalsIgnoreCase("setlobby")) {
//                        com.abidux.craftus.CraftUs.lobby = ((Player) sender).getLocation();
//                        com.abidux.craftus.CraftUs.instance.getConfig().set("lobby", ((Player) sender).getLocation());
//                        com.abidux.craftus.CraftUs.instance.saveConfig();
//                        sender.sendMessage("§9[CraftUs] §aSuccess!");
//                        return true;
//                    } else if (args[0].equalsIgnoreCase("start")) {
//                        CraftUsProfile profile = com.abidux.craftus.CraftUs.profiles.get(sender);
//                        if (profile.getCurrentMatch() == null) {
//                            sender.sendMessage("§9[CraftUs] §cYou need to join in a match first.");
//                            return true;
//                        }
//                        Matches.startMatch(profile.getCurrentMatch());
//                        sender.sendMessage("§9[CraftUs] §aStarted!");
//                        return true;
//                    } else if (args[0].equalsIgnoreCase("tasks")) {
//                        Inventory inventory = Bukkit.createInventory(null, 4 * 9, "§9Tasks");
//                        inventory.setItem(12, HeadItem.ELECTRIC.getHead());
//                        inventory.setItem(13, HeadItem.MAZE.getHead());
//                        inventory.setItem(14, HeadItem.METEORITE.getHead());
//                        inventory.setItem(21, HeadItem.RADIO.getHead());
//                        inventory.setItem(22, HeadItem.MYSTERY_BOX.getHead());
//                        inventory.setItem(23, HeadItem.WAVE.getHead());
//                        ((Player) sender).openInventory(inventory);
//                        return true;
//                    } else if (args[0].equalsIgnoreCase("setbutton")) {
//                        MeetingButton button = new MeetingButton(((Player) sender).getLocation());
//                        buttons.put(button.getStand(), button);
//                        return true;
//                    } else if (args[0].equalsIgnoreCase("save")) {
//                        GameMaps.save();
//                        com.abidux.craftus.CraftUs.saveStands(false);
//                        return true;
//                    }
//                }
//                Player player = (Player) sender;
//                PlayerChatEventListener.editingName.remove(player);
//                Inventory inv = Bukkit.createInventory(null, 3 * 9, DisplayMessage.PREFIX.toString());
//                inv.setItem(12, HeadItem.PLUS.getHead());
//                inv.setItem(14, HeadItem.BOOK.getHead());
//                player.openInventory(inv);
//                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
//                return true;
//            } else {
//                sender.sendMessage(DisplayMessage.PREFIX.toString() + DisplayMessage.YOU_ARE_NOT_ALLOWED_TO_DO_THIS);
//            }
//        }
//        return false;
    }

    private boolean handleBaseCommand(Player player) {
        CraftUsPlayer craftUsPlayer = playerRepository.getPlayerForId(player.getUniqueId());
        if (craftUsPlayer == null) {
            return false;
        }

        plugin.getLogger().info("CraftUs command issues by " + player.getDisplayName());
        menuController.openMenuForPlayer(craftUsPlayer, InventoryMenuIdentifier.ADMIN_HOME);
        return true;
    }

    private boolean handleSubCommand(Player player, String label) {
        CraftUsSubCommand subCommand = CraftUsSubCommand.fromLabel(label);
        if (subCommand == null) {
            player.sendMessage("Unknown sub-command: " + label);
            return false;
        }

        CraftUsPlayer craftUsPlayer = playerRepository.getPlayerForId(player.getUniqueId());
        if (craftUsPlayer == null) {
            return false;
        }

        switch (subCommand) {
            case SET_LOBBY:
                Location location = player.getLocation();
                config.setLobbyLocation(location);
                player.sendMessage("Lobby set to current location");
                break;
            case SET_BUTTON:
                plugin.getLogger().info("Set button command issues by " + player.getDisplayName());
                break;
            case TASKS:
                menuController.openMenuForPlayer(craftUsPlayer, InventoryMenuIdentifier.ADMIN_MAP_TASKS);
                break;
            case START:
                plugin.getLogger().info("Start command issues by " + player.getDisplayName());
                break;
            case RELOAD:
                config.load();
                break;
            case SAVE:
                config.save();
                break;
        }

        return true;
    }

}