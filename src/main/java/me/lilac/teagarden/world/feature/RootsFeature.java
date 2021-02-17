package me.lilac.teagarden.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import java.util.Random;

public class RootsFeature extends Feature<BlockClusterFeatureConfig> {

    public RootsFeature(Codec<BlockClusterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        BlockState blockstate = config.stateProvider.getBlockState(rand, pos);
        BlockPos blockpos = config.field_227298_k_ ? reader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos) : pos;

        int i = 0;
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for(int j = 0; j < config.tryCount; ++j) {
            mutablePos.setAndOffset(blockpos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
            BlockPos blockPos = mutablePos.down();
            BlockState blockState = reader.getBlockState(blockPos);
            if ((reader.isAirBlock(mutablePos) || config.isReplaceable && reader.getBlockState(mutablePos).getMaterial().isReplaceable())
                    && blockstate.isValidPosition(reader, mutablePos) && (config.whitelist.isEmpty() || config.whitelist.contains(blockState.getBlock()))
                    && !config.blacklist.contains(blockState) && (!config.requiresWater || reader.getFluidState(blockPos.west()).isTagged(FluidTags.WATER)
                    || reader.getFluidState(blockpos.east()).isTagged(FluidTags.WATER) || reader.getFluidState(blockPos.north()).isTagged(FluidTags.WATER)
                    || reader.getFluidState(blockPos.south()).isTagged(FluidTags.WATER))) {
                config.blockPlacer.place(reader, blockPos, blockstate, rand);
                ++i;
            }
        }

        return i > 0;
    }
}
