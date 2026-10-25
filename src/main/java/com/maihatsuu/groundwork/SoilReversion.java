package com.maihatsuu.groundwork;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Right-clicking a dirt path with a shovel, or farmland with a hoe, turns it back into dirt.
 * Right-clicking farmland with a shovel flattens it into a dirt path.
 */
public final class SoilReversion {
	private SoilReversion() {
	}

	public static void register() {
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (player.isSpectator()) {
				return InteractionResult.PASS;
			}

			ItemStack stack = player.getItemInHand(hand);
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = level.getBlockState(pos);
			BlockState target;
			SoundEvent sound;
			if (state.is(Blocks.DIRT_PATH) && stack.is(ItemTags.SHOVELS)) {
				target = Blocks.DIRT.defaultBlockState();
				sound = SoundEvents.SHOVEL_FLATTEN;
			} else if (state.is(Blocks.FARMLAND) && stack.is(ItemTags.HOES)) {
				target = Blocks.DIRT.defaultBlockState();
				sound = SoundEvents.HOE_TILL;
			} else if (state.is(Blocks.FARMLAND) && stack.is(ItemTags.SHOVELS)
					// Same conditions as vanilla path-making in ShovelItem.useOn.
					&& hitResult.getDirection() != Direction.DOWN
					&& level.getBlockState(pos.above()).isAir()) {
				target = Blocks.DIRT_PATH.defaultBlockState();
				sound = SoundEvents.SHOVEL_FLATTEN;
			} else {
				return InteractionResult.PASS;
			}

			if (!player.mayUseItemAt(pos, hitResult.getDirection(), stack)) {
				return InteractionResult.PASS;
			}

			// Played on both sides; passing the player stops the server from sending it back to them.
			level.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);

			if (!level.isClientSide()) {
				// Paths and farmland are shorter than a full block, so lift anything standing on them when they become dirt.
				BlockState newState = Block.pushEntitiesUp(state, target, level, pos);
				level.setBlockAndUpdate(pos, newState);
				level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
				stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			}

			return InteractionResult.sidedSuccess(level.isClientSide());
		});
	}
}
