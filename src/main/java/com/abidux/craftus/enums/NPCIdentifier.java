package com.abidux.craftus.enums;

import java.util.UUID;

public enum NPCIdentifier {
    PLAYER_ANGRY("f3b5cc35-090f-4de1-9c92-f46bb277c4d4"),
    PLAYER_CREEPY("58994d90-7987-4395-b967-b072e51c5d5d"),
    PLAYER_HAPPY("b8000dfb-f025-4ba4-9e48-fca50ccc19c2"),
    PLAYER_CRISPY("f03a713c-ab7a-40f7-8e49-46938e0cc594"),
    PLAYER_SCAREDY("46f0bddf-c33d-4b63-8797-ae773f34264f"),
    PLAYER_LAZY("afa0fd25-2d6b-4030-873d-390a1f24b93f");

    private final String rawUUID;

    NPCIdentifier(String rawUUID) {
        this.rawUUID = rawUUID;
    }

    public UUID getUniqueId() {
        return UUID.fromString(rawUUID);
    }
}
