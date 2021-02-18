package me.lilac.teagarden.tea;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.item.ItemRegistry;
import me.lilac.teagarden.tea.data.PositionedName;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.StringNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        ListNBT list = new ListNBT();
        CompoundNBT effect = new CompoundNBT();
        effect.putInt("Id", 1);
        effect.putInt("Amplifier", 1);
        effect.putInt("Duration", 100);

        CompoundNBT effect1 = new CompoundNBT();
        effect1.putInt("Id", 2);
        effect1.putInt("Amplifier", 2);
        effect1.putInt("Duration", 500);
        list.add(effect);
        list.add(effect1);
        compound.put("CustomPotionEffects", list);

        CompoundNBT display = new CompoundNBT();
        ListNBT names = new ListNBT();
        CompoundNBT name1 = new CompoundNBT();


        for (TeaIngredient ingredient : ingredients) {
            /*for (PositionedName name : ingredient.getNames()) {
                name1.putString("text", name.getName() + " Tea");
                name1.putBoolean("italic", false);
                name1.putString("color", "#C0FFEE");
                System.out.println(name.getName());
            }*/
        }

        names.add(name1);
        display.put("Name", names);
        // {display:{Name:'[{"text":"Test","italic":false,"color":"#33ff99"}]'}}
        // Names are strings???????
        compound.put("display", display);
        itemStack.setTag(compound);

        return itemStack;
    }
}
