package me.lilac.teagarden.block;

import me.lilac.teagarden.TeaGarden;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TeaGarden.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TeaGarden.ID);

    public static final RegistryObject<Block> TEA_PLANT = BLOCKS.register("tea_plant", TeaPlantBlock::new);
    public static final RegistryObject<Block> PEPPER_VINE = BLOCKS.register("pepper_vine", PepperVineBlock::new);
    public static final RegistryObject<Block> GINGER_ROOT = BLOCKS.register("ginger_root", RootBlock::new);
    public static final RegistryObject<Block> CASSAVA_ROOT = BLOCKS.register("cassava_root", CassavaRootBlock::new);
    public static final RegistryObject<Block> STRAWBERRY_PLANT = BLOCKS.register("strawberry_plant", StrawberryPlantBlock::new);
    public static final RegistryObject<Block> MINT_PLANT = BLOCKS.register("mint_plant", MintPlantBlock::new);
    public static final RegistryObject<Block> TEA_LEAF_PILE = BLOCKS.register("tea_leaf_pile", TeaLeafPileBlock::new);
    public static final RegistryObject<Block> TEA_POT = BLOCKS.register("tea_pot", TeaPotBlock::new);
    public static final RegistryObject<Block> SAUCER = BLOCKS.register("saucer", () -> new Block(Properties.create(Material.GLASS).sound(SoundType.GLASS).doesNotBlockMovement().notSolid()));

    private static Item.Properties getCreativeTab() {
        return new Item.Properties().group(TeaGarden.CREATIVE_TAB);
    }
}
