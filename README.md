# Groundwork

A Fabric mod for Minecraft 1.21.1 that lets you undo dirt paths and farmland, and adds gamerules to stop farmland being trampled or decaying. Requires [Fabric API](https://modrinth.com/mod/fabric-api).

The mod is server-side: install it on a server and players can join with an unmodded client, Fabric or not, and still get every feature. In singleplayer, install it as normal.

## Features

### Revert dirt paths with a shovel

Right-click a dirt path while holding any shovel to turn it back into dirt. It plays the shovel flatten sound and uses 1 durability.

### Revert farmland with a hoe

Right-click farmland while holding any hoe to turn it back into dirt. It plays the hoe tilling sound and uses 1 durability.

### Turn farmland into a dirt path with a shovel

Right-click farmland while holding any shovel to flatten it straight into a dirt path, without turning it back into dirt first. Like making a path from dirt or grass, the block above must be empty (so it won't work with crops planted) and it plays the shovel flatten sound and uses 1 durability.

All of these work with any item in the `#minecraft:shovels` or `#minecraft:hoes` tag, so modded tools that use those tags are supported. They follow normal build permissions, so they don't work in spectator mode or where you can't change blocks, such as in adventure mode.

## Gamerules

| Gamerule | Default | When set to `false` |
| --- | --- | --- |
| `doFarmlandTrample` | `true` | Players and mobs landing on farmland no longer trample it into dirt. Fall damage still applies. |
| `doFarmlandDecay` | `true` | Dry farmland with nothing planted on it no longer turns back into dirt over time. It still dries out, and placing a solid block on top still turns it into dirt. |

Change them in-game with commands (requires cheats or operator permission):

```
/gamerule doFarmlandTrample false
/gamerule doFarmlandDecay false
```

Or set them on the Game Rules screen when creating a world: `doFarmlandTrample` is under **Miscellaneous** and `doFarmlandDecay` under **World Updates**.

## Building

```
./gradlew build
```

The mod jar is written to `build/libs/`. Use `./gradlew runClient` to launch a development client with the mod loaded.

## License

This mod is available under the CC0 license.
