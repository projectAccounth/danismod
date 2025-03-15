package org.danismod.danismod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final RegistryKey<EntityType<?>> ELEPHANT_KEY = RegistryKey.of(
            Registries.ENTITY_TYPE.getKey(),
            Identifier.of("danismod", "elephant")
    );

    public static final RegistryKey<EntityType<?>> LION_KEY = RegistryKey.of(
            Registries.ENTITY_TYPE.getKey(),
            Identifier.of("danismod", "lion")
    );

    public static final EntityType<Elephant> ELEPHANT = EntityType.Builder.create(Elephant::new, SpawnGroup.CREATURE)
            .dimensions(1.8f, 2.5f)
            .build(ELEPHANT_KEY);

    public static final EntityType<Lion> LION = EntityType.Builder.create(Lion::new, SpawnGroup.CREATURE)
            .dimensions(1.3f, 1.8f)
            .build(LION_KEY);

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, ELEPHANT_KEY, ELEPHANT);
        FabricDefaultAttributeRegistry.register(ELEPHANT, Elephant.createMobAttributes());
        Registry.register(Registries.ENTITY_TYPE, LION_KEY, LION);
        FabricDefaultAttributeRegistry.register(LION, Lion.createMobAttributes());
    }
}