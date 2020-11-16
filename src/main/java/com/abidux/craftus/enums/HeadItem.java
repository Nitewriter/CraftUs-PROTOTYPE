package com.abidux.craftus.enums;

import org.bukkit.inventory.ItemStack;

public enum HeadItem {

    PLUS(null),
    BOOK(null),
    EARTH(null),
    BED(null),
    X(null),
    RADIO(null),
    WAVE(null),
    MYSTERY_BOX(null),
    ELECTRIC(null),
    MAZE(null),
    METEORITE(null),
    MEETING_BUTTON(null);

    ItemStack head;

    HeadItem(ItemStack head) {
        this.head = head;
    }

    public ItemStack getHead() {
        return head;
    }

    public void setItem(ItemStack head) {
        this.head = head;
    }

}