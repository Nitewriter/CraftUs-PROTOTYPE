package com.abidux.craftus.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SkullBuilder {

    private final String url;

    public SkullBuilder(String url) {
        this.url = url;
    }

    private String name;

    public SkullBuilder name(String name) {
        this.name = name;
        return this;
    }

    private List<String> lore;

    public SkullBuilder lore(String... lore) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, lore);
        this.lore = list;
        return this;
    }

    public ItemStack build() {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        setProfile(meta, this.url);
        if (!this.name.isEmpty()) {
            meta.setDisplayName(this.name);
        }
        if (this.lore != null) {
            meta.setLore(this.lore);
        }
        head.setItemMeta(meta);
        return head;
    }

    private void setProfile(SkullMeta meta, String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}