package com.maihatsuu.groundwork;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

import net.minecraft.world.level.GameRules;

public class Groundwork implements ModInitializer {
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
}
