package com.mercurio;

import com.mercurio.item.BlazeCrossbowItem;
import com.mercurio.item.CreeperSnowballItem;
import com.mercurio.item.SkeletonCompassItem;
import com.mercurio.item.WitherClubItem;
import java.util.List;
import java.util.function.Function;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;

public final class ModItems {
	private ModItems() {}

	public static final Item CLAVA_BLAZE = register("clava_blaze", WitherClubItem::new,
			props("clava_blaze", Rarity.EPIC).stacksTo(1));

	public static final Item BUSSOLA_ESQUELETO = register("bussola_esqueleto", SkeletonCompassItem::new,
			props("bussola_esqueleto", Rarity.UNCOMMON).stacksTo(1));

	public static final Item BESTA_BLAZE = register("besta_blaze", BlazeCrossbowItem::new,
			props("besta_blaze", Rarity.RARE).stacksTo(1));

	public static final Item NEVE_CREEPER = register("neve_creeper", CreeperSnowballItem::new,
			props("neve_creeper", Rarity.UNCOMMON).stacksTo(16));

	// ---------- Aba do criativo: "Mercúrio" ----------
	public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Mercurio.MOD_ID, "mercurio"));

	public static final CreativeModeTab TAB = FabricCreativeModeTab.builder()
			.icon(() -> new ItemStack(CLAVA_BLAZE))
			.title(Component.translatable("creativeTab.mercurio"))
			.displayItems((params, output) -> {
				output.accept(CLAVA_BLAZE);
				output.accept(BUSSOLA_ESQUELETO);
				output.accept(BESTA_BLAZE);
				output.accept(NEVE_CREEPER);
			})
			.build();

	// ---------- Helpers ----------
	private static Item.Properties props(String name, Rarity rarity) {
		return new Item.Properties()
				.rarity(rarity)
				.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
				.component(DataComponents.LORE, lore(name));
	}

	/** Monta a lore a partir das chaves do arquivo de idioma. */
	private static ItemLore lore(String name) {
		String base = "item." + Mercurio.MOD_ID + "." + name + ".lore.";
		return new ItemLore(List.of(
				flavor(base + "1"),
				flavor(base + "2"),
				Component.empty(),
				line(Component.translatable("lore.mercurio.power"), ChatFormatting.GOLD),
				line(Component.translatable(base + "p1"), ChatFormatting.YELLOW),
				line(Component.translatable(base + "p2"), ChatFormatting.YELLOW),
				Component.empty(),
				line(Component.translatable(base + "cd"), ChatFormatting.DARK_GRAY)));
	}

	private static Component flavor(String key) {
		return Component.translatable(key).withStyle(s -> s.withColor(ChatFormatting.GRAY).withItalic(true));
	}

	private static Component line(Component text, ChatFormatting color) {
		return text.copy().withStyle(s -> s.withColor(color).withItalic(false));
	}

	public static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties props) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Mercurio.MOD_ID, name));
		T item = factory.apply(props.setId(key));
		Registry.register(BuiltInRegistries.ITEM, key, item);
		return item;
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, TAB);
	}
}
