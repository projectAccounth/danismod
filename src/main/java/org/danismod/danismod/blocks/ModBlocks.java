package org.danismod.danismod.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.danismod.danismod.Danismod;

import java.util.function.Function;

public class ModBlocks {
    @SuppressWarnings("unused")
    private static void registerInstance(String name, Block block) {
        Registry.register(Registries.BLOCK, Identifier.of(Danismod.MOD_ID, name), block);
    }
    private static Block register(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of("danismod", path);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }

    public static final Block IVORY_GRINDER = register("ivory_grinder",
            IvoryGrinderBlock::new,
            AbstractBlock.Settings.create()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static void initialize() {

    }
}
