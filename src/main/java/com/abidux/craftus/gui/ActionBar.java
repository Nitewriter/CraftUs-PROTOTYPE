package com.abidux.craftus.gui;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBar {
    public static void sendMessage(Player player, String message) {
        TextComponent textComponent = new TextComponent(message);
        sendMessage(player, textComponent);
    }

    public static void sendMessage(Player player, TextComponent textComponent) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
    }
}
