package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import yerova.botanicpledge.setup.BPBlockEntities;


public class RitualPedestalBlockEntity extends RitualBaseBlockEntity {

    public RitualPedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(BPBlockEntities.RITUAL_PEDESTAL.get(), pos, blockState);
    }
}
