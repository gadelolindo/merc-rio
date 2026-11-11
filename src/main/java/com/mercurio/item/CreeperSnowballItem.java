package com.mercurio.item;

import com.mercurio.CreeperSnowballTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Neve Sibilante do Creeper: bola de neve que explode ao atingir algo. */
public class CreeperSnowballItem extends Item {
	private static final int COOLDOWN_TICKS = 10;

	public CreeperSnowballItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.getCooldowns().isOnCooldown(stack)) {
			return InteractionResult.PASS;
		}

		if (level instanceof ServerLevel serverLevel) {
			Snowball ball = Projectile.spawnProjectileFromRotation(Snowball::new, serverLevel, stack, player, 0.0F, 1.5F, 1.0F);
			CreeperSnowballTracker.track(ball);
			serverLevel.playSound(null, player.blockPosition(), SoundEvents.CREEPER_PRIMED, SoundSource.PLAYERS, 1.0F, 1.2F);
		}

		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		stack.consume(1, player);
		return InteractionResult.SUCCESS;
	}
}
