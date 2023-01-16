package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemTwigWand;
import yerova.botanicpledge.common.blocks.block_entities.BlockEntityInit;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;

import java.util.HashMap;

public class RitualCenterBlock extends BaseEntityBlock {

    public static final HashMap<BlockPos, Block> ritualBlocks() {
        HashMap<BlockPos, Block> blockList = new HashMap<BlockPos, Block>();

        blockList.putAll(manaPools());
        blockList.putAll(ritualPedestals());

        blockList.put(new BlockPos(1, 1, 1), ModBlocks.gaiaPylon);
        blockList.put(new BlockPos(-1, 1, 1), ModBlocks.gaiaPylon);
        blockList.put(new BlockPos(1, 1, -1), ModBlocks.gaiaPylon);
        blockList.put(new BlockPos(-1, 1, -1), ModBlocks.gaiaPylon);


        return blockList;
    }

    public static final HashMap<BlockPos, Block> ritualPedestals() {
        HashMap<BlockPos, Block> blockList = new HashMap<BlockPos, Block>();

        blockList.put(new BlockPos(3, 0, 3), BlockInit.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(-3, 0, 3), BlockInit.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(3, 0, -3), BlockInit.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(-3, 0, -3), BlockInit.RITUAL_PEDESTAL.get());

        blockList.put(new BlockPos(0, 0, 4), BlockInit.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(0, 0, -4), BlockInit.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(4, 0, 0), BlockInit.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(-4, 0, 0), BlockInit.RITUAL_PEDESTAL.get());

        return blockList;
    }

    public static final HashMap<BlockPos, Block> manaPools() {
        HashMap<BlockPos, Block> blockList = new HashMap<BlockPos, Block>();

        blockList.put(new BlockPos(1, 0, 1), ModBlocks.manaPool);
        blockList.put(new BlockPos(-1, 0, 1), ModBlocks.manaPool);
        blockList.put(new BlockPos(1, 0, -1), ModBlocks.manaPool);
        blockList.put(new BlockPos(-1, 0, -1), ModBlocks.manaPool);

        return blockList;
    }

    public RitualCenterBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RitualCenterBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult blockHitResult) {
        if (handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof RitualCenterBlockEntity tile) {


            if (completedStructure(player, pos, world, handIn)) {
                tile.attemptCraft(tile.getHeldStack(), player);
            }

            if (tile.getHeldStack() != null && player.getItemInHand(handIn).isEmpty()) {
                if (world.getBlockState(pos.above()).getMaterial() != Material.AIR)
                    return InteractionResult.SUCCESS;
                ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                world.addFreshEntity(item);
                tile.setHeldStack(ItemStack.EMPTY);
            } else if (!player.getInventory().getSelected().isEmpty()
                    && !(player.getItemInHand(handIn).getItem() instanceof ItemTwigWand)) {
                if (tile.getHeldStack() != null) {
                    ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                    world.addFreshEntity(item);
                }
                tile.setHeldStack(player.getInventory().removeItem(player.getInventory().selected, 1));

            }
            world.sendBlockUpdated(pos, state, state, 1);


        }
        return InteractionResult.SUCCESS;

    }


    public static boolean completedStructure(Player player, BlockPos blockPos, Level level, InteractionHand interactionHand) {
        boolean allChecked = true;
        if (player.getItemInHand(interactionHand).getItem() instanceof ItemTwigWand) {
            for (BlockPos s : ritualBlocks().keySet()) {
                BlockPos checkPos = blockPos.offset(s);
                if (!level.getBlockState(checkPos).getBlock().equals(ritualBlocks().get(s))) {
                    allChecked = false;
                    break;
                }
            }
            if (!allChecked) {
                player.sendMessage(new TextComponent("Structure not Complete"), player.getUUID());
            }

        }
        return allChecked;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.RITUAL_CENTER_BLOCK_ENTITY.get(),
                RitualCenterBlockEntity::tick);
    }


}
