package org.danismod.danismod.features.trees;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class PalmFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillFoliagePlacerFields(instance).apply(instance, PalmFoliagePlacer::new)
    );

    public PalmFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ModFoliagePlacers.PALM_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(
        TestableWorld world,
        FoliagePlacer.BlockPlacer replacer,
        Random random,
        TreeFeatureConfig config,
        int trunkHeight,
        TreeNode node,
        int foliageHeight,
        int radius,
        int offset
    ) {
        BlockPos center = node.getCenter().up(1);
    
        // Central tuft: 3x3 flat + center above
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos pos = center.add(dx, 0, dz);
                replacer.placeBlock(pos, config.foliageProvider.get(random, pos).with(LeavesBlock.PERSISTENT, true));
            }
        }
        replacer.placeBlock(center.up(), config.foliageProvider.get(random, center.up()).with(LeavesBlock.PERSISTENT, true));
    
        // Four main fronds: N, S, E, W
        for (Direction dir : Direction.Type.HORIZONTAL) {
            int dx = dir.getOffsetX();
            int dz = dir.getOffsetZ();
    
            BlockPos current = center;
            for (int i = 1; i <= radius; i++) {
                int dy = -(i >= radius - 1 ? 1 : 0); // slight drop at the end
                BlockPos pos = center.add(dx * i, dy, dz * i);
                replacer.placeBlock(pos, config.foliageProvider.get(random, pos).with(LeavesBlock.PERSISTENT, true));
    
                // Add one extra drooping tip leaf
                if (i == radius) {
                    BlockPos tip = pos.down();
                    replacer.placeBlock(tip, config.foliageProvider.get(random, tip).with(LeavesBlock.PERSISTENT, true));
                }
            }
        }
    }
    

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 2; // Controls vertical spread of foliage
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        // Only allow horizontal fronds at y == 0
        if (y != 0) return true;

        // Allow center
        if (dx == 0 && dz == 0) return false;

        // Allow straight-line fronds in 4 directions
        if ((dx == 0 && Math.abs(dz) <= radius) || (dz == 0 && Math.abs(dx) <= radius)) {
            return false;
        }

        // Allow drooping tip at max distance
        if ((Math.abs(dx) == radius && dz == 0) || (Math.abs(dz) == radius && dx == 0)) {
            return y == -1 ? false : true;
        }

        return true;
    }
}