package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.blocks.block_entities.YggdrasilLogBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPBlocks;

public class YggdrasilLog extends BaseEntityBlock {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public YggdrasilLog(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }


    public BlockState rotate(BlockState pState, Rotation pRot) {
        return rotatePillar(pState, pRot);
    }

    public static BlockState rotatePillar(BlockState pState, Rotation pRotation) {
        switch (pRotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)pState.getValue(AXIS)) {
                    case X:
                        return pState.setValue(AXIS, Direction.Axis.Z);
                    case Z:
                        return pState.setValue(AXIS, Direction.Axis.X);
                    default:
                        return pState;
                }
            default:
                return pState;
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand) == ItemStack.EMPTY) return InteractionResult.PASS;
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

        ItemStack stack = pPlayer.getItemInHand(pHand);

        if (Essence.isEssence(stack) && blockEntity instanceof YggdrasilLogBlockEntity entity) {

            Essence essence = Essence.getEssence(stack);

            entity.auraData.setBaseEssence(essence,1);
            entity.auraData.setEssenceAmount(essence,10);
            entity.auraData.setType(AuraNodeType.getRandomType());

        }

        blockEntity.setChanged();

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(AXIS, pContext.getClickedFace().getAxis());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        Block block = pState.getBlock();

        if (block == BPBlocks.YGGDRASIL_LOG.get()) {
            return new YggdrasilLogBlockEntity(BPBlockEntities.YGGDRASIL_LOG.get(), pPos, pState);
        } else if (block == BPBlocks.STRIPPED_YGGDRASIL_LOG.get()) {
            return new YggdrasilLogBlockEntity(BPBlockEntities.STRIPPED_YGGDRASIL_LOG.get(), pPos, pState);
        } else if (block == BPBlocks.YGGDRASIL_WOOD.get()) {
            return new YggdrasilLogBlockEntity(BPBlockEntities.YGGDRASIL_WOOD.get(), pPos, pState);
        } else if (block == BPBlocks.STRIPPED_YGGDRASIL_WOOD.get()) {
            return new YggdrasilLogBlockEntity(BPBlockEntities.STRIPPED_YGGDRASIL_WOOD.get(), pPos, pState);
        }

        return null;
    }


    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (context.getItemInHand().getItem() instanceof AxeItem)
            if (state.is(BPBlocks.YGGDRASIL_LOG.get())) {
                return BPBlocks.STRIPPED_YGGDRASIL_LOG.get().defaultBlockState();
            }

        if (state.is(BPBlocks.YGGDRASIL_WOOD.get())) {
            return BPBlocks.STRIPPED_YGGDRASIL_WOOD.get().defaultBlockState();
        }

        return state;
    }
}
