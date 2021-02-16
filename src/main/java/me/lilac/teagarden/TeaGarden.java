package me.lilac.teagarden;

import me.lilac.teagarden.block.BlockRegistry;
import me.lilac.teagarden.client.RenderLayerManager;
import me.lilac.teagarden.container.ContainerRegistry;
import me.lilac.teagarden.gui.TeaPotScreen;
import me.lilac.teagarden.item.ItemRegistry;
import me.lilac.teagarden.tea.TeaManager;
import me.lilac.teagarden.tileentity.TileEntityRegistry;
import me.lilac.teagarden.world.FeatureRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TeaGarden.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TeaGarden {

    private static TeaGarden instance;
    public static final String ID = "teagarden";
    public static ItemGroup CREATIVE_TAB = new TeaGardenItemGroup();
    public static final Logger LOGGER = LogManager.getLogger("Tea Garden");
    private final TeaManager teaManager = new TeaManager();

    public TeaGarden() {
        instance = this;

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockRegistry.BLOCKS.register(eventBus);
        ItemRegistry.ITEMS.register(eventBus);
        TileEntityRegistry.TILE_ENTITIES.register(eventBus);
        ContainerRegistry.CONTAINERS.register(eventBus);
        FeatureRegistry.FEATURES.register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> RenderLayerManager::new);
        ScreenManager.registerFactory(ContainerRegistry.TEA_POT.get(), TeaPotScreen::new);
    }

    @SubscribeEvent
    public void OnReload(AddReloadListenerEvent event) {
        event.addListener(this.teaManager);
    }

    public TeaManager getTeaManager() {
        return this.teaManager;
    }

    public static TeaGarden getInstance() {
        return instance;
    }
}
