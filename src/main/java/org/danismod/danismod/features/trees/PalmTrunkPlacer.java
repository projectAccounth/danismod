package org.danismod.danismod.features.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer.TreeNode;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class PalmTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<PalmTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
		instance -> fillTrunkPlacerFields(instance).apply(instance, PalmTrunkPlacer::new)
	);

    public PalmTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTrunkPlacerTypes.PALM;
    }


    @Override
    public List<TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random,
            int height, BlockPos startPos, TreeFeatureConfig config) 
    {
        List<TreeNode> foliage = new ArrayList<>();
        BlockPos.Mutable pos = new BlockPos.Mutable(startPos.getX(), startPos.getY(), startPos.getZ());
        Direction leanDir = Direction.Type.HORIZONTAL.random(random);
        
        for (int i = 0; i < height; i++) {
            if (i > height / 2) pos.move(leanDir);
            pos.move(Direction.UP);
            getAndSetState(world, replacer, random, pos, config);
            // setToDirt(world, replacer, random, pos, config); // Test
        }

        foliage.add(new TreeNode(pos.toImmutable(), 0, false));
        return foliage;
    }
}