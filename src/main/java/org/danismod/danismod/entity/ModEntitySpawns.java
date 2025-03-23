package org.danismod.danismod.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntitySpawns {
    private static boolean isSavannaBiome(BiomeSelectionContext context) {
        RegistryKey<Biome> biomeKey = context.getBiomeKey();
        return biomeKey == BiomeKeys.SAVANNA || biomeKey == BiomeKeys.SAVANNA_PLATEAU;
    }
    public static void registerSpawns() {
        BiomeModifications.addSpawn(
                ModEntitySpawns::isSavannaBiome,
                SpawnGroup.CREATURE,
                ModEntities.ELEPHANT,
                10, 3, 8
        );

        BiomeModifications.addSpawn(
                ModEntitySpawns::isSavannaBiome,
                SpawnGroup.CREATURE,
                ModEntities.BUFFALO,
                14, 3, 7
        );

        BiomeModifications.addSpawn(
                ModEntitySpawns::isSavannaBiome,
                SpawnGroup.CREATURE,
                ModEntities.LION,
                20, 3, 5
        );
    }
}
