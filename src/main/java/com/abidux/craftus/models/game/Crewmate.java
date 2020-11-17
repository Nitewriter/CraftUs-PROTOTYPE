package com.abidux.craftus.models.game;

import com.abidux.craftus.enums.DisplayMessage;
import com.abidux.craftus.enums.PlayerColor;
import com.abidux.craftus.game.tasks.CrewTask;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Crewmate extends GamePlayer {
    private final List<CrewTask> crewTasks = new ArrayList<>();

    public Crewmate(Player player, PlayerColor color) {
        super(player, color);

        setTitle(DisplayMessage.CREWMATE_TITLE.toString());
        setSubTitle(DisplayMessage.CREWMATE_SUBTITLE.toString());
    }

    public List<CrewTask> getCrewTasks() {
        return crewTasks;
    }
}
