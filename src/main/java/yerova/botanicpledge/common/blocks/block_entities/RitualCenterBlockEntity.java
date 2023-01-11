package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import yerova.botanicpledge.api.BotanicPledgeAPI;
import yerova.botanicpledge.api.utils.ManaUtils;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.ParticleUtils;
import yerova.botanicpledge.client.particle.custom.YggdralParticleData;
import yerova.botanicpledge.common.blocks.RitualCenterBlock;
import yerova.botanicpledge.common.config.BotanicPledgeCommonConfigs;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.recipes.ritual.IBotanicRitualRecipe;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RitualCenterBlockEntity extends RitualBaseBlockEntity implements IAnimatable {


    private final AnimationFactory factory = new AnimationFactory(this);


    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    private int counter = 0;

    boolean isCrafting = false;

    public RitualCenterBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityInit.RITUAL_CENTER_BLOCK_ENTITY.get(), p_155229_, p_155230_);


    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ritual_core.idle", true));
        return PlayState.CONTINUE;
    }


    public static void tick(Level level, BlockPos pos, BlockState state, RitualCenterBlockEntity entity) {


        if (level.isClientSide) {
            if (entity.isCrafting) {

                for (BlockPos bPos : RitualCenterBlock.ritualPedestals().keySet()) {
                    BlockPos p = pos.offset(bPos);
                    if (level.getBlockEntity(p) instanceof RitualPedestalBlockEntity pedestalTile && pedestalTile.getHeldStack() != null && !pedestalTile.getHeldStack().isEmpty())
                        level.addParticle(
                                YggdralParticleData.createData(new ParticleColor(19, 237, 237)),
                                p.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2), p.getY() + 1.5 + ParticleUtils.inRange(-0.3, 0.3), p.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                                0, 0, 0);
                }
                if (!entity.heldStack.isEmpty()) {
                    level.addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                            pos.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                            pos.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                            pos.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                            0, 0, 0);
                }
            }

            return;
        }


        int craftingLength = 210;
        if (entity.isCrafting) {
            if (entity.getRecipe(entity.heldStack, null) == null)
                entity.isCrafting = false;
            entity.counter += 1;
        }

        if (entity.counter > craftingLength) {
            entity.counter = 1;

            if (entity.isCrafting) {
                IBotanicRitualRecipe recipe = entity.getRecipe(entity.heldStack, null);
                List<ItemStack> pedestalItems = entity.getPedestalItems();
                if (recipe != null) {
                    pedestalItems.forEach(i -> i = null);
                    entity.heldStack = recipe.getResult(pedestalItems, entity.heldStack, entity);

                    //NBT handling
                    if (entity.heldStack.getItem() instanceof DivineCoreItem) {
                        if (recipe.getAdditionalNBT() != null) {
                            CompoundTag combinedTag = entity.heldStack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
                            for (String s : recipe.getAdditionalNBT().getAllKeys()) {
                                if (combinedTag.contains(s)) {
                                    combinedTag.putDouble(s, combinedTag.getDouble(s) + recipe.getAdditionalNBT().getDouble(s));
                                } else {
                                    combinedTag.putDouble(s, recipe.getAdditionalNBT().getDouble(s));
                                }
                                entity.heldStack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).merge(combinedTag);
                            }
                        }
                    }
                    entity.clearItems(entity);

                }

                entity.isCrafting = false;
            }
            entity.updateBlock();
        }
    }

    public void clearItems(BlockEntity entity) {
        for (BlockPos blockPos : RitualCenterBlock.ritualPedestals().keySet()) {
            if (level.getBlockEntity(entity.getBlockPos().offset(blockPos)) instanceof RitualPedestalBlockEntity tile && tile.getHeldStack() != null) {
                tile.setHeldStack(tile.getHeldStack().getContainerItem());
                BlockState state = level.getBlockState(blockPos);
                level.sendBlockUpdated(blockPos, state, state, 3);
            }
        }
    }


    public IBotanicRitualRecipe getRecipe(ItemStack stack, @Nullable Player playerEntity) {
        List<ItemStack> pedestalItems = getPedestalItems();
        return BotanicPledgeAPI.getInstance().getBotanicRitualRecipes(level).stream().filter(r -> r.isMatch(pedestalItems, stack, this, playerEntity)).findFirst().orElse(null);
    }

    public boolean attemptCraft(ItemStack catalyst, @Nullable Player playerEntity) {
        IBotanicRitualRecipe recipe = this.getRecipe(catalyst, playerEntity);


        if (catalyst.hasTag() && catalyst.getTag().contains(BPConstants.STATS_TAG_NAME) && recipe != null && recipe.getAdditionalNBT() != null) {
            CompoundTag tag = catalyst.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);


            for (String str : recipe.getAdditionalNBT().getAllKeys()) {
                if (tag.getAllKeys().contains(str)) {

                    BotanicPledge.LOGGER.info("Something went right");

                    if (!(tag.getDouble(str) < BPConstants.ATTRIBUTED_STATS().get(str))) {
                        return false;
                    }

                    if (str.equals("may_fly")) {
                        return false;

                    }

                    if (str.equals("jump_height")) {
                        return false;
                    }
                }
            }
        }


        BotanicPledge.LOGGER.info(BotanicPledgeCommonConfigs.ARMOR_MAX_VALUE.get() + "");

        if (isCrafting)
            return false;
        if (!craftingPossible(catalyst, playerEntity)) {
            return false;
        }

        if (recipe.consumesMana()) ManaUtils.takeManaNearby(worldPosition, level, 2, recipe.getManaCost());
        this.isCrafting = true;
        updateBlock();
        return true;
    }

    public boolean craftingPossible(ItemStack stack, Player playerEntity) {
        if (stack.isEmpty()) return false;
        IBotanicRitualRecipe recipe = this.getRecipe(stack, playerEntity);
        return recipe != null && (!recipe.consumesMana() || (recipe.consumesMana() && ManaUtils.hasManaNearby(worldPosition, level, 10, recipe.getManaCost())));
    }


    @Override
    public void load(CompoundTag compound) {
        heldStack = ItemStack.of((CompoundTag) compound.get("itemStack"));
        isCrafting = compound.getBoolean("is_crafting");
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (heldStack != null) {
            CompoundTag reagentTag = new CompoundTag();
            heldStack.save(reagentTag);
            tag.put("itemStack", reagentTag);
        }
        tag.putBoolean("is_crafting", isCrafting);


    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("is_crafting", this.isCrafting);
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return heldStack.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        if (isCrafting) return ItemStack.EMPTY;
        return heldStack;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (isCrafting || stack.isEmpty())
            return false;
        return heldStack.isEmpty() && craftingPossible(stack, null);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (isCrafting)
            return ItemStack.EMPTY;
        ItemStack stack = heldStack.copy().split(2);
        heldStack.shrink(count);
        updateBlock();
        return stack;

    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (isCrafting)
            return ItemStack.EMPTY;
        return heldStack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (isCrafting)
            return;
        this.heldStack = stack;
        updateBlock();

    }

    @Override
    public void setHeldStack(ItemStack heldStack) {
        if (isCrafting)
            return;
        this.heldStack = heldStack;
        updateBlock();
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        this.heldStack = ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        itemHandler.invalidate();
        super.invalidateCaps();
    }

    public List<ItemStack> getPedestalItems() {
        ArrayList<ItemStack> pedestalItems = new ArrayList<>();
        for (BlockPos blockPos : RitualCenterBlock.ritualPedestals().keySet()) {
            BlockPos tmpPos = worldPosition;
            if (level.getBlockEntity(tmpPos.offset(blockPos)) instanceof RitualPedestalBlockEntity tile && tile.getHeldStack() != null && !tile.getHeldStack().isEmpty()) {
                pedestalItems.add(tile.getHeldStack());
            }
        }
        return pedestalItems;
    }



}
