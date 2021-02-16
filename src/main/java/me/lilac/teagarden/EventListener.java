package me.lilac.teagarden;

import me.lilac.teagarden.item.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TeaGarden.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventListener {

    @SubscribeEvent
    public static void onBlockClicked(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() instanceof AxeItem) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            BlockState state = world.getBlockState(pos);
            if (state.getBlock().getTags().contains(BlockTags.LOGS.getName())) {
                world.addEntity(new ItemEntity(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(ItemRegistry.CINNAMON.get(), world.rand.nextBoolean() ? 0 : 1)));
            }
        }
    }
}
