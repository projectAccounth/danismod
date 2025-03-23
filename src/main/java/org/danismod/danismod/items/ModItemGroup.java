package org.danismod.danismod.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup TEST_GROUP = new ItemGroup.Builder(ItemGroup.Row.TOP, 5)
            .icon(() -> new ItemStack(ModItems.ELEPHANT_TUSK))
            .displayName(Text.translatable("itemGroup.danismod.mod_group"))
            .entries((context, entries) -> {
                entries.add(ModItems.IVORY_SWORD);
                entries.add(ModItems.IVORY_PICKAXE);
                entries.add(ModItems.IVORY_HOE);
                entries.add(ModItems.IVORY_SHOVEL);
                entries.add(ModItems.IVORY_AXE);
                entries.add(ModItems.ELEPHANT_TUSK);
                entries.add(ModItems.IVORY);
                entries.add(ModItems.ELEPHANT_SPAWN_EGG);
                entries.add(ModItems.LION_SPAWN_EGG);
            })
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of("danismod", "mod_group"), TEST_GROUP);
    }
}
