package me.lilac.teagarden.tea;

import me.lilac.teagarden.tea.data.IngredientModifier;
import me.lilac.teagarden.tea.data.IngredientType;
import me.lilac.teagarden.tea.data.PositionedName;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeaIngredient {

    private List<Item> items;
    private IngredientType type;
    private int brewability;
    private int hunger;
    private int saturation;
    private Color color;
    private String description;
    private List<PositionedName> names;
    private List<EffectInstance> effects;
    private List<IngredientModifier> modifiers;

    public TeaIngredient() {
        this.items = new ArrayList<>();
        this.type = IngredientType.BASE;
        this.names = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.modifiers = new ArrayList<>();
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setType(IngredientType type) {
        this.type = type;
    }

    public IngredientType getType() {
        return type;
    }

    public void setBrewability(int brewability) {
        this.brewability = brewability;
    }

    public int getBrewability() {
        return brewability;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHunger() {
        return hunger;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setNames(List<PositionedName> names) {
        this.names = names;
    }

    public List<PositionedName> getNames() {
        return names;
    }

    public void setEffects(List<EffectInstance> effects) {
        this.effects = effects;
    }

    public List<EffectInstance> getEffects() {
        return effects;
    }

    public void setModifiers(List<IngredientModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<IngredientModifier> getModifiers() {
        return modifiers;
    }

    public static class Builder {
        TeaIngredient ingredient;

        public Builder() {
            this.ingredient = new TeaIngredient();
        }

        public TeaIngredient.Builder addItem(Item... item) {
            this.ingredient.getItems().addAll(Arrays.asList(item));
            return this;
        }

        public TeaIngredient.Builder addItems(List<Item> items) {
            this.ingredient.getItems().addAll(items);
            return this;
        }

        public TeaIngredient.Builder withType(IngredientType type) {
            this.ingredient.setType(type);
            return this;
        }

        public TeaIngredient.Builder withBrewability(int brewability) {
            this.ingredient.brewability = brewability;
            return this;
        }

        public TeaIngredient.Builder withHunger(int hunger) {
            this.ingredient.setHunger(hunger);
            return this;
        }

        public TeaIngredient.Builder withSaturation(int saturation) {
            this.ingredient.setSaturation(saturation);
            return this;
        }

        public TeaIngredient.Builder withColor(Color color) {
            this.ingredient.color = color;
            return this;
        }

        public TeaIngredient.Builder addName(String name, int position) {
            this.ingredient.names.add(new PositionedName(name, position));
            return this;
        }

        public TeaIngredient.Builder addEffect(EffectInstance effect) {
            this.ingredient.effects.add(effect);
            return this;
        }

        public TeaIngredient.Builder addModifier(IngredientModifier modifier) {
            this.ingredient.modifiers.add(modifier);
            return this;
        }

        public TeaIngredient build() {
            return this.ingredient;
        }
    }
}
