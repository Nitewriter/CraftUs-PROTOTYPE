package com.abidux.craftus.utils;

import com.abidux.craftus.exceptions.PlayerCommandException;
import com.abidux.craftus.exceptions.RestrictedCommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAssertion {
    public static Player assertPlayer(CommandSender sender, Command command) throws PlayerCommandException {
        if (sender instanceof Player) {
            return (Player) sender;
        }

        throw new PlayerCommandException(command);
    }

    public static Player assertAdmin(CommandSender sender, Command command, String permission) throws PlayerCommandException, RestrictedCommandException {
        Player player = assertPlayer(sender, command);
        if (player.hasPermission(permission)) {
            return player;
        }

        throw new RestrictedCommandException(command, permission);
    }
}
