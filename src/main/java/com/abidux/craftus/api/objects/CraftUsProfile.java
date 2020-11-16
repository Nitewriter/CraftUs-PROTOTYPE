package com.abidux.craftus.api.objects;

import com.abidux.craftus.api.PlayerType;
import com.abidux.craftus.api.objects.tasks.CrewTask;
import com.abidux.craftus.enums.PlayerColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CraftUsProfile {

    private CrewTask crewTask;
    public List<CrewTask> crewTasks = new ArrayList<>();
    private final Player player;
    private Match currentMatch;
    private PlayerColor playerColor;
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