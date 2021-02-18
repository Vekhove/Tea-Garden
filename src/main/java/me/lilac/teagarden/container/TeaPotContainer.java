package me.lilac.teagarden.container;

import me.lilac.teagarden.block.BlockRegistry;
import me.lilac.teagarden.tileentity.TeaPotTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TeaPotContainer extends Container {

    private TeaPotTileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private IIntArray fields;

    public TeaPotContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ContainerRegistry.TEA_POT.get(), windowId);
        this.tileEntity = (TeaPotTileEntity) world.getTileEntity(pos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);
        this.fields = this.tileEntity.fields;
        assertIntArraySize(this.fields, 2);
        this.trackIntArray(this.fields);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new SlotItemHandler(handler, 0, 80, 12));
                addSlot(new SlotItemHandler(handler, 1, 58, 20));
                addSlot(new SlotItemHandler(handler, 2, 102, 20));
                addSlot(new SlotItemHandler(handler, 3, 58, 49));
                addSlot(new SlotItemHandler(handler, 4, 80, 57));
                addSlot(new SlotItemHandler(handler, 5, 102, 49));
                addSlot(new SlotItemHandler(handler, 6, 150, 35));
            });

            addPlayerSlots(9, 8, 84, 3, 9); // Inventory
            addPlayerSlots(0, 8, 142, 1, 9); // Hotbar
        }
    }

    private void addPlayerSlots(int index, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                addSlot(new SlotItemHandler(this.playerInventory, index, x, y));
                x += 18;
                index++;
            }
            x = 8;
            y += 18;
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, BlockRegistry.TEA_POT.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        // Shift Click Manager
        // Place datapack items in correct places,
        // Place other items wherever - Make tea: Weird Crafting Table Tea
        this.fields.set(0, -1);
        return ItemStack.EMPTY;
    }

    public int getProgress() {
        int max = 200;
        int current = max - this.fields.get(0);
        return max != 0 && current != 0 && current < max ? current * 24 / max : 0;
    }

    public int getWaterLevel() {
        int max = 18;
        int current = this.fields.get(1);
        return max != 0 && current != 8 ? current * 47 / max : 0;
    }

    public int getCurrentWater() {
        return this.fields.get(1);
    }
}
