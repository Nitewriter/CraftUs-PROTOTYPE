package com.abidux.craftus.enums;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum CraftUsSubCommand {
    SET_LOBBY,
    SET_BUTTON,
    TASKS,
    START,
    RELOAD,
    SAVE;

    public static Map<String, CraftUsSubCommand> labeledCommand = new HashMap<String, CraftUsSubCommand>() {
        private static final long serialVersionUID = 990024413780950853L;

        {
        put("setlobby", CraftUsSubCommand.SET_LOBBY);
        put("setbutton", CraftUsSubCommand.SET_BUTTON);
        put("tasks", CraftUsSubCommand.TASKS);
        put("start", CraftUsSubCommand.START);
        put("reload", CraftUsSubCommand.RELOAD);
        put("save", CraftUsSubCommand.SAVE);
    }};

    public static @Nullable CraftUsSubCommand fromLabel(String label) {
        String ignoringCase = label.toLowerCase();
        return labeledCommand.get(ignoringCase);
    }
}
