package me.lilac.teagarden.block;

import me.lilac.teagarden.item.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import java.util.Random;

public class TeaLeafPileBlock extends Block {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_2;
    public static final BooleanProperty OUTSIDE = BooleanProperty.create("outside");
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public TeaLeafPileBlock() {
        super(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().sound(SoundType.PLANT).zeroHardnessAndResistance().notSolid());
        this.setDefaultState(this.getStateContainer().getBaseState().with(AGE, 0).with(OUTSIDE, false));
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(ItemRegistry.TEA_LEAF.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(ItemRegistry.TEA_LEAF.get());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE, OUTSIDE);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.get(AGE) < 2;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        int age = state.get(AGE);

        if (age < 2 && worldIn.getLightSubtracted(pos.up(), 0) >= 9 && ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(15) == 0)) {
            worldIn.setBlockState(pos, state.with(AGE, age + 1).with(OUTSIDE, worldIn.canBlockSeeSky(pos)), 2);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return worldIn.canBlockSeeSky(currentPos) ? stateIn.with(OUTSIDE, true) : stateIn.with(OUTSIDE, false);
    }
}
