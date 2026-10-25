package com.maihatsuu.notramplerevertsoil;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoTrampleRevertSoil implements ModInitializer {
	public static final String MOD_ID = "no-trample-revert-soil";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// When false, entities landing on farmland no longer turn it into dirt.
	public static final GameRules.Key<GameRules.BooleanValue> DO_FARMLAND_TRAMPLE =
			GameRuleRegistry.register("doFarmlandTrample", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

	// When false, dry farmland with nothing planted on it no longer decays into dirt on random ticks.
	public static final GameRules.Key<GameRules.BooleanValue> DO_FARMLAND_DECAY =
			GameRuleRegistry.register("doFarmlandDecay", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));

	@Override
	public void onInitialize() {
		SoilReversion.register();
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
