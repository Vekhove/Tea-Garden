package me.lilac.teagarden.tea;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.item.ItemRegistry;
import me.lilac.teagarden.tea.data.IngredientModifier;
import me.lilac.teagarden.tea.data.IngredientType;
import me.lilac.teagarden.tea.data.PositionedName;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.StringNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

    public static TeaIngredient getIngredientFiltered(ItemStack itemStack, Predicate<TeaIngredient> predicate) {
        TeaManager teaManager = TeaGarden.getInstance().getTeaManager();
        return teaManager.getTeaIngredients().stream().filter(predicate).findFirst().orElse(new TeaIngredient.Builder().withType(IngredientType.INGREDIENT).addItem(itemStack.getItem()).build());
    }

    // Uhhh, sorry languages that grammar differently.
    public static String getTeaName(Map<String, Integer> names) {
        String adverb = null;
        List<String> adjectives = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        String tea = null;

        // Only set the values if it's the first time.
        for (String name : names.keySet()) {
            switch (names.get(name)) {
                case 0:
                    if (adverb == null) adverb = name;
                    break;
                case 1:
                    if (adjectives.size() != 2) {
                        if (!adjectives.isEmpty() && TeaGarden.getInstance().getTeaManager().getBlockedIngredientNames().contains(name)) break;
                        adjectives.add(name);
                    }
                    break;
                case 2:
                    if (verbs.size() != 3) verbs.add(name);
                    break;
                case 3:
                    if (tea == null) tea = name;
                    break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        if (adverb != null) stringBuilder.append(adverb).append(" ");
        if (adjectives.size() >= 1) stringBuilder.append(adjectives.get(0)).append(" ");
        if (adjectives.size() == 2) {
            stringBuilder.append("and ");
            stringBuilder.append(adjectives.get(1)).append(" ");
        }

        if (verbs.size() <= 2) {
            stringBuilder.append(verbs.get(0));
        }

        if (verbs.size() > 2) {
            stringBuilder.append(verbs.get(0));
            stringBuilder.append(" and ");
            stringBuilder.append(verbs.get(1));
        }

        if (tea != null) {
            stringBuilder.append(" ").append(tea);
        } else {
            stringBuilder.append(" Tea");
        }

        return stringBuilder.toString();
    }

    public static ItemStack getTeaFromIngredients(Random random, TeaIngredient... ingredients) {
        ItemStack itemStack = new ItemStack(ItemRegistry.TEA_CUP.get());
        CompoundNBT nbt = itemStack.getOrCreateTag();
        CompoundNBT display = new CompoundNBT();
        ListNBT effectsNbt = new ListNBT();

        // Default Values
        int speed = 32;
        int amplifierMod = 0;
        int durationMod = 0;
        int brewability = 0;
        int hunger = 0;
        int saturation = 0;
        Map<String, Integer> names = new HashMap<>();
        List<EffectInstance> effects = new ArrayList<>();

        for (TeaIngredient ingredient : ingredients) {
            brewability += ingredient.getBrewability();
            hunger += ingredient.getHunger();
            saturation += ingredient.getSaturation();

            for (PositionedName name : ingredient.getNames()) names.put(name.getName(), name.getPosition());
            if (ingredient.getNames().isEmpty() && ingredient.getType() == IngredientType.INGREDIENT) {
                names.put(ingredient.getItems().get(0).getName().getString(), 2);
            }

            for (IngredientModifier modifier : ingredient.getModifiers()) {
                boolean negative = modifier.getMax() > 0;
                int value = modifier.getValue() != 0 ? modifier.getValue() : random.nextInt(Math.abs((modifier.getMax()) - Math.abs(modifier.getMin())) + 1) + modifier.getMin();
                if (negative) value = value * -1;

                int chance = modifier.getValueChance();

                if (chance >= random.nextInt(100)) {
                    switch (modifier.getType()) {
                        case SPEED:
                            speed += value;
                            break;
                        case POTENCY:
                            amplifierMod += value;
                            break;
                        case DURATION:
                            durationMod += value;
                            break;
                    }
                }
            }

            effects.addAll(ingredient.getEffects());
        }

        display.putString("Name", "[{\"text\":\"" + getTeaName(names) + "\",\"italic\":false}]");
        nbt.put("display", display);

        for (EffectInstance effectInstance : effects) {
            Effect effect = effectInstance.getPotion();
            int amplifier = effectInstance.getAmplifier();
            int duration = effectInstance.getDuration();

            // Can't use CustomPotionEffects with string ids??
            // TODO: Manage NBT myself instead of depending on PotionUtils?
            CompoundNBT effectNbt = new CompoundNBT();
            effectNbt.putInt("Id", Effect.getId(effect));
            effectNbt.putInt("Amplifier", amplifier + amplifierMod);
            effectNbt.putInt("Duration", duration + durationMod);
            effectsNbt.add(effectNbt);
        }

        nbt.put("CustomPotionEffects", effectsNbt);
        nbt.putInt("Speed", speed);

        itemStack.setTag(nbt);
        return itemStack;
    }
}
