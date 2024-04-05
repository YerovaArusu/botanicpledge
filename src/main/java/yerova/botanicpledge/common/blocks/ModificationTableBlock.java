package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.item.WandOfTheForestItem;
import yerova.botanicpledge.common.blocks.block_entities.ModificationTableBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.AttributeProvider;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.RuneGemItem;
import yerova.botanicpledge.setup.BPBlockEntities;


public class ModificationTableBlock extends BaseEntityBlock {
    public static final BooleanProperty ALTER = BooleanProperty.create("alter");

    public ModificationTableBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ALTER, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModificationTableBlockEntity(pPos, pState);
    }

    /*
    Copied and adapted from:
    https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/common/block/ArcanePedestal.java
 */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        if (handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof ModificationTableBlockEntity tile) {

            if (tile.getHeldStack() != null && player.getItemInHand(handIn).isEmpty() && !state.getValue(ModificationTableBlock.ALTER)) {
                ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                world.addFreshEntity(item);
                tile.setHeldStack(ItemStack.EMPTY);

            } else if (!player.getInventory().getSelected().isEmpty()
                    && !(player.getItemInHand(handIn).getItem() instanceof WandOfTheForestItem)) {
                if (!state.getValue(ModificationTableBlock.ALTER)) {
                    if (tile.getHeldStack() != null) {
                        ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), tile.getHeldStack());
                        world.addFreshEntity(item);
                    }
                    tile.setHeldStack(player.getInventory().removeItem(player.getInventory().selected, 1));
                } else {
                    if (tile.getHeldStack().getCapability(AttributeProvider.ATTRIBUTE).isPresent()) {
                        Attribute attribute = tile.getHeldStack().getCapability(AttributeProvider.ATTRIBUTE).resolve().get();
                        if (handleAttribute(world, handIn, player, attribute)) return InteractionResult.FAIL;
                    }

                    if (tile.getHeldStack().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).isPresent()) {
                        Attribute attribute = tile.getHeldStack().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve().get();
                        if (handleAttribute(world, handIn, player, attribute)) return InteractionResult.FAIL;
                    }
                }
            }
            world.sendBlockUpdated(pos, state, state, 2);
        }
        return InteractionResult.SUCCESS;
    }

    private boolean handleAttribute(Level world, InteractionHand handIn, Player player, Attribute attribute) {
        if (player.getItemInHand(handIn).getItem() instanceof RuneGemItem) {
            if (!attribute.addRune(Attribute.Rune.getRuneFromStack(player.getItemInHand(handIn)))) return true;

        } else if (player.isCrouching() || player.getItemInHand(handIn).isEmpty()) {
            if (attribute.getAllRunes().isEmpty()) return true;

            if (!player.addItem(attribute.getAllRunes().get(0).getAsStack())) {
                ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), attribute.getAllRunes().get(0).getAsStack());
                world.addFreshEntity(item);
            }
            attribute.removeRuneAt(0);
        }
        return false;
    }


    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ALTER);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }


    @Nullable
    @Override
    public <T extends
            BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BPBlockEntities.MODIFICATION_TABLE.get(),
                ModificationTableBlockEntity::tick);
    }


}
