package com.abidux.craftus.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SkullBuilder {

    private final String encodedTextures;
    private String name;
    private List<String> lore;

    public SkullBuilder(String encodedTextures) {
        this.encodedTextures = encodedTextures;
    }

    public SkullBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SkullBuilder lore(String... lore) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, lore);
        this.lore = list;
        return this;
    }

    public ItemStack build() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        setProfile(meta, encodedTextures);
        if (!name.isEmpty()) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        head.setItemMeta(meta);
        return head;
    }

    private void setProfile(SkullMeta meta, String encodedTextures) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", encodedTextures));

        try {
            Method setProfile = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            setProfile.setAccessible(true);
            setProfile.invoke(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}