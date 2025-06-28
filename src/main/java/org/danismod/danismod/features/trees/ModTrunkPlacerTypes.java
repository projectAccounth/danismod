package org.danismod.danismod.features.trees;

import org.danismod.danismod.Danismod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class ModTrunkPlacerTypes {
    public static final TrunkPlacerType<PalmTrunkPlacer> PALM = Registry.register(
        Registries.TRUNK_PLACER_TYPE,
        Identifier.of(Danismod.MOD_ID, "palm_trunk_placer"),
        new TrunkPlacerType<>(PalmTrunkPlacer.CODEC)
    );

    public static void initialize() {}
}