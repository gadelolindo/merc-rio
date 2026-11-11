package com.mercurio.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Clava Calcinante do Blaze: dispara cranios do Wither. */
public class WitherClubItem extends Item {
	private static final int COOLDOWN_TICKS = 30;

	public WitherClubItem(Properties properties) {
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
			WitherSkull skull = new WitherSkull(serverLevel, player, look);
			skull.setDangerous(player.isShiftKeyDown()); // agachado = cranio azul
			skull.setPos(player.getX() + look.x * 1.5, player.getEyeY() - 0.2 + look.y * 1.5, player.getZ() + look.z * 1.5);
			serverLevel.addFreshEntity(skull);
			serverLevel.playSound(null, player.blockPosition(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
		}

		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		return InteractionResult.SUCCESS;
	}
}
