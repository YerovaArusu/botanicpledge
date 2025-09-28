package yerova.botanicpledge.common.blocks.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.essence.EssenceCondenserBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.essence.EssenceJarBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;

public class EssenceCondenser extends BaseEntityBlock {

    public EssenceCondenser(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 14, 14);

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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BPBlockEntities.ESSENCE_CONDENSER.get(),
                EssenceCondenserBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof EssenceCondenserBlockEntity tile) {

            if (tile.getHeldStack() != null) {
                if (player.getItemInHand(handIn).isEmpty()) {
                    player.setItemInHand(handIn, tile.getHeldStack());
                    tile.setHeldStack(ItemStack.EMPTY);
                    world.sendBlockUpdated(pos, state, state, 2);
                    return InteractionResult.SUCCESS;
                } else  {
                    ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                    world.addFreshEntity(item);
                    world.sendBlockUpdated(pos, state, state, 2);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

}
