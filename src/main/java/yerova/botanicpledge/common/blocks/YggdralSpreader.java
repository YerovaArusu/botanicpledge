package yerova.botanicpledge.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.common.item.lens.LensItem;
import yerova.botanicpledge.common.blocks.block_entities.YggdralSpreaderBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class YggdralSpreader extends BotaniaWaterloggedBlock implements EntityBlock {
    private static final VoxelShape SHAPE = box(2, 2, 2, 14, 14, 14);
    private static final VoxelShape SHAPE_PADDING = box(1, 1, 1, 15, 15, 15);
    private static final VoxelShape SHAPE_SCAFFOLDING = box(0, 0, 0, 16, 16, 16);

    public enum Variant {
        YGGDRAL(10_000, 60_000, 0x20FF20, 0x00FF00, 120, 20f, 2f);

        public final int burstMana;
        public final int manaCapacity;
        public final int color;
        public final int hudColor;
        public final int preLossTicks;
        public final float lossPerTick;
        public final float motionModifier;

        Variant(int bm, int mc, int c, int hc, int plt, float lpt, float mm) {
            burstMana = bm;
            manaCapacity = mc;
            color = c;
            hudColor = hc;
            preLossTicks = plt;
            lossPerTick = lpt;
            motionModifier = mm;
        }
    }

    public final YggdralSpreader.Variant variant;

    public YggdralSpreader(YggdralSpreader.Variant v, BlockBehaviour.Properties builder) {
        super(builder);
        registerDefaultState(defaultBlockState().setValue(BotaniaStateProperties.HAS_SCAFFOLDING, false));
        this.variant = v;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BotaniaStateProperties.HAS_SCAFFOLDING);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(BotaniaStateProperties.HAS_SCAFFOLDING)) {
            return SHAPE_SCAFFOLDING;
        }
        BlockEntity be = blockGetter.getBlockEntity(blockPos);
        return be instanceof YggdralSpreaderBlockEntity spreader && spreader.paddingColor != null ? SHAPE_PADDING : SHAPE;
    }

    @Nonnull
    @Override
    public VoxelShape getOcclusionShape(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        return SHAPE;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction orientation = placer == null ? Direction.WEST : Direction.orderedByNearest(placer)[0].getOpposite();
        YggdralSpreaderBlockEntity spreader = (YggdralSpreaderBlockEntity) world.getBlockEntity(pos);

        switch (orientation) {
            case DOWN:
                spreader.rotationY = -90F;
                break;
            case UP:
                spreader.rotationY = 90F;
                break;
            case NORTH:
                spreader.rotationX = 270F;
                break;
            case SOUTH:
                spreader.rotationX = 90F;
                break;
            case WEST:
                break;
            case EAST:
                spreader.rotationX = 180F;
                break;
        }
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (!(tile instanceof YggdralSpreaderBlockEntity spreader)) {
            return InteractionResult.PASS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() instanceof WandOfTheForestItem) {
            return InteractionResult.PASS;
        }
        boolean mainHandEmpty = player.getMainHandItem().isEmpty();

        ItemStack lens = spreader.getItemHandler().getItem(0);
        boolean playerHasLens = heldItem.getItem() instanceof LensItem;
        boolean lensIsSame = playerHasLens && ItemStack.isSameItemSameTags(heldItem, lens);
        ItemStack wool = spreader.paddingColor != null
                ? new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor))
                : ItemStack.EMPTY;
        boolean playerHasWool = ColorHelper.isWool(Block.byItem(heldItem.getItem()));
        boolean woolIsSame = playerHasWool && ItemStack.isSameItemSameTags(heldItem, wool);
        boolean playerHasScaffolding = !heldItem.isEmpty() && heldItem.is(Items.SCAFFOLDING);
        boolean shouldInsert = (playerHasLens && !lensIsSame)
                || (playerHasWool && !woolIsSame)
                || (playerHasScaffolding && !state.getValue(BotaniaStateProperties.HAS_SCAFFOLDING));

        if (shouldInsert) {
            if (playerHasLens) {
                ItemStack toInsert = heldItem.copy();
                toInsert.setCount(1);

                heldItem.shrink(1);
                if (!lens.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(lens);
                }

                spreader.getItemHandler().setItem(0, toInsert);
                world.playSound(player, pos, BotaniaSounds.spreaderAddLens, SoundSource.BLOCKS, 1F, 1F);
            } else if (playerHasWool) {
                Block woolBlock = Block.byItem(heldItem.getItem());

                heldItem.shrink(1);
                if (spreader.paddingColor != null) {
                    ItemStack spreaderWool = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
                    player.getInventory().placeItemBackInInventory(spreaderWool);
                }

                spreader.paddingColor = ColorHelper.getWoolColor(woolBlock);
                spreader.setChanged();
                world.playSound(player, pos, BotaniaSounds.spreaderCover, SoundSource.BLOCKS, 1F, 1F);
            } else { // playerHasScaffolding
                world.setBlockAndUpdate(pos, state.setValue(BotaniaStateProperties.HAS_SCAFFOLDING, true));
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                }

                world.playSound(player, pos, BotaniaSounds.spreaderScaffold, SoundSource.BLOCKS, 1F, 1F);
            }
            return InteractionResult.SUCCESS;
        }

        if (state.getValue(BotaniaStateProperties.HAS_SCAFFOLDING) && player.isSecondaryUseActive()) {
            if (!player.getAbilities().instabuild) {
                ItemStack scaffolding = new ItemStack(Items.SCAFFOLDING);
                player.getInventory().placeItemBackInInventory(scaffolding);
            }
            world.setBlockAndUpdate(pos, state.setValue(BotaniaStateProperties.HAS_SCAFFOLDING, false));
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

            world.playSound(player, pos, BotaniaSounds.spreaderUnScaffold, SoundSource.BLOCKS, 1F, 1F);

            return InteractionResult.SUCCESS;
        }
        if (!lens.isEmpty() && (mainHandEmpty || lensIsSame)) {
            player.getInventory().placeItemBackInInventory(lens);
            spreader.getItemHandler().setItem(0, ItemStack.EMPTY);

            world.playSound(player, pos, BotaniaSounds.spreaderRemoveLens, SoundSource.BLOCKS, 1F, 1F);

            return InteractionResult.SUCCESS;
        }
        if (spreader.paddingColor != null && (mainHandEmpty || woolIsSame)) {
            player.getInventory().placeItemBackInInventory(wool);
            spreader.paddingColor = null;
            spreader.setChanged();

            world.playSound(player, pos, BotaniaSounds.spreaderUncover, SoundSource.BLOCKS, 1F, 1F);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }



    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (!(tile instanceof YggdralSpreaderBlockEntity spreader)) {
                return;
            }

            if (spreader.paddingColor != null) {
                ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), padding);
            }

            if (state.getValue(BotaniaStateProperties.HAS_SCAFFOLDING)) {
                ItemStack scaffolding = new ItemStack(Items.SCAFFOLDING);
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), scaffolding);
            }

            Containers.dropContents(world, pos, spreader.getItemHandler());

            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Nonnull
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new YggdralSpreaderBlockEntity(pos, state);
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BPBlockEntities.YGGDRAL_SPREADER.get(), YggdralSpreaderBlockEntity::commonTick);
    }

}
