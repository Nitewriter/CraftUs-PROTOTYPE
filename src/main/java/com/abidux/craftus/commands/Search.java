package com.abidux.craftus.commands;

import com.abidux.craftus.Main;
import com.abidux.craftus.api.Matches;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Search implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!Matches.players.contains(player) && Main.profiles.get(player).getCurrentMatch() == null) {
                Matches.players.add(player);
                System.out.println("Added " + player.getDisplayName() + " to matches.");
            }
        }
        return false;
    }

}