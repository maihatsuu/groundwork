# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

"No Trample & Revert Soil" is a Fabric mod for Minecraft 1.21.1 (mod id `no-trample-revert-soil`, package `com.maihatsuu.notramplerevertsoil`). The README documents the player-facing behaviour; keep it in sync when features or gamerules change.

It has two parts, both server-side logic in `src/main`:

- **Soil reversion** (`SoilReversion`, a Fabric `UseBlockCallback`): a shovel on `dirt_path` or a hoe on `farmland` turns the block back into dirt, and a shovel on `farmland` turns it into `dirt_path` (only with air above and not from the bottom face, the same conditions as vanilla path-making). Tools are matched by the `ItemTags.SHOVELS` / `ItemTags.HOES` tags, not by `instanceof`. The callback fires on both sides. The sound plays on both, with the player passed so the server doesn't send it back to them, while the block change and durability loss happen only on the server. This mirrors vanilla `ShovelItem.useOn`.
- **Gamerules** (`doFarmlandTrample`, `doFarmlandDecay`, both default `true`): registered as static fields on `NoTrampleRevertSoil` through Fabric's `GameRuleRegistry`, and enforced in `FarmBlockMixin`. That mixin uses MixinExtras `@WrapWithCondition` on the `FarmBlock.turnToDirt` call inside `fallOn` and inside `randomTick`, so the rest of each vanilla method still runs (for example fall damage). `turnToDirt` is also called from `FarmBlock.tick`, when a solid block is placed on top; that call is deliberately left alone.

The client source set has no features yet. `ExampleClientMixin` is still the template placeholder, and `fabric.mod.json` still has the template description, authors and contact links.

## Commands

- `./gradlew build`: compile, remap and package. Jars land in `build/libs/`. CI (`.github/workflows/build.yml`) runs only this.
- `./gradlew runClient` / `./gradlew runServer`: launch a dev game or server. The working directory is `run/`.
- `./gradlew genSources`: decompile Minecraft sources so you can browse vanilla classes. Do this before writing mixins against unfamiliar vanilla code.

There are no tests and no linter set up.

## Architecture and conventions

- **Mojang official mappings** (`loom.officialMojangMappings()`), not Yarn. Use Mojang names in mixin targets and imports (for example `net.minecraft.world.level.block.FarmBlock`, `ResourceLocation`, `Minecraft`). Yarn names will not resolve.
- **Split source sets** (`splitEnvironmentSourceSets()`): common/server code goes in `src/main`, client-only code in `src/client`. `src/main` must not reference client classes.
- Each source set has its own mixin config: `no-trample-revert-soil.mixins.json` (common, `mixins` array) and `no-trample-revert-soil.client.mixins.json` (client, `client` array). Every new mixin class has to be listed in the matching config. Both set `injectors.defaultRequire: 1`, so an injector that matches nothing fails at load time instead of being skipped silently.
- Entrypoints are declared in `src/main/resources/fabric.mod.json`. `NoTrampleRevertSoil.MOD_ID`, `LOGGER` and `id(path)` (builds a namespaced `ResourceLocation`) are the shared helpers.
- Versions (Minecraft, loader, Loom, Fabric API, mod version) live in `gradle.properties`. `${version}` in `fabric.mod.json` is filled in by `processResources`.
- Java 21 target. CI builds with JDK 25.
