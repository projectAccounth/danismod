package org.danismod.others;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GrinderFuelRegistry {
    private static final Map<Item, Integer> FUEL_MAP = new HashMap<>();

    public static void register(Item item, int fuelUses) {
        FUEL_MAP.put(item, fuelUses);
    }

    public static int getFuelUses(ItemStack stack) {
        return FUEL_MAP.getOrDefault(stack.getItem(), 0);
    }

    public static boolean isFuel(ItemStack stack) {
        return FUEL_MAP.containsKey(stack.getItem());
    }

    public static void initialize() {
        register(Items.BLAZE_POWDER, 50);
        register(Items.STICK, 5);
        register(Items.COAL, 70);
        register(Items.CHARCOAL, 70);
    }
}
