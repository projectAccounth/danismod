package org.danismod.danismod.items;

import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import org.danismod.danismod.Danismod;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.danismod.danismod.entity.ModEntities;

import java.util.function.Function;

public class ModItems {
    private static final Item.Settings DEFAULT_SETTINGS = new Item.Settings();

    private static Item register(String name, Function<Item.Settings, Item> itemFactory) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Danismod.MOD_ID, name));
        Item item = itemFactory.apply(DEFAULT_SETTINGS.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static final ToolMaterial IVORY_TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            450,
            3.0F,
            0F,
            22,
            ItemTags.ARMOR_ENCHANTABLE
    );

    public static final Item ELEPHANT_TUSK = register("elephant_tusk", Item::new);
    public static final Item IVORY = register("ivory", Item::new);
    public static final Item IVORY_INGOT = register("reinforced_ivory", Item::new);

    public static final Item IVORY_SWORD = register("ivory_sword",
            settings -> new SwordItem(IVORY_TOOL_MATERIAL, 5f, .8f, settings));
    public static final Item IVORY_HOE = register("ivory_hoe",
            settings -> new HoeItem(IVORY_TOOL_MATERIAL, 3f, .6f, settings));
    public static final Item IVORY_PICKAXE = register("ivory_pickaxe",
            settings -> new PickaxeItem(IVORY_TOOL_MATERIAL, 4f, .6f, settings));
    public static final Item IVORY_AXE = register("ivory_axe",
            settings -> new AxeItem(IVORY_TOOL_MATERIAL, 8f, .5f, settings));
    public static final Item IVORY_SHOVEL = register("ivory_shovel",
            settings -> new ShovelItem(IVORY_TOOL_MATERIAL, 3f, .6f, settings));

    public static final Item ELEPHANT_SPAWN_EGG = register("elephant_spawn_egg",
            settings -> new SpawnEggItem(ModEntities.ELEPHANT, settings));
    public static final Item LION_SPAWN_EGG = register("lion_spawn_egg",
            settings -> new SpawnEggItem(ModEntities.LION, settings));

    public static final Item PALM_SAPLING = register("palm_sapling", Item::new);

    public static void initialize() {}
}