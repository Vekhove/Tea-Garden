package me.lilac.teagarden.world;

import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.block.BlockRegistry;
import me.lilac.teagarden.block.MintPlantBlock;
import me.lilac.teagarden.block.PepperVineBlock;
import me.lilac.teagarden.block.StrawberryPlantBlock;
import me.lilac.teagarden.block.TeaPlantBlock;
import me.lilac.teagarden.world.feature.RootsFeature;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = TeaGarden.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FeatureRegistry {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TeaGarden.ID);

    public static final RegistryObject<Feature<BlockClusterFeatureConfig>> ROOTS = FEATURES.register("roots", () -> new RootsFeature(BlockClusterFeatureConfig.field_236587_a_));

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());

        // Ginger Roots
        event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ROOTS.get()
                .withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockRegistry.GINGER_ROOT.get().getDefaultState()), SimpleBlockPlacer.PLACER)).tries(32).build())
                .withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));

        // Cassava Roots
        event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ROOTS.get()
                .withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockRegistry.CASSAVA_ROOT.get().getDefaultState()), SimpleBlockPlacer.PLACER)).tries(32).build())
                .withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));


        // Tea Plant - Not Warm & Not Cold
        if (biome.getTemperature() > 0.1f && biome.getTemperature() < 1.5f) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    Feature.FLOWER.withConfiguration((new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider())
                            .addWeightedBlockstate(BlockRegistry.TEA_PLANT.get().getDefaultState().with(TeaPlantBlock.AGE, 3), 1), SimpleBlockPlacer.PLACER)).tries(32).build())
                            .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(1));
        }

        // Pepper Vine - Warm & Hot
        if (biome.getTemperature() > 0.9f && biome.getTemperature() < 3.0f) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    Feature.FLOWER.withConfiguration((new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider())
                            .addWeightedBlockstate(BlockRegistry.PEPPER_VINE.get().getDefaultState().with(PepperVineBlock.AGE, 3), 1), SimpleBlockPlacer.PLACER)).tries(32).build())
                            .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(1));
        }

        // Strawberry Plant - Dark Oak
        if (biome.getTemperature() == 0.7f) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    Feature.FLOWER.withConfiguration((new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider())
                            .addWeightedBlockstate(BlockRegistry.STRAWBERRY_PLANT.get().getDefaultState().with(StrawberryPlantBlock.AGE, 3), 1), SimpleBlockPlacer.PLACER)).tries(32).build())
                            .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(1));
        }

        // Mint Plant - Cold
        if (biome.getTemperature() < 0.3f) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    Feature.FLOWER.withConfiguration((new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider())
                            .addWeightedBlockstate(BlockRegistry.MINT_PLANT.get().getDefaultState().with(MintPlantBlock.AGE, 3), 1), SimpleBlockPlacer.PLACER)).tries(32).build())
                            .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(1));
        }
    }

}
