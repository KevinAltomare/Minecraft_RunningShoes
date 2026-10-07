package com.kevin.runningshoes;

import com.kevin.runningshoes.item.RunningShoesItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunningShoesMod implements ModInitializer {
	public static final String MOD_ID = "runningshoes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		RunningShoesItem.register();
		LOGGER.info("Running Shoes initialized");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
