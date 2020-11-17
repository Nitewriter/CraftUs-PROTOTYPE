package com.abidux.craftus.utils;

import com.abidux.craftus.models.PlayerSkin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerSkinApplicator {

    public static void changePlayerSkin(Player player, PlayerSkin playerSkin) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        applyTextures(profile, playerSkin);
    }

    public static void applyTextures(GameProfile gameProfile, PlayerSkin playerSkin) {
        PropertyMap properties = gameProfile.getProperties();
        if (properties.containsKey("textures")) {
            properties.removeAll("textures");
        }

        properties.put("textures", playerSkin.getTextureProperty());
    }

}
