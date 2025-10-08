package yerova.botanicpledge.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.blocks.block_entities.essence.AuraNodeBlockEntity;
import yerova.botanicpledge.setup.BPEssences;

public class FloatingNodeFeature extends Feature<SimpleBlockConfiguration> {

    public FloatingNodeFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {


        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, origin.getX(), origin.getZ());

        BlockPos placePos = new BlockPos(origin.getX(), surfaceY + 2, origin.getZ());

        BlockState state = context.config().toPlace().getState(context.random(), placePos);


        if (level.isEmptyBlock(placePos)) {
            level.setBlock(placePos, state, 3);

            if (level.getBlockEntity(placePos) instanceof AuraNodeBlockEntity be) {

                be.auraData.setBaseEssence(Essence.getRandomEssence(level.getRandom(), BPEssences.EMPTY_ESSENCE.get()), 10);

                be.auraData.setNodeRank(level.getRandom().nextInt(1,8));
                be.auraData.setType(AuraNodeType.values()[level.getRandom().nextInt(AuraNodeType.values().length-1)]);

                for (int j = 0; j <= level.getRandom().nextInt(0,be.auraData.getNodeRank()); j++) {
                    be.auraData.addEssence(Essence.getRandomEssence(level.getRandom(), BPEssences.EMPTY_ESSENCE.get()), 10);
                }


                level.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
                be.setChanged();

            }

            return true;
        }

        return false;
    }
}

