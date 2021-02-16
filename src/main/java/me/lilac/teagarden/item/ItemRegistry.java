package me.lilac.teagarden.item;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.block.BlockRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TeaGarden.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TeaGarden.ID);

    public static final RegistryObject<Item> TEA_PLANT = ITEMS.register("tea_plant", () -> new BlockItem(BlockRegistry.TEA_PLANT.get(), getCreativeTab()));
    public static final RegistryObject<Item> TEA_LEAF = ITEMS.register("tea_leaf", () -> new TeaLeafItem(getCreativeTab()));
    public static final RegistryObject<Item> DRIED_BLACK_TEA_LEAVES = ITEMS.register("dried_black_tea_leaves", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> DRIED_GREEN_TEA_LEAVES = ITEMS.register("dried_green_tea_leaves", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> DRIED_WHITE_TEA_LEAVES = ITEMS.register("dried_white_tea_leaves", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> DRIED_OOLONG_TEA_LEAVES = ITEMS.register("dried_oolong_tea_leaves", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> MATCHA_POWDER = ITEMS.register("matcha_powder", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> CINNAMON = ITEMS.register("cinnamon", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> PEPPER_SEEDS = ITEMS.register("pepper_seeds", () -> new BlockNamedItem(BlockRegistry.PEPPER_VINE.get(), getCreativeTab()));
    public static final RegistryObject<Item> GINGER = ITEMS.register("ginger", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> STRAWBERRY = ITEMS.register("strawberry", () -> new BlockNamedItem(BlockRegistry.STRAWBERRY_PLANT.get(), getCreativeTab().food(new Food.Builder().hunger(4).saturation(2.4f).build())));
    public static final RegistryObject<Item> CINNAMON_COOKIE = ITEMS.register("cinnamon_cookie", () -> new Item(getCreativeTab().food(new Food.Builder().hunger(2).saturation(0.4f).build())));
    public static final RegistryObject<Item> GINGER_COOKIE = ITEMS.register("ginger_cookie", () -> new Item(getCreativeTab().food(new Food.Builder().hunger(2).saturation(0.4f).build())));
    public static final RegistryObject<Item> TAPIOCA = ITEMS.register("tapioca", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> MINT_LEAF = ITEMS.register("mint_leaf", () -> new BlockNamedItem(BlockRegistry.MINT_PLANT.get(), getCreativeTab()));
    public static final RegistryObject<Item> BAT_WING = ITEMS.register("bat_wing", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> THE_BOOK_OF_TEA = ITEMS.register("the_book_of_tea", () -> new Item(getCreativeTab()));
    public static final RegistryObject<Item> TEA_CUP = ITEMS.register("tea_cup", () -> new TeaCupItem(getCreativeTab()));
    public static final RegistryObject<Item> TEA_POT = ITEMS.register("tea_pot", () -> new BlockItem(BlockRegistry.TEA_POT.get(), getCreativeTab()));
    public static final RegistryObject<Item> SAUCER = ITEMS.register("saucer", () -> new BlockItem(BlockRegistry.SAUCER.get(), getCreativeTab()));
    public static final RegistryObject<Item> GINGER_ROOT = ITEMS.register("ginger_root", () -> new BlockItem(BlockRegistry.GINGER_ROOT.get(), getCreativeTab()));
    public static final RegistryObject<Item> CASSAVA_ROOT = ITEMS.register("cassava_root", () -> new BlockItem(BlockRegistry.CASSAVA_ROOT.get(), getCreativeTab()));

    private static Item.Properties getCreativeTab() {
        return new Item.Properties().group(TeaGarden.CREATIVE_TAB);
    }
}
