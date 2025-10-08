package yerova.botanicpledge.common.blocks;

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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;

public class RitualPedestalBlock extends BaseEntityBlock {


    public RitualPedestalBlock(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 16, 13);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new RitualPedestalBlockEntity(blockPos, state);
    }

    /*
        Copied and adapted from:
        https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/common/block/ArcanePedestal.java
     */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof RitualPedestalBlockEntity tile) {
            if (tile.getHeldStack() != null && player.getItemInHand(handIn).isEmpty()) {
                ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                world.addFreshEntity(item);
                tile.setHeldStack(ItemStack.EMPTY);
            } else if (!player.getInventory().getSelected().isEmpty()) {
                if (tile.getHeldStack() != null) {
                    ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                    world.addFreshEntity(item);
                }
                tile.setHeldStack(player.getInventory().removeItem(player.getInventory().selected, 1));
            }
            world.sendBlockUpdated(pos, state, state, 2);
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        if (pLevel.getBlockEntity(pPos) instanceof RitualPedestalBlockEntity e && e.getHeldStack() != null ) {
            pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), e.getHeldStack()));
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BPBlockEntities.RITUAL_PEDESTAL.get(),
                RitualPedestalBlockEntity::tick);
    }
}
