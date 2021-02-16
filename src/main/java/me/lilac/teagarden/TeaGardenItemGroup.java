package me.lilac.teagarden;

import me.lilac.teagarden.item.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Arrays;

public class TeaGardenItemGroup extends ItemGroup {

    public TeaGardenItemGroup() {
        super("teagarden");
    }

    @Override
    public ItemStack createIcon() {
        return ItemRegistry.TEA_CUP.get().getDefaultInstance();
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        items.addAll(Arrays.asList(
                ItemRegistry.THE_BOOK_OF_TEA.get().getDefaultInstance(),
                ItemRegistry.TEA_PLANT.get().getDefaultInstance(),
                ItemRegistry.GINGER_ROOT.get().getDefaultInstance(),
                ItemRegistry.CASSAVA_ROOT.get().getDefaultInstance(),
                ItemRegistry.PEPPER_SEEDS.get().getDefaultInstance(),
                ItemRegistry.STRAWBERRY.get().getDefaultInstance(),
                ItemRegistry.MINT_LEAF.get().getDefaultInstance(),
                ItemRegistry.GINGER.get().getDefaultInstance(),
                ItemRegistry.TAPIOCA.get().getDefaultInstance(),
                ItemRegistry.TEA_LEAF.get().getDefaultInstance(),
                ItemRegistry.DRIED_BLACK_TEA_LEAVES.get().getDefaultInstance(),
                ItemRegistry.DRIED_GREEN_TEA_LEAVES.get().getDefaultInstance(),
                ItemRegistry.DRIED_WHITE_TEA_LEAVES.get().getDefaultInstance(),
                ItemRegistry.DRIED_OOLONG_TEA_LEAVES.get().getDefaultInstance(),
                ItemRegistry.MATCHA_POWDER.get().getDefaultInstance(),
                ItemRegistry.CINNAMON_COOKIE.get().getDefaultInstance(),
                ItemRegistry.GINGER_COOKIE.get().getDefaultInstance(),
                ItemRegistry.BAT_WING.get().getDefaultInstance(),
                ItemRegistry.SAUCER.get().getDefaultInstance(),
                ItemRegistry.TEA_POT.get().getDefaultInstance(),
                ItemRegistry.TEA_CUP.get().getDefaultInstance()
        ));
    }
}
