package com.abidux.craftus.models.game;

import com.abidux.craftus.enums.PlayerColor;
import com.abidux.craftus.game.items.Equipment;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamePlayer {
    private final Player player;
    private final PlayerColor color;
    private String title;
    private String subTitle;
    private Boolean isGhost = false;

    public GamePlayer(Player player, PlayerColor color) {
        this.player = player;
        this.color = color;

        updatePlayerGameMode();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Boolean isGhost() {
        return isGhost;
    }

    public void setIsGhost(Boolean isGhost) {
        this.isGhost = isGhost;
        updatePlayerGameMode();
    }

    public void givePliers() {
        player.getInventory().setHeldItemSlot(4);
        player.getInventory().setItem(4, Equipment.pliers);
    }

    private void updatePlayerGameMode() {
        GameMode gameMode = isGhost ? GameMode.SPECTATOR : GameMode.ADVENTURE;
        player.setGameMode(gameMode);
    }

}
