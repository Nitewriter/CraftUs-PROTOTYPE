package com.abidux.craftus.game;

import com.abidux.craftus.enums.PlayerColor;
import com.abidux.craftus.game.tasks.CrewTask;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CraftUsProfile {

    private final Player player;
    public List<CrewTask> crewTasks = new ArrayList<>();
    private CrewTask crewTask;
    private GameMatch currentGameMatch;
    private PlayerColor playerColor;
    private PlayerType type;

    public CraftUsProfile(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public GameMatch getCurrentMatch() {
        return currentGameMatch;
    }

    public void setCurrentMatch(GameMatch currentGameMatch) {
        this.currentGameMatch = currentGameMatch;
    }

    public PlayerColor getColor() {
        return playerColor;
    }

    public void setColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public PlayerType getType() {
        return type;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public CrewTask getTask() {
        return crewTask;
    }

    public void setTask(CrewTask crewTask) {
        this.crewTask = crewTask;
    }

}