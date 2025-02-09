package yerova.botanicpledge.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.List;

public class BPPlacedFeatures {

    public static final ResourceKey<PlacedFeature> YGGDRASIL_TREE = registerKey("yggdrasil_tree_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, YGGDRASIL_TREE, configuredFeatures.getOrThrow(ConfiguredBPFeatures.YGGDRASIL_WOOD_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.00001f, 2),
                        BPBlocks.YGGDRASIL_SAPLING.get()));
    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(BotanicPledge.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
