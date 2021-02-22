package me.lilac.teagarden.block;

import me.lilac.teagarden.container.TeaPotContainer;
import me.lilac.teagarden.tileentity.TeaPotTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

public class TeaPotBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);

    public TeaPotBlock() {
        super(Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.2F).notSolid());
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TeaPotTileEntity();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) return ActionResultType.SUCCESS;
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TeaPotTileEntity) {
            TeaPotTileEntity teaPot = (TeaPotTileEntity) tileEntity;
            ItemStack heldItem = player.getHeldItem(handIn);

            if (heldItem.getItem() == Items.WATER_BUCKET) {
                if (teaPot.fields.get(1) > 18 - 9) return ActionResultType.FAIL;
                teaPot.fields.set(1, teaPot.fields.get(1) + 9);
                player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.CONSUME;
            } else if (heldItem.getItem() == Items.POTION) {
                if (heldItem.hasTag() && heldItem.getTag().getString("Potion").equalsIgnoreCase("minecraft:water")) {
                    if (teaPot.fields.get(1) > 18 - 3) return ActionResultType.FAIL;
                    teaPot.fields.set(1, teaPot.fields.get(1) + 3);
                    player.setHeldItem(handIn, new ItemStack(Items.GLASS_BOTTLE));
                    player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResultType.CONSUME;
                }
            }

            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("screen.teagarden.tea_pot");
                }

                @Override
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                    return new TeaPotContainer(p_createMenu_1_, worldIn, pos, p_createMenu_2_, p_createMenu_3_);
                }
            };

            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack item = handler.getStackInSlot(i);
                    if (!item.isEmpty()) worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), item));
                }

                worldIn.removeTileEntity(pos);
            });
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
