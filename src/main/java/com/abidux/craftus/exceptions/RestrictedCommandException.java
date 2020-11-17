package com.abidux.craftus.exceptions;

import org.bukkit.command.Command;

public class RestrictedCommandException extends Exception {

    public RestrictedCommandException(Command command, String permission) {
        super(command + " requires the permission '" + permission + "'");
    }
}



