package com.abidux.craftus.gui;

import org.bukkit.entity.Player;

public class Title {
    public static void sendTitle(Player player, String titleMessage, String subTitleMessage) {
        sendTitle(player, titleMessage, subTitleMessage, 3);
    }

    public static void sendTitle(Player player, String titleMessage, String subTitleMessage, int duration) {
        int gameTick = 20;
        int tickDuration = duration * gameTick;
        player.sendTitle(titleMessage, subTitleMessage, gameTick, tickDuration, gameTick);
    }
}
