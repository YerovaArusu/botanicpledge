package yerova.botanicpledge.common.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import yerova.botanicpledge.common.worldgen.tree.placer.YggdrasilTrunkPlacer;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BPFeatures;
import yerova.botanicpledge.setup.BotanicPledge;

public class ConfiguredBPFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> YGGDRASIL_WOOD_KEY = registerKey("yggdrasil_wood");

    public static final ResourceKey<ConfiguredFeature<?, ?>> AURA_NODE_FEATURE = registerKey("aura_node");


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        register(context, YGGDRASIL_WOOD_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BPBlocks.YGGDRASIL_LOG.get()),
                new YggdrasilTrunkPlacer(1, 4, 2),
                BlockStateProvider.simple(BPBlocks.YGGDRASIL_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 3),
                new TwoLayersFeatureSize(2, 0, 2)).build());

        register(context, AURA_NODE_FEATURE, BPFeatures.FLOATING_NODE.get(),
                new SimpleBlockConfiguration(BlockStateProvider.simple(BPBlocks.AURA_NODE.get()))
        );


    }



    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(BotanicPledge.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
