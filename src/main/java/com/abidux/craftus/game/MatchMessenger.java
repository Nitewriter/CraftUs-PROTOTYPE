package com.abidux.craftus.game;

import com.abidux.craftus.game.tasks.CrewTask;
import com.abidux.craftus.gui.ActionBar;
import com.abidux.craftus.gui.Title;
import com.abidux.craftus.models.game.Crewmate;
import com.abidux.craftus.models.game.GamePlayer;
import com.abidux.craftus.models.game.Impostor;
import org.bukkit.entity.Player;

public class MatchMessenger {
    static void sendPlayerRole(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        Title.sendTitle(player, gamePlayer.getTitle(), gamePlayer.getSubTitle());
    }

    static void sendPlayerTasks(GamePlayer gamePlayer) {
        if (gamePlayer instanceof Impostor) {
            return;
        }

        Crewmate crewmate = (Crewmate) gamePlayer;
        Player player = crewmate.getPlayer();

        player.sendMessage("Tasks:");
        for (CrewTask task : crewmate.getCrewTasks()) {
            player.sendMessage("- " + task.getClass().getSimpleName() + 1);
        }
    }

    static void sendPlayerWinner(Player player, String teamName) {
        Title.sendTitle(player, "Winner", teamName);
    }

    static void sendPlayerStartGameCountdown(Player player, CoolDown coolDown) {
        if (coolDown.isCleared()) {
            return;
        }

        String message = "Starting in " + coolDown.getRemainingDuration();
        ActionBar.sendMessage(player, message);
    }

    static void sendPlayerCoolDownMessage(Player player, CoolDown coolDown) {
        String message = "" + coolDown.getRemainingDuration() + " seconds remaining before that actions is available";
        ActionBar.sendMessage(player, message);
    }
}
