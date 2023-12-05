package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.WandOfTheForestItem;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.HashMap;

public class RitualCenterBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 16, 13);
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public static final HashMap<BlockPos, Block> ritualBlocks() {
        HashMap<BlockPos, Block> blockList = new HashMap<BlockPos, Block>();

        blockList.putAll(manaPools());
        blockList.putAll(ritualPedestals());

        blockList.put(new BlockPos(1, 1, 1), BotaniaBlocks.gaiaPylon);
        blockList.put(new BlockPos(-1, 1, 1), BotaniaBlocks.gaiaPylon);
        blockList.put(new BlockPos(1, 1, -1), BotaniaBlocks.gaiaPylon);
        blockList.put(new BlockPos(-1, 1, -1), BotaniaBlocks.gaiaPylon);


        return blockList;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public static final HashMap<BlockPos, Block> ritualPedestals() {
        HashMap<BlockPos, Block> blockList = new HashMap<BlockPos, Block>();

        blockList.put(new BlockPos(3, 0, 3), BPBlocks.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(-3, 0, 3), BPBlocks.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(3, 0, -3), BPBlocks.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(-3, 0, -3), BPBlocks.RITUAL_PEDESTAL.get());

        blockList.put(new BlockPos(0, 0, 4), BPBlocks.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(0, 0, -4), BPBlocks.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(4, 0, 0), BPBlocks.RITUAL_PEDESTAL.get());
        blockList.put(new BlockPos(-4, 0, 0), BPBlocks.RITUAL_PEDESTAL.get());

        return blockList;
    }

    public static final HashMap<BlockPos, Block> manaPools() {
        HashMap<BlockPos, Block> blockList = new HashMap<BlockPos, Block>();

        blockList.put(new BlockPos(1, 0, 1), BotaniaBlocks.manaPool);
        blockList.put(new BlockPos(-1, 0, 1), BotaniaBlocks.manaPool);
        blockList.put(new BlockPos(1, 0, -1), BotaniaBlocks.manaPool);
        blockList.put(new BlockPos(-1, 0, -1), BotaniaBlocks.manaPool);

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
        if (!world.isClientSide) {
            if (world.getBlockEntity(pos) instanceof RitualCenterBlockEntity tile) {

                if (completedStructure(player, pos, world, handIn)) {
                    if (tile.attemptCraft(tile.getHeldStack(), player)) return InteractionResult.SUCCESS;
                }

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


    public static boolean completedStructure(Player player, BlockPos blockPos, Level level, InteractionHand interactionHand) {
        boolean allChecked = true;
        if (player.getItemInHand(interactionHand).getItem() instanceof WandOfTheForestItem) {
            for (BlockPos s : ritualBlocks().keySet()) {
                BlockPos checkPos = blockPos.offset(s);
                if (!level.getBlockState(checkPos).getBlock().equals(ritualBlocks().get(s))) {
                    allChecked = false;
                    break;
                }
            }
            if (!allChecked) {
                player.sendSystemMessage(Component.literal("Structure not Complete"));
            }

        }
        return allChecked;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BPBlockEntities.RITUAL_CENTER_BLOCK_ENTITY.get(),
                RitualCenterBlockEntity::tick);
    }


}
