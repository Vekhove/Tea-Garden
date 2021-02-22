package me.lilac.teagarden.tileentity;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.item.ItemRegistry;
import me.lilac.teagarden.tea.TeaIngredient;
import me.lilac.teagarden.tea.TeaUtils;
import me.lilac.teagarden.tea.data.IngredientType;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import java.util.ArrayList;
import java.util.List;

public class TeaPotTileEntity extends TileEntity implements ITickableTileEntity {

    private ItemStackHandler itemStackHandler = createHandler();
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> itemStackHandler);
    public int brewingTime;
    private int waterAmount;
    private int brewCount;
    private int maxBrewingTime = 1600;

    public TeaPotTileEntity() {
        super(TileEntityRegistry.TEA_POT.get());
    }

    @Override
    public void tick() {
        if (this.world.isRemote) return;

        // Reset brewing time & don't do anything if there's no base item.
        if (this.itemStackHandler.getStackInSlot(0) == ItemStack.EMPTY) {
            brewingTime = -1;
            return;
        }

        // Can't brew if there's no cups!
        if (this.itemStackHandler.getStackInSlot(6).getItem() != ItemRegistry.TEA_CUP.get() || !this.itemStackHandler.getStackInSlot(7).isEmpty()) {
            brewingTime = -1;
            return;
        }

        // Reset the brewing time if the block under isn't a furnace, or if there's no water left.
        Block underBlock = this.world.getBlockState(this.pos.down()).getBlock();
        if (!(underBlock instanceof AbstractFurnaceBlock) || !this.world.getBlockState(this.pos.down()).get(BlockStateProperties.LIT) || waterAmount == 0) {
            brewingTime = -1;
            return;
        }

        // If the brewing time is 0, and all the previous conditions are met - set value.
        if (brewingTime <= 0) {
            if (underBlock == Blocks.FURNACE) {
                maxBrewingTime = 1600; // TODO: Get Config Value
            } else if (underBlock == Blocks.BLAST_FURNACE || underBlock == Blocks.SMOKER) {
                maxBrewingTime = 800; // TODO: Get Config Value
            }
            brewingTime = maxBrewingTime;
        }
        brewingTime--;

        // If the brewing has completed, check if ingredients exist and add them to the array.
        if (brewingTime == 0) {
            List<TeaIngredient> ingredients = new ArrayList<>();

            // Slot 0 should never be null.
            ItemStack baseStack = this.itemStackHandler.getStackInSlot(0);
            if (!baseStack.isEmpty()) ingredients.add(TeaUtils.getIngredientFiltered(baseStack, (ingredient) ->
                    ingredient.getItems().contains(baseStack.getItem()) && ingredient.getType() == IngredientType.BASE));

            ItemStack ingredientStack = this.itemStackHandler.getStackInSlot(1);
            if (!ingredientStack.isEmpty()) ingredients.add(TeaUtils.getIngredientFiltered(ingredientStack, (ingredient) ->
                    ingredient.getItems().contains(ingredientStack.getItem()) && ingredient.getType() == IngredientType.INGREDIENT));

            ItemStack ingredientStack1 = this.itemStackHandler.getStackInSlot(2);
            if (!ingredientStack1.isEmpty()) ingredients.add(TeaUtils.getIngredientFiltered(ingredientStack1, (ingredient) ->
                    ingredient.getItems().contains(ingredientStack1.getItem()) && ingredient.getType() == IngredientType.INGREDIENT));

            ItemStack mixerStack = this.itemStackHandler.getStackInSlot(3);
            if (!mixerStack.isEmpty()) ingredients.add(TeaUtils.getIngredientFiltered(mixerStack, (ingredient) ->
                    ingredient.getItems().contains(mixerStack.getItem()) && ingredient.getType() == IngredientType.MIXER));

            ItemStack mixerStack1 = this.itemStackHandler.getStackInSlot(4);
            if (!mixerStack1.isEmpty()) ingredients.add(TeaUtils.getIngredientFiltered(mixerStack1, (ingredient) ->
                    ingredient.getItems().contains(mixerStack1.getItem()) && ingredient.getType() == IngredientType.MIXER));

            ItemStack mixerStack2 = this.itemStackHandler.getStackInSlot(5);
            if (!mixerStack2.isEmpty()) ingredients.add(TeaUtils.getIngredientFiltered(mixerStack2, (ingredient) ->
                    ingredient.getItems().contains(mixerStack2.getItem()) && ingredient.getType() == IngredientType.MIXER));

            // Make Tea Cup with the given ingredients!
            this.itemStackHandler.setStackInSlot(7, TeaUtils.getTeaFromIngredients(this.world.rand, ingredients.toArray(new TeaIngredient[ingredients.size()])));
            brewCount++;

            // Only decrease the water if 3 teas have been brewed.
            if (brewCount == 3) {
                waterAmount--;
                brewCount = 0;
            }

            // TODO: Don't steal buckets and bottles!!!
            // Oh. Don't steal every item either, damn.
            for (int i = 0; i < 7; i++) {
                ItemStack stack = this.itemStackHandler.getStackInSlot(i);
                stack.shrink(1);
                this.itemStackHandler.setStackInSlot(i, stack.isEmpty() ? ItemStack.EMPTY : stack);
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.itemStackHandler.deserializeNBT(nbt.getCompound("Inventory"));
        this.brewingTime = nbt.getInt("BrewingTime");
        this.waterAmount = nbt.getInt("WaterAmount");
        this.brewCount = nbt.getInt("BrewCount");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Inventory", this.itemStackHandler.serializeNBT());
        compound.putInt("BrewingTime", this.brewingTime);
        compound.putInt("WaterAmount", this.waterAmount);
        compound.putInt("BrewCount", this.brewCount);
        return super.write(compound);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT compoundNBT = new CompoundNBT();
        this.write(compoundNBT);
        return new SUpdateTileEntityPacket(this.pos, 0, compoundNBT);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState blockState = this.world.getBlockState(pos);
        this.read(blockState, pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(8) {

            @Override
            protected void onContentsChanged(int slot) {
                brewingTime = -1;
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                switch (slot) {
                    case 0:
                        return TeaGarden.getInstance().getTeaManager().getBaseIngredients().contains(stack.getItem());
                    case 1:
                    case 2:
                        return true;
                    case 6:
                        return stack.getItem() == ItemRegistry.TEA_CUP.get() && stack.getTag() == null;
                    case 7:
                        return false;
                    default:
                        return TeaGarden.getInstance().getTeaManager().getMixers().contains(stack.getItem());
                }
            }
        };
    }

    public final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TeaPotTileEntity.this.brewingTime;
                case 1:
                    return TeaPotTileEntity.this.waterAmount;
                case 2:
                    return TeaPotTileEntity.this.maxBrewingTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TeaPotTileEntity.this.brewingTime = value;
                    break;
                case 1:
                    TeaPotTileEntity.this.waterAmount = value;
                    break;
                case 2:
                    TeaPotTileEntity.this.maxBrewingTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 3;
        }
    };

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap) {
        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return this.itemHandler.cast();
        return super.getCapability(cap);
    }
}
