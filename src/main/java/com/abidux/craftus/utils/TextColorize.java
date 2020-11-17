package com.abidux.craftus.utils;

import org.bukkit.ChatColor;

public class TextColorize {
    public static String toInvisibleString(String original) {
        StringBuilder invisible = new StringBuilder();
        for (char c : original.toCharArray()) {
            invisible.append(ChatColor.COLOR_CHAR + "").append(c);
        }
        return invisible.toString();
    }

    public static String toVisibleString(String invisible) {
        return invisible.replaceAll("" + ChatColor.COLOR_CHAR, "");
    }
}
