package yerova.botanicpledge.common.worldgen.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.DarkOakTreeGrower;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.worldgen.ConfiguredBPFeatures;

public class YggdrasilTreeGrower extends DarkOakTreeGrower {
    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource p_255891_) {
        return ConfiguredBPFeatures.YGGDRASIL_WOOD_KEY;
    }
}
