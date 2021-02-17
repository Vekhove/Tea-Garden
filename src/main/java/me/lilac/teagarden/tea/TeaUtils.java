package me.lilac.teagarden.tea;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.item.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import java.util.function.Predicate;

public class TeaUtils {

    public static TeaIngredient getIngredientFromItemStack(ItemStack stack) {
        return getIngredientFromItem(stack.getItem());
    }

    public static TeaIngredient getIngredientFromItem(Item item) {
        TeaManager teaManager = TeaGarden.getInstance().getTeaManager();

        for (TeaIngredient ingredient : teaManager.getTeaIngredients()) {
            if (ingredient.getItems().contains(item)) {
                return ingredient;
            }
        }

        return null;
    }

    public static TeaIngredient getIngredientFiltered(Predicate<TeaIngredient> predicate) {
        TeaManager teaManager = TeaGarden.getInstance().getTeaManager();
        return teaManager.getTeaIngredients().stream().filter(predicate).findFirst().orElse(null);
    }

    public static ItemStack getTeaFromIngredients(TeaIngredient... ingredients) {
        ItemStack itemStack = new ItemStack(ItemRegistry.TEA_CUP.get());
        CompoundNBT compound = itemStack.getOrCreateTag();
        compound.putInt("Speed", 5);
        itemStack.setTag(compound);
        return itemStack;
    }
}
