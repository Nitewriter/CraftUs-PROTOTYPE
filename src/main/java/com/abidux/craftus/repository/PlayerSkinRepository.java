package com.abidux.craftus.repository;

import com.abidux.craftus.CraftUs;
import com.abidux.craftus.models.PlayerSkin;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class PlayerSkinRepository {

    private final CraftUs plugin;
    private final HashMap<Integer, PlayerSkin> playerSkins = new HashMap<>();

    @Inject
    public PlayerSkinRepository(CraftUs plugin) {
        this.plugin = plugin;
    }

    public @Nullable PlayerSkin getPlayerSkin(Integer skinId) {
        return playerSkins.get(skinId);
    }

    public void load() {
        plugin.getLogger().info(getClass().getSimpleName() + ".load");

        List<Path> filePaths = getBundledSkinFiles();
        if (filePaths.isEmpty()) {
            plugin.getLogger().severe("Empty 'skins' resource directory");
            return;
        }

        try {
            for (Path path : filePaths) {
                InputStream inputStream = CraftUs.class
                        .getClassLoader()
                        .getResourceAsStream(path.toString());

                if (inputStream == null) {
                    plugin.getLogger().info("- Not Loaded: (" + path + ")");
                    continue;
                }

                PlayerSkin playerSkin = PlayerSkin.fromJSON(inputStream);
                playerSkins.put(playerSkin.getId(), playerSkin);
                plugin.getLogger().info("- Loaded: (" + playerSkin.getId() + ") " + playerSkin.getName());
            }

            plugin.getLogger().info("" + playerSkins.size() + " player skins loaded");
        } catch (JsonSyntaxException | IOException exception) {
            plugin.getLogger().severe(exception.getLocalizedMessage());
        }
    }

    private List<Path> getBundledSkinFiles() {
        try {
            ArrayList<Path> files = new ArrayList<>();
            URL resourcesURL = CraftUs.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation();

            Path jarFile = Paths.get(resourcesURL.toString().substring("file:".length()));
            ClassLoader classLoader = CraftUs.class.getClassLoader();
            FileSystem fileSystem = FileSystems.newFileSystem(jarFile, classLoader);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fileSystem.getPath("skins"));
            directoryStream.forEach(files::add);
            return files;
        } catch (IOException exception) {
            plugin.getLogger().severe(exception.getMessage());
        }

        return new ArrayList<>();
    }

}
