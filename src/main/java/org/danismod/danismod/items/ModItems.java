package org.danismod.danismod.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import org.danismod.danismod.Danismod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Danismod.MOD_ID, name));

        Item item = itemFactory.apply(settings.registryKey(itemKey));

        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static final ToolMaterial IVORY_TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            607,
            8.0F,
            0F,
            22,
            ItemTags.REPAIRS_IRON_ARMOR
    );

    public static final Item ELEPHANT_TUSK = register("elephant_tusk", Item::new, new Item.Settings());
    public static final Item IVORY = register("ivory", Item::new, new Item.Settings());

    public static final Item IVORY_SWORD = register(
            "ivory_sword",
            settings -> new SwordItem(IVORY_TOOL_MATERIAL, 6f, 1f, settings),
            new Item.Settings()
    );
    public static final Item IVORY_HOE = register(
            "ivory_hoe",
            settings -> new SwordItem(IVORY_TOOL_MATERIAL, 3f, .6f, settings),
            new Item.Settings()
    );
    public static final Item IVORY_PICKAXE = register(
            "ivory_pickaxe",
            settings -> new SwordItem(IVORY_TOOL_MATERIAL, 4f, .8f, settings),
            new Item.Settings()
    );
    public static final Item IVORY_AXE = register(
            "ivory_axe",
            settings -> new SwordItem(IVORY_TOOL_MATERIAL, 8f, .8f, settings),
            new Item.Settings()
    );
    public static final Item IVORY_SHOVEL = register(
            "ivory_shovel",
            settings -> new SwordItem(IVORY_TOOL_MATERIAL, 3f, .7f, settings),
            new Item.Settings()
    );


    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.ELEPHANT_TUSK));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.IVORY));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ModItems.IVORY_HOE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ModItems.IVORY_AXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ModItems.IVORY_PICKAXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ModItems.IVORY_SHOVEL));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register((itemGroup) -> itemGroup.add(ModItems.IVORY_SWORD));
    }
}