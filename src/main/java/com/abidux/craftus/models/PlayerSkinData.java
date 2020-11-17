package com.abidux.craftus.models;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerSkinData {

    private final UUID uuid;
    private final PlayerSkinTexture texture;

}
