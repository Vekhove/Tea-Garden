package me.lilac.teagarden.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class RootBlock extends Block {

    public RootBlock() {
        super(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().sound(SoundType.SWEET_BERRY_BUSH).zeroHardnessAndResistance().notSolid());
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.up()).isIn(Blocks.DIRT) || worldIn.getBlockState(pos.up()).isIn(Blocks.GRASS_BLOCK);
    }
}
