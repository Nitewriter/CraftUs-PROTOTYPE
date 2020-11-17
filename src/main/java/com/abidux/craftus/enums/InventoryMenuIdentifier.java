package com.abidux.craftus.enums;

import java.util.UUID;

public enum InventoryMenuIdentifier {
    ADMIN_HOME("32b233df-f49f-4c9e-93d0-7e373fdc3603"),
    ADMIN_MAPS("4f85d2fe-c280-4fd5-9630-b5709a2f933d"),
    ADMIN_MAP_EDIT("113b9f53-414e-447c-bba1-6e706f452089"),
    ADMIN_MAP_TASKS("f1a0980a-e17c-4079-b704-d0fe704bd438"),

    GAME_COMMS_TASK("bd83e059-3668-496b-8ce8-934b53811deb"),
    GAME_ELECTRIC_TASK("9a1a8d14-c6ff-48e2-97cf-e9d57ddb8b4c"),
    GAME_LEVEL_TASK("e1fe9f7f-9cfb-4585-91f8-31fbaac98c0b"),
    GAME_MAZE_TASK("cf3fba84-cd11-4068-a538-01fca8bf4645"),
    GAME_METEOR_TASK("e20c2317-88f1-45f2-973b-5508a4ed2221"),
    GAME_WIRES_TASK("ebc959a9-bc08-429e-8158-96031741f3cc");

    private final String rawUUID;

    InventoryMenuIdentifier(String rawUUID) {
        this.rawUUID = rawUUID;
    }

    public UUID getUniqueId() {
        return UUID.fromString(rawUUID);
    }
}
