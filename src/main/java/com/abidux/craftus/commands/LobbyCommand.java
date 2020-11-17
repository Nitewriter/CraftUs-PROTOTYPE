package com.abidux.craftus.commands;

import com.abidux.craftus.CraftUs;
import com.abidux.craftus.controllers.TeleportController;
import com.abidux.craftus.events.PlayerTeleportedToLobbyEvent;
import com.abidux.craftus.exceptions.PlayerCommandException;
import com.abidux.craftus.models.CraftUsPlayer;
import com.abidux.craftus.repository.PlayerRepository;
import com.abidux.craftus.utils.CommandAssertion;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class LobbyCommand implements CommandExecutor {

    public static String command = "lobby";

    public final CraftUs plugin;
    public final TeleportController teleportController;
    public final PlayerRepository playerRepository;

    @Inject
    public LobbyCommand(CraftUs plugin, PlayerRepository playerRepository, TeleportController teleportController) {
        this.plugin = plugin;
        this.playerRepository = playerRepository;
        this.teleportController = teleportController;
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
            Player player = CommandAssertion.assertPlayer(sender, command);
            return handlePlayerLobbyCommand(player);
        } catch (PlayerCommandException exception) {
            plugin.getLogger().warning(exception.getMessage());
        }

        return false;
    }

    private boolean handlePlayerLobbyCommand(Player player) {
        CraftUsPlayer craftUsPlayer = playerRepository.getPlayerForId(player.getUniqueId());
        if (craftUsPlayer == null) {
            return false;
        }

        // If player is not currently in a match teleport to lobby
        if (craftUsPlayer.getCurrentMatch() == null) {
            teleportController.teleportPlayerToLobby(player);
            return true;
        }

        // Broadcast event to be handled by player's current match
        Event event = new PlayerTeleportedToLobbyEvent(craftUsPlayer);
        Bukkit.getPluginManager().callEvent(event);
        return true;
    }

}