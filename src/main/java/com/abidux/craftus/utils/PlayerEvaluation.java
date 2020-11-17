package com.abidux.craftus.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerEvaluation {
    public static boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }
}
