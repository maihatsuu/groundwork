package com.maihatsuu.groundwork.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.maihatsuu.groundwork.Groundwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {
	private static final String TURN_TO_DIRT = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V";

	// Only the turnToDirt call is skipped, so fall damage from super.fallOn still applies.
	@WrapWithCondition(method = "fallOn", at = @At(value = "INVOKE", target = TURN_TO_DIRT))
	private boolean groundwork$checkTrampleGameRule(Entity entity, BlockState state, Level level, BlockPos pos) {
		return level.getGameRules().getBoolean(Groundwork.DO_FARMLAND_TRAMPLE);
	}

	// Only covers dry, unplanted farmland decaying; a solid block placed on top still turns it to dirt.
	@WrapWithCondition(method = "randomTick", at = @At(value = "INVOKE", target = TURN_TO_DIRT))
	private boolean groundwork$checkDecayGameRule(Entity entity, BlockState state, Level level, BlockPos pos) {
		return level.getGameRules().getBoolean(Groundwork.DO_FARMLAND_DECAY);
	}
}
