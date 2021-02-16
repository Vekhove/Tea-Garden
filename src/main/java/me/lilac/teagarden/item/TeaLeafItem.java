package me.lilac.teagarden.item;

import me.lilac.teagarden.block.BlockRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class TeaLeafItem extends Item {

    public TeaLeafItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getItem().getCount() < 5 && !context.getPlayer().isCreative()) return ActionResultType.FAIL;

        context.getItem().shrink(5);
        context.getWorld().setBlockState(context.getPos().add(0, 1, 0), BlockRegistry.TEA_LEAF_PILE.get().getDefaultState());
        return ActionResultType.func_233537_a_(context.getWorld().isRemote);
    }

}
