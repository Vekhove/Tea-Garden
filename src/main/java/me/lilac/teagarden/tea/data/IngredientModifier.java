package me.lilac.teagarden.tea.data;

public class IngredientModifier {

    private ModifierType type;
    private int value;
    private int max;
    private int min;
    private int valueChance;
    private String changeEquation;
    private int changeChance;

    public IngredientModifier(ModifierType type) {
        this.type = type;
    }

    public IngredientModifier(ModifierType type, int value) {
        this(type);
        this.value = value;
    }

    public IngredientModifier(ModifierType type, int min, int max) {
        this(type);
        this.min = min;
        this.max = max;
    }

    public ModifierType getType() {
        return type;
    }

    public void setType(ModifierType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getValueChance() {
        return valueChance;
    }

    public void setValueChance(int valueChance) {
        this.valueChance = valueChance;
    }

    public String getChangeEquation() {
        return changeEquation;
    }

    public void setChangeEquation(String changeEquation) {
        this.changeEquation = changeEquation;
    }

    public int getChangeChance() {
        return changeChance;
    }

    public void setChangeChance(int changeChance) {
        this.changeChance = changeChance;
    }
}
