package com.mercurio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Acompanha as bolas de neve do Creeper em voo e explode quando elas
 * atingem algo (a entidade e descartada no impacto).
 */
public final class CreeperSnowballTracker {
	private static final float EXPLOSION_POWER = 2.5F;
	private static final List<Tracked> TRACKED = new ArrayList<>();

	private static final class Tracked {
		final Snowball ball;
		Vec3 last;

		Tracked(Snowball ball) {
			this.ball = ball;
			this.last = ball.position();
		}
	}

	private CreeperSnowballTracker() {}

	public static void track(Snowball ball) {
		TRACKED.add(new Tracked(ball));
	}

	public static void register() {
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> TRACKED.clear());

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			Iterator<Tracked> it = TRACKED.iterator();
			while (it.hasNext()) {
				Tracked t = it.next();
				if (t.ball.isRemoved()) {
					// DISCARDED = bateu em algo. Ignora descarregamento de chunk etc.
					if (t.ball.getRemovalReason() == Entity.RemovalReason.DISCARDED
							&& t.ball.level() instanceof ServerLevel level) {
						level.explode(t.ball.getOwner(), t.last.x, t.last.y, t.last.z,
								EXPLOSION_POWER, Level.ExplosionInteraction.MOB);
					}
					it.remove();
				} else {
					t.last = t.ball.position();
				}
			}
		});
	}
}
