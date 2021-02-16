package me.lilac.teagarden.client;

import me.lilac.teagarden.block.BlockRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class RenderLayerManager {

    public RenderLayerManager() {
        RenderTypeLookup.setRenderLayer(BlockRegistry.TEA_PLANT.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockRegistry.TEA_LEAF_PILE.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockRegistry.CASSAVA_ROOT.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockRegistry.GINGER_ROOT.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockRegistry.MINT_PLANT.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockRegistry.PEPPER_VINE.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BlockRegistry.STRAWBERRY_PLANT.get(), RenderType.getCutoutMipped());
    }
}
