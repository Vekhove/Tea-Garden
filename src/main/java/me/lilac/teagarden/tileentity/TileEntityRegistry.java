package me.lilac.teagarden.tileentity;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.block.BlockRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TeaGarden.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityRegistry {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TeaGarden.ID);

    public static final RegistryObject<TileEntityType<TeaPotTileEntity>> TEA_POT = TILE_ENTITIES.register("tea_pot", () -> TileEntityType.Builder.create(TeaPotTileEntity::new, BlockRegistry.TEA_POT.get()).build(null));
}
