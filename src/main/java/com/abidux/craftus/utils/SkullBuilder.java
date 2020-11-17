package com.abidux.craftus.utils;

import com.abidux.craftus.enums.PlayerSkinOld;
import com.google.inject.Inject;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SkullBuilder {

    @Getter private final UUID uniqueId;
    @Getter private final String name;
    @Getter private List<String> lore;

    @Inject private Logger logger;

    public SkullBuilder(UUID uuid, String name) {
        uniqueId = uuid;
        this.name = name;
    }

    public SkullBuilder lore(String... lore) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, lore);
        this.lore = list;
        return this;
    }

    public ItemStack build(PlayerSkinOld playerSkin) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = playerHead.getItemMeta();
        if (meta == null) {
            return playerHead;
        }

        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        GameProfile gameProfile = playerSkin.getGameProfile(uniqueId, name);
        setProfile(skullMeta, gameProfile);
        skullMeta.setDisplayName(name);

        if (lore != null) {
            skullMeta.setLore(lore);
        }

        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }

    private void setProfile(SkullMeta skullMeta, GameProfile gameProfile) {
        try {
            Method setProfile = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            setProfile.setAccessible(true);
            setProfile.invoke(skullMeta, gameProfile);
        } catch (Exception exception) {
            logger.severe(exception.getMessage());
        }
    }

}