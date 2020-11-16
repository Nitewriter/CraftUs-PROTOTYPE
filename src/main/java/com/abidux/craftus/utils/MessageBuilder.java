package com.abidux.craftus.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class MessageBuilder {
	
	private String message;
	private String clickEvent = "";
	private String hoverEvent = "";
	
	public MessageBuilder message(String message) {
		this.message = message;
		return this;
	}
	
	public MessageBuilder clickEvent(String action, String result) {
		this.clickEvent = ",\"clickEvent\":{\"action\":\""+action+"\",\"value\":\""+result+"\"}";
		return this;
	}
	
	public MessageBuilder hoverEvent(String action, String result) {
		this.hoverEvent = ",\"hoverEvent\":{\"action\":\""+action+"\",\"value\":\""+result+"\"}";
		return this;
	}
	
	public String build() {
		return "{\"text\":\""+message+"\""+clickEvent+hoverEvent+"}";
	}
	
	public void send(Player player) {
		PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(build()));
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
}