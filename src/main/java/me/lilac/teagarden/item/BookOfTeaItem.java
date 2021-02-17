package me.lilac.teagarden.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class BookOfTeaItem extends Item {

    public BookOfTeaItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return ActionResultType.func_233537_a_(context.getWorld().isRemote);
    }
}
