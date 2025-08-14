package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.moddingx.libx.base.tile.BlockEntityBase;
import yerova.botanicpledge.common.blocks.block_entities.EssenceCondenserBlockEntity;

public class EssenceCondenser extends BaseEntityBlock {

    public EssenceCondenser(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 7, 14);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }





    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EssenceCondenserBlockEntity(pPos, pState);
    }
}
