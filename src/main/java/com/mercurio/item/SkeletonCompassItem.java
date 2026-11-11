package com.mercurio.item;

import java.util.Comparator;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/** Bussola dos Ossos Errantes: revela mortos-vivos proximos. */
public class SkeletonCompassItem extends Item {
	private static final int COOLDOWN_TICKS = 100;
	private static final double RADIUS = 40.0;
	private static final int GLOW_TICKS = 200;

	public SkeletonCompassItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.getCooldowns().isOnCooldown(stack)) {
			return InteractionResult.PASS;
		}

		if (level instanceof ServerLevel serverLevel) {
			AABB box = player.getBoundingBox().inflate(RADIUS);
			List<LivingEntity> undead = serverLevel.getEntitiesOfClass(LivingEntity.class, box,
					e -> e != player && e.getType().is(EntityTypeTags.UNDEAD));

			for (LivingEntity e : undead) {
				e.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_TICKS, 0, false, false));
			}

			if (undead.isEmpty()) {
				player.displayClientMessage(Component.translatable("message.mercurio.compass.none"), true);
			} else {
				double nearest = undead.stream()
						.mapToDouble(e -> Math.sqrt(e.distanceToSqr(player)))
						.min().orElse(0);
				player.displayClientMessage(Component.translatable("message.mercurio.compass.found",
						undead.size(), String.format("%.0f", nearest)), true);
			}
			serverLevel.playSound(null, player.blockPosition(), SoundEvents.SKELETON_AMBIENT, SoundSource.PLAYERS, 1.0F, 0.7F);
		}

		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		return InteractionResult.SUCCESS;
	}
}
