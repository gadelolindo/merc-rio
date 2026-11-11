package com.mercurio;

import net.fabricmc.api.ModInitializer;

public class Mercurio implements ModInitializer {
	public static final String MOD_ID = "mercurio";

	@Override
	public void onInitialize() {
		ModItems.initialize();
		CreeperSnowballTracker.register();
	}
}
