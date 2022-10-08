package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;

public class RitualPedestalBlock extends BaseEntityBlock {

    protected RitualPedestalBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new RitualPedestalBlockEntity(blockPos, state);
    }




    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if(handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;
        if(!world.isClientSide && world.getBlockEntity(pos) instanceof RitualPedestalBlockEntity tile) {
            if (tile.getStack() != null && player.getItemInHand(handIn).isEmpty()) {
                if(world.getBlockState(pos.above()).getMaterial() != Material.AIR)
                    return InteractionResult.SUCCESS;
                ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getStack());
                world.addFreshEntity(item);
                tile.setStack(ItemStack.EMPTY);
            } else if (!player.getInventory().getSelected().isEmpty()) {
                if(tile.getStack() != null){
                    ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getStack());
                    world.addFreshEntity(item);
                }

                tile.setStack(player.getInventory().removeItem(player.getInventory().selected, 1));

            }
            world.sendBlockUpdated(pos, state, state, 2);
        }
        return  InteractionResult.SUCCESS;
    }

}
