package org.danismod.danismod.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.Set;

public class ModEntitySpawns {

    private record SpawnEntry(
            Set<RegistryKey<Biome>> biomes,
            SpawnGroup group,
            EntityType<?> entityType,
            int weight,
            int minGroup,
            int maxGroup
    ) {}

    // reminder: add spawns here
    private static final List<SpawnEntry> SPAWNS = List.of(
            new SpawnEntry(
                    Set.of(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU),
                    SpawnGroup.CREATURE, ModEntities.ELEPHANT, 10, 3, 8
            ),
            new SpawnEntry(
                    Set.of(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU),
                    SpawnGroup.CREATURE, ModEntities.BUFFALO, 14, 3, 7
            ),
            new SpawnEntry(
                    Set.of(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU),
                    SpawnGroup.CREATURE, ModEntities.LION, 20, 3, 5
            )
    );

    public static void registerSpawns() {
        for (SpawnEntry entry : SPAWNS) {
            BiomeModifications.addSpawn(
                    context -> entry.biomes().contains(context.getBiomeKey()),
                    entry.group(),
                    entry.entityType(),
                    entry.weight(),
                    entry.minGroup(),
                    entry.maxGroup()
            );
        }
    }
}