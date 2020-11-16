package com.abidux.craftus.commands;

import com.abidux.craftus.Main;
import com.abidux.craftus.api.Matches;
import com.abidux.craftus.api.objects.CraftUsProfile;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lobby implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && cmd.getName().equalsIgnoreCase("lobby")) {
            Player player = (Player) sender;
            CraftUsProfile profile = Main.profiles.get(player);
            if (profile.getCurrentMatch() != null) {
                profile.getCurrentMatch().leave(player, true);
                Matches.leave(Main.profiles.get(player));
            } else if (Main.lobby != null) {
                profile.getPlayer().teleport(Main.lobby);
                profile.getPlayer().setGameMode(GameMode.ADVENTURE);
            }
        }
        return false;
    }

}