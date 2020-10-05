package br.com.abidux.craftus.api.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import br.com.abidux.craftus.api.PlayerType;
import br.com.abidux.craftus.api.objects.tasks.Task;
import br.com.abidux.craftus.enums.Color;

public class CraftUsProfile {
	
	private Task task;
	public List<Task> tasks = new ArrayList<Task>();
	private Player player;
	private Match currentMatch;
	private Color color;
	private PlayerType type;
	
	public CraftUsProfile(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Match getCurrentMatch() {
		return currentMatch;
	}
	
	public void setCurrentMatch(Match currentMatch) {
		this.currentMatch = currentMatch;
	}
	
	public String getMatchName() {
		return currentMatch.getName();
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public PlayerType getType() {
		return type;
	}
	
	public void setType(PlayerType type) {
		this.type = type;
	}
	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
}