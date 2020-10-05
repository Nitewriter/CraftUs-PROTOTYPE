package br.com.abidux.craftus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.Matches;
import br.com.abidux.craftus.api.objects.CraftUsProfile;

public class Lobby implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player && cmd.getName().equalsIgnoreCase("lobby")) {
			Player player = (Player)sender;
			CraftUsProfile profile = Main.profiles.get(player);
			if (profile.getCurrentMatch() != null) {
				profile.getCurrentMatch().leave(player, true);
				Matches.leave(Main.profiles.get(player));
			}else if (Main.lobby != null) profile.getPlayer().teleport(Main.lobby);
		}
		return false;
	}
	
}