package com.abidux.craftus.models.game;

import com.abidux.craftus.enums.DisplayMessage;
import com.abidux.craftus.enums.PlayerColor;
import com.abidux.craftus.game.CoolDown;
import com.abidux.craftus.game.items.Equipment;
import org.bukkit.entity.Player;

public class Impostor extends GamePlayer {
    private final CoolDown killCoolDown = new CoolDown(30);
    private final CoolDown sabotageCoolDown = new CoolDown(30);

    public Impostor(Player player, PlayerColor color) {
        super(player, color);

        setTitle(DisplayMessage.IMPOSTOR_TITLE.toString());
        setSubTitle(DisplayMessage.IMPOSTOR_SUBTITLE.toString());
    }

    public void updateCoolDowns() {
        killCoolDown.update();
        sabotageCoolDown.update();
    }

    public CoolDown getKillCoolDown() {
        return killCoolDown;
    }

    public CoolDown getSabotageCoolDown() {
        return sabotageCoolDown;
    }

    public void giveSword() {
        Player player = getPlayer();
        player.getInventory().setItem(8, Equipment.sword);
    }

}
