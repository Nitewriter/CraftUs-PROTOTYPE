package br.com.abidux.craftus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.abidux.craftus.Main;
import br.com.abidux.craftus.api.Matches;

public class Search implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmdd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (!Matches.players.contains(player) && Main.profiles.get(player).getCurrentMatch() == null)
				Matches.players.add(player);
		}
		return false;
	}
	
}