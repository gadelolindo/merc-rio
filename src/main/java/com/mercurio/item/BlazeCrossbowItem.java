package com.mercurio.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Besta Flamejante do Blaze: dispara 3 bolas de fogo em leque. */
public class BlazeCrossbowItem extends Item {
	private static final int COOLDOWN_TICKS = 20;
	private static final double SPREAD_DEGREES = 8.0;

	public BlazeCrossbowItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.getCooldowns().isOnCooldown(stack)) {
			return InteractionResult.PASS;
		}

		if (level instanceof ServerLevel serverLevel) {
			Vec3 look = player.getLookAngle();
			for (int i = -1; i <= 1; i++) {
				Vec3 dir = look.yRot((float) Math.toRadians(i * SPREAD_DEGREES));
				SmallFireball fireball = new SmallFireball(serverLevel, player, dir);
				fireball.setPos(player.getX() + dir.x * 1.2, player.getEyeY() - 0.2 + dir.y * 1.2, player.getZ() + dir.z * 1.2);
				serverLevel.addFreshEntity(fireball);
			}
			serverLevel.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
		}

		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		return InteractionResult.SUCCESS;
	}
}
