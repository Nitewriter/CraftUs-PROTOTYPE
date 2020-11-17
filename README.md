# CraftUs-PROTOTYPE

This is a prototype, it's possible it has bugs, and the code isn't perfect.

#### Supported Minecraft Versions

- 1.13.x -> 1.16.4

## Developer Guide

This plugin project is configure to be edited using [IntelliJ CE](https://www.jetbrains.com/idea/download/), and uses [Maven](https://maven.apache.org) for build & dependency management.

#### Spigot Dependency Installation

To support custom skulls, this plugin makes use of private Mojang APIs that are not provided by the `spigot-api` Maven dependency and can only be referenced from a Spigot server binary.

You can build it from source using the [Spigot BuildTools](https://www.spigotmc.org/wiki/buildtools/). You will need the 1.16.4 version of the Spigot server binary.

### Debugging

You can attach to the running plugin by using the `Debug Spigot` run configuration. See [IntelliJ: Debug Your Plugin](https://www.spigotmc.org/wiki/intellij-debug-your-plugin/) for more details.

## Using The Plugin

Grant your player the `craftus.admin` permission to create maps and start matches.

#### Admin Commands

| Command | Description |
| --- | --- |
| reload | reloads the plugin configuration |
| setlobby | sets the player lobby |
| start | starts a gameMatch |
| tasks | opens the crewTask menu |
| setbutton | sets meeting button |
| save | saves maps and tasks |

#### Player Commands

| Command | Description |
| --- | --- |
| lobby | teleports player to the lobby,<br />leaves current gameMatch if playing |

## Roadmap

- Refine game loop update cycle
- Define gameMatch maker controller
- Move gameMatch state resolution to gameMatch
- Refine player profile
- Refine command executors
- Refine menu system
- Refine task system
- Refine listeners, possibly define domain specific events
    - Look in to delegation in Java
    - Look in to event broadcast in Java
- Refine armor stand dead player management
- Refine task item for simpler map creation
- Investigate mcMMO project for development practices we can barrow
