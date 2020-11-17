package com.abidux.craftus.models;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.properties.Property;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Data
public class PlayerSkin {

    private final Integer id;
    private final String name;
    private final String model;
    private final PlayerSkinData data;

    public static PlayerSkin fromJSON(InputStream inputStream) throws IOException, JsonSyntaxException {


        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);
        String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        return fromJSON(json);
    }

    public static PlayerSkin fromJSON(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, PlayerSkin.class);
    }

    public Property getTextureProperty() {
        PlayerSkinTexture skinTexture = getData().getTexture();
        return new Property("textures", skinTexture.getValue(), skinTexture.getSignature());
    }

}
