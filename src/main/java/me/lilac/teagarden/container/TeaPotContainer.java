package me.lilac.teagarden.container;

import me.lilac.teagarden.block.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TeaPotContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    public TeaPotContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ContainerRegistry.TEA_POT.get(), windowId);
        tileEntity = world.getTileEntity(pos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new SlotItemHandler(handler, 0, 21, 31));
                addSlot(new SlotItemHandler(handler, 1, 47, 13));
                addSlot(new SlotItemHandler(handler, 2, 47, 49));
                addSlot(new SlotItemHandler(handler, 3, 70, 31));
                addSlot(new SlotItemHandler(handler, 4, 93, 31));
                addSlot(new SlotItemHandler(handler, 5, 116, 31));
                addSlot(new SlotItemHandler(handler, 6, 151, 31));
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
        return ItemStack.EMPTY;
    }
}
