# CraftUs-PROTOTYPE

This is a prototype, it's possible it has bugs, and the code isn't perfect.

#### Supported Minecraft Versions

- 1.13.x -> 1.16.4

## Developer Guide

This plugin project is configure to be edited using [IntelliJ CE](https://www.jetbrains.com/idea/download/), and uses [Maven](https://maven.apache.org) for build & dependency management.

#### Spigot Dependency Installation

To support custom skulls, this plugin makes use of private Mojang APIs that are not provided by the `spigot-api` Maven dependency and can only be referenced from a Spigot server binary.

You can download a pre-built Spigot binary from [getbukkit.org](https://getbukkit.org) or by building it from source using the [Spigot BuildTools](https://www.spigotmc.org/wiki/buildtools/). You will need the 1.16.4 version of the Spigot server binary.

Once you have a server binary, place it in your user home directory at `minecraft/lib`.

```bash
# Move to your home directory
cd ~

# Make the Minecraft lib directory
mkdir -p minecraft/lib

# Copy the Spigot server jar to the lib directory
cp /path/to/jar/spigot-1.16.4.jar ~/minecraft/lib/spigot-1.16.4.jar
```

### Debugging

You can attach to the running plugin by using the `Debug Spigot` run configuration. See [IntelliJ: Debug Your Plugin](https://www.spigotmc.org/wiki/intellij-debug-your-plugin/) for more details.

## Using The Plugin

Grant your player the `craftus.admin` permission to create maps and start matches.

#### Admin Commands

| Command | Description |
| --- | --- |
| reload | reloads the plugin configuration |
| setlobby | sets the player lobby |
| start | starts a match |
| tasks | opens the crewTask menu |
| setbutton | sets meeting button |
| save | saves maps and tasks |

#### Player Commands

| Command | Description |
| --- | --- |
| lobby | teleports player to the lobby,<br />leaves current match if playing |

