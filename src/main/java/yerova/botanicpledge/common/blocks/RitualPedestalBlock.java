package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class RitualPedestalBlock extends BaseEntityBlock {

    public RitualPedestalBlock(Properties properties) {
        super(properties);
    }


    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 16, 13);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new RitualPedestalBlockEntity(blockPos, state);
    }


    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player cPlayer, InteractionHand handIn, BlockHitResult hit) {
        if (!world.isClientSide) {
            ServerPlayer player = (ServerPlayer) cPlayer;

            if (world.getBlockEntity(pos) instanceof RitualPedestalBlockEntity tile) {
                if (tile.getHeldStack() != null || tile.getHeldStack() != ItemStack.EMPTY) {
                    if (!player.addItem(tile.getHeldStack())) {
                        ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                        world.addFreshEntity(item);
                        tile.setHeldStack(ItemStack.EMPTY);
                    } else tile.setHeldStack(ItemStack.EMPTY);

                    if (!player.getItemInHand(handIn).isEmpty()) {
                        tile.setHeldStack(player.getInventory().removeItem(player.getInventory().selected, 1));
                    }
                }
            }

            world.sendBlockUpdated(pos, state, state, 2);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
