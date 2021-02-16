package me.lilac.teagarden.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class CassavaRootBlock extends RootBlock {

    public CassavaRootBlock() {
        super();
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return (worldIn.getBlockState(pos.up()).isIn(Blocks.DIRT)
                || worldIn.getBlockState(pos.up()).isIn(Blocks.GRASS_BLOCK))
                && worldIn.getBlockState(pos.up(2)).getBlock().getTags().contains(BlockTags.LOGS.getName());
    }
}
