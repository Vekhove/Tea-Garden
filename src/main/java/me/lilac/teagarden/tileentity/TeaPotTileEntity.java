package me.lilac.teagarden.tileentity;

import me.lilac.teagarden.TeaGarden;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TeaPotTileEntity extends TileEntity implements ITickableTileEntity {

    private ItemStackHandler itemStackHandler = createHandler();
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> itemStackHandler);
    private int counter;

    public TeaPotTileEntity() {
        super(TileEntityRegistry.TEA_POT.get());
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        // Yeah.
        // Later.
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        counter = nbt.getInt("counter");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inventory", itemStackHandler.serializeNBT());
        compound.putInt("counter", counter);
        return super.write(compound);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(7) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                switch (slot) {
                    case 0:
                        return TeaGarden.getInstance().getTeaManager().getBaseIngredients().contains(stack.getItem());
                    case 1:
                    case 2:
                        return TeaGarden.getInstance().getTeaManager().getIngredients().contains(stack.getItem());
                    default:
                        return TeaGarden.getInstance().getTeaManager().getMixers().contains(stack.getItem());
                }
            }
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap) {
        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandler.cast();
        return super.getCapability(cap);
    }
}
