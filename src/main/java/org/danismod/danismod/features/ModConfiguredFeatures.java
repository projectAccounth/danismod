package org.danismod.danismod.features;

import org.danismod.danismod.Danismod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final Identifier PALM_TREE_IDENTIFIER = Identifier.of(Danismod.MOD_ID, "palm");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PALM_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, PALM_TREE_IDENTIFIER);

    public static void initialize() {}
}