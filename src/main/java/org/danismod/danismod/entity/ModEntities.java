package org.danismod.danismod.entity;

import org.danismod.danismod.Danismod;
import org.danismod.danismod.entity.mobs.Buffalo;
import org.danismod.danismod.entity.mobs.Elephant;
import org.danismod.danismod.entity.mobs.Lion;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    private static <T extends Entity> EntityType<T> registerEntity(
        String name,
        EntityType.EntityFactory<T> factory,
        float width, float height,
        SpawnGroup group
    ) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Danismod.MOD_ID, name));
        EntityType<T> type = EntityType.Builder.create(factory, group).dimensions(width, height).build(key);
        Registry.register(Registries.ENTITY_TYPE, key, type);
        return type;
    }

    public static final EntityType<Elephant> ELEPHANT = registerEntity("elephant", Elephant::new, 1.8f, 2.5f, SpawnGroup.CREATURE);
    public static final EntityType<Lion> LION = registerEntity("lion", Lion::new, 1f, 1.2f, SpawnGroup.CREATURE);
    public static final EntityType<Buffalo> BUFFALO = registerEntity("buffalo", Buffalo::new, 1.8f, 2.5f, SpawnGroup.CREATURE);

    public static void register() {
        FabricDefaultAttributeRegistry.register(ELEPHANT, Elephant.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LION, Lion.createMobAttributes());
        FabricDefaultAttributeRegistry.register(BUFFALO, Buffalo.createMobAttributes());
    }
}