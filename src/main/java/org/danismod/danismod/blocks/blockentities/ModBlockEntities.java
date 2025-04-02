package org.danismod.danismod.blocks.blockentities;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.danismod.danismod.Danismod;
import org.danismod.danismod.blocks.ModBlocks;


public class ModBlockEntities {

    public static final BlockEntityType<IvoryGrinderBlockEntity> IVORY_GRINDER_BLOCK_ENTITY_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Danismod.MOD_ID, "ivory_grinder"),
            FabricBlockEntityTypeBuilder.create(IvoryGrinderBlockEntity::new, ModBlocks.IVORY_GRINDER).build()
    );
    public static void register() {}
}
