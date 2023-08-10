package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import yerova.botanicpledge.setup.BlockEntityInit;


public class RitualPedestalBlockEntity extends RitualBaseBlockEntity {

    public RitualPedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.RITUAL_PEDESTAL_BLOCK_ENTITY.get(), pos, blockState);
    }
}
