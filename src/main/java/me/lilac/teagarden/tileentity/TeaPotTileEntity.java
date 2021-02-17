package me.lilac.teagarden.tileentity;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.item.ItemRegistry;
import me.lilac.teagarden.tea.TeaIngredient;
import me.lilac.teagarden.tea.TeaUtils;
import me.lilac.teagarden.tea.data.IngredientType;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeaPotTileEntity extends TileEntity implements ITickableTileEntity {

    private ItemStackHandler itemStackHandler = createHandler();
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> itemStackHandler);
    private int brewingTime;

    public TeaPotTileEntity() {
        super(TileEntityRegistry.TEA_POT.get());
    }

    @Override
    public void tick() {
        if (this.world.isRemote) return;
        if ((!this.world.getBlockState(this.pos.down()).getBlock().getTags().contains(BlockTags.CAMPFIRES.getName())
                && !this.world.getBlockState(this.pos.down(2)).getBlock().getTags().contains(BlockTags.CAMPFIRES.getName()))
                || this.itemStackHandler.getStackInSlot(0) == ItemStack.EMPTY
                || this.itemStackHandler.getStackInSlot(6) != ItemStack.EMPTY) {
            brewingTime = -1;
            return;
        }

        if (brewingTime <= 0) brewingTime = 1600;
        brewingTime--;

        if (brewingTime == 0) {
            List<TeaIngredient> ingredients = new ArrayList<>();

            // Slot 0 should never be null.
            ingredients.add(TeaUtils.getIngredientFiltered((ingredient) -> {
                Item item = this.itemStackHandler.getStackInSlot(0).getItem();
                return ingredient.getItems().contains(item) && ingredient.getType() == IngredientType.BASE;
            }));

            Item item1 = this.itemStackHandler.getStackInSlot(1).getItem();
            if (item1 != Items.AIR) ingredients.add(TeaUtils.getIngredientFiltered((ingredient) -> {
                return ingredient.getItems().contains(item1) && ingredient.getType() == IngredientType.INGREDIENT;
            }));

            Item item2 = this.itemStackHandler.getStackInSlot(2).getItem();
            if (item2 != Items.AIR) ingredients.add(TeaUtils.getIngredientFiltered((ingredient) -> {
                return ingredient.getItems().contains(item2) && ingredient.getType() == IngredientType.INGREDIENT;
            }));

            Item item3 = this.itemStackHandler.getStackInSlot(3).getItem();
            ingredients.add(TeaUtils.getIngredientFiltered((ingredient) -> {
                return ingredient.getItems().contains(item3) && ingredient.getType() == IngredientType.MIXER;
            }));

            Item item4 = this.itemStackHandler.getStackInSlot(4).getItem();
            if (item4 != Items.AIR) ingredients.add(TeaUtils.getIngredientFiltered((ingredient) -> {
                return ingredient.getItems().contains(item4) && ingredient.getType() == IngredientType.MIXER;
            }));

            Item item5 = this.itemStackHandler.getStackInSlot(5).getItem();
            if (item5 != Items.AIR) ingredients.add(TeaUtils.getIngredientFiltered((ingredient) -> {
                return ingredient.getItems().contains(item5) && ingredient.getType() == IngredientType.MIXER;
            }));

            ingredients.forEach((ing) -> {
                System.out.println("Found Ing: " + ing.getType().toString());
            });

            this.itemStackHandler.setStackInSlot(6, TeaUtils.getTeaFromIngredients(ingredients.toArray(new TeaIngredient[ingredients.size()])));
        }
    }

    public boolean isBrewing() {
        return brewingTime > 0;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.itemStackHandler.deserializeNBT(nbt.getCompound("Inventory"));
        this.brewingTime = nbt.getInt("BrewingTime");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Inventory", this.itemStackHandler.serializeNBT());
        compound.putInt("BrewingTime", this.brewingTime);
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
                    case 6:
                        return true;
                    default:
                        return TeaGarden.getInstance().getTeaManager().getMixers().contains(stack.getItem());
                }
            }
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap) {
        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return this.itemHandler.cast();
        return super.getCapability(cap);
    }
}
