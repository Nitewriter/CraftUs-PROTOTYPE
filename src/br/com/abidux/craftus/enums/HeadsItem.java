package br.com.abidux.craftus.enums;

import org.bukkit.inventory.ItemStack;

public enum HeadsItem {
	
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
	private HeadsItem(ItemStack head) {
		this.head = head;
	}
	
	public ItemStack getHead() {
		return head;
	}
	
	public void setItem(ItemStack head) {
		this.head = head;
	}
	
}