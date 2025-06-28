package org.danismod.danismod.features.trees;

import org.danismod.danismod.Danismod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class ModFoliagePlacers {
    public static final FoliagePlacerType<PalmFoliagePlacer> PALM_FOLIAGE_PLACER = Registry.register(
        Registries.FOLIAGE_PLACER_TYPE,
        Identifier.of(Danismod.MOD_ID, "palm_foliage_placer"),
        new FoliagePlacerType<>(PalmFoliagePlacer.CODEC)
    );

    public static void initialize() {}
}
