package me.lilac.teagarden.tea;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.tea.data.IngredientModifier;
import me.lilac.teagarden.tea.data.IngredientType;
import me.lilac.teagarden.tea.data.ModifierType;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeaManager extends JsonReloadListener {

    private List<TeaIngredient> teaIngredients;
    private List<String> blockedIngredientNames;

    public TeaManager() {
        super(new Gson(), "ingredients");
        this.teaIngredients = new ArrayList<>();
        this.blockedIngredientNames = new ArrayList<>();
    }

    public List<Item> getBaseIngredients() {
        List<Item> ingredients = new ArrayList<>();

        teaIngredients.forEach((ingredient) -> {
            if (ingredient.getType() == IngredientType.BASE) ingredients.addAll(ingredient.getItems());

        });

        return ingredients;
    }

    public List<Item> getIngredients() {
        List<Item> ingredients = new ArrayList<>();

        teaIngredients.forEach((ingredient) -> {
            if (ingredient.getType() == IngredientType.INGREDIENT) ingredients.addAll(ingredient.getItems());
        });

        return ingredients;
    }

    public List<Item> getMixers() {
        List<Item> ingredients = new ArrayList<>();

        teaIngredients.forEach((ingredient) -> {
            if (ingredient.getType() == IngredientType.MIXER) ingredients.addAll(ingredient.getItems());
        });

        return ingredients;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                objectIn.forEach((location, jsonElement) -> {
                    JsonObject ingredient = jsonElement.getAsJsonObject();
                    if (!ingredient.has("id")) {
                        TeaGarden.LOGGER.error("File " + location.getPath() + ".json is missing field 'id'!");
                        return;
                    }

                    deserializeIngredient(location, ingredient);
                });
            } catch (InterruptedException e) {
                TeaGarden.LOGGER.error(e.getMessage());
            }
        }).start();
    }

    private void deserializeIngredient(ResourceLocation location, JsonObject ingredient) {
        TeaIngredient.Builder builder = new TeaIngredient.Builder();

        ITextComponent itemName = null;

        // Item IDs
        for (JsonElement element : ingredient.getAsJsonArray("id")) {
            String namespacedId = element.getAsString().replace("items/", "");
            String[] namespacedIdSplit = namespacedId.split(":");
            String namespace = namespacedIdSplit[0];
            String id = namespacedIdSplit[1];
            if (namespacedId.startsWith("#")) {
                ITag<Item> tag = ItemTags.getCollection().get(new ResourceLocation(namespace.substring(1), id));
                if (tag == null) {
                    TeaGarden.LOGGER.error("There was an error parsing the tag: " + namespace + ":" + id + " in " + location.getPath() + ".json");
                    continue;
                }

                List<Item> items = tag.getAllElements();
                if (!items.isEmpty()) {
                    itemName = items.get(0).getName();
                    builder.addItems(items);
                }

            } else {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(namespace, id));
                itemName = item.getName();
                builder.addItem(item);
            }
        }

        // Type
        IngredientType type = ingredient.has("type") ? IngredientType.valueOf(ingredient.get("type").getAsString().toUpperCase()) : IngredientType.INGREDIENT;
        if (!ingredient.has("type")) {
            TeaGarden.LOGGER.warn("Element 'type' not found in " + location.getPath() + ". Using type: INGREDIENT");
        }
        builder.withType(type);

        // Brewability
        builder.withBrewability(ingredient.has("brewability") ? ingredient.get("brewability").getAsInt() : 0);

        // Hunger
        int hunger = ingredient.has("hunger") ? ingredient.get("hunger").getAsInt() : 0;
        builder.withHunger(hunger);

        // Saturation
        int saturation = ingredient.has("saturation") ? ingredient.get("saturation").getAsInt() : 0;
        builder.withSaturation(saturation);

        // Color
        Color color = Color.decode(ingredient.has("color") ? ingredient.get("color").getAsString() : "#FFFFFF");
        builder.withColor(color);

        // Names
        if (ingredient.has("names")) {
            JsonArray names = ingredient.getAsJsonArray("names");
            for (JsonElement name : names) {
                JsonObject nameObj = name.getAsJsonObject();

                String nameStr = nameObj.has("name") ? nameObj.get("name").getAsString() : itemName.getString();
                int position = 0;

                if (nameObj.has("position")) {
                    position = nameObj.get("position").getAsInt();
                } else {
                    switch (type) {
                        case BASE:
                            position = 3;
                            break;
                        case INGREDIENT:
                            position = 2;
                            break;
                        case MIXER:
                            position = 1;
                            break;
                    }
                }

                builder.addName(nameStr, position);
                if (nameObj.has("blocked") && nameObj.get("blocked").getAsBoolean()) blockedIngredientNames.add(nameStr);
            }
        }

        // Effects
        if (ingredient.has("effects")) {
            JsonArray effects = ingredient.getAsJsonArray("effects");
            for (JsonElement effect : effects) {
                JsonObject effectObj = effect.getAsJsonObject();

                String id = effectObj.get("id").getAsString();
                int duration = effectObj.has("duration") ? effectObj.get("duration").getAsInt() : 100;
                int amplifier = effectObj.has("amplifier") ? effectObj.get("amplifier").getAsInt() : 0;

                EffectInstance effectInstance = new EffectInstance(Registry.EFFECTS.getOrDefault(new ResourceLocation(id)), duration, amplifier);
                builder.addEffect(effectInstance);
            }
        }

        // Modifiers
        if (ingredient.has("modifiers")) {
            JsonArray modifiers = ingredient.getAsJsonArray("modifiers");
            for (JsonElement modifier : modifiers) {
                JsonObject modifierObj = modifier.getAsJsonObject();

                ModifierType modifierType = ModifierType.valueOf(modifierObj.get("type").getAsString().toUpperCase());
                int value = modifierObj.has("value") ? modifierObj.get("value").getAsInt() : 0;
                int chance = modifierObj.has("chance") ? modifierObj.get("chance").getAsInt() : 100;
                int min = modifierObj.has("min") ? modifierObj.get("min").getAsInt() : 0;
                int max = modifierObj.has("max") ? modifierObj.get("max").getAsInt() : 0;

                IngredientModifier ingredientModifier = new IngredientModifier(modifierType, value);
                ingredientModifier.setValueChance(chance);
                ingredientModifier.setMin(min);
                ingredientModifier.setMin(max);
                builder.addModifier(ingredientModifier);
            }
        }

        TeaIngredient teaIngredient = builder.build();
        this.teaIngredients.add(teaIngredient);
    }

    public List<TeaIngredient> getTeaIngredients() {
        return teaIngredients;
    }

    public List<String> getBlockedIngredientNames() {
        return blockedIngredientNames;
    }
}
