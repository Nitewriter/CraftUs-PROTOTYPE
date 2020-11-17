package com.abidux.craftus.exceptions;

import org.bukkit.command.Command;

public class PlayerCommandException extends Exception {

    public PlayerCommandException(Command command) {
        super(command + " is only usable by players");
    }
}
