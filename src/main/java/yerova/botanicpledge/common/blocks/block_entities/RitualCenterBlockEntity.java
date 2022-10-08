package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
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
import yerova.botanicpledge.common.blocks.RitualCenterBlock;
import yerova.botanicpledge.common.recipes.ritual.IBotanicRitualRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RitualCenterBlockEntity extends RitualBaseBlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);


    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    protected final ContainerData data;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int progress = 0;
    private int maxProgress = 128;

    private int hasReachedMaxStat = 0;
    private int maxedStat = -1;
    private CompoundTag savedItemNBT;

    public ItemStack catalystItem = ItemStack.EMPTY;
    boolean isCrafting = false;

    public RitualCenterBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityInit.RITUAL_CENTER_BLOCK_ENTITY.get(), p_155229_, p_155230_);

        this.data = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return RitualCenterBlockEntity.this.progress;
                    case 1:
                        return RitualCenterBlockEntity.this.maxProgress;
                    case 2:
                        return RitualCenterBlockEntity.this.hasReachedMaxStat;
                    case 3:
                        return RitualCenterBlockEntity.this.maxedStat;
                    default:
                        return 0;
                }
            }


            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        RitualCenterBlockEntity.this.progress = value;
                        break;
                    case 1:
                        RitualCenterBlockEntity.this.maxProgress = value;
                        break;
                    case 2:
                        RitualCenterBlockEntity.this.hasReachedMaxStat = value;
                        break;
                    case 3:
                        RitualCenterBlockEntity.this.maxedStat = value;
                        break;
                }
            }


            public int getCount() {
                return CoreAltarBlockEntity.dataContainerSize = 4;
            }
        };


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


    public void clearItems(BlockEntity entity) {
        for (BlockPos blockPos : RitualCenterBlock.ritualPedestals().keySet()) {
            if (level.getBlockEntity(entity.getBlockPos().offset(blockPos)) instanceof RitualPedestalBlockEntity tile && tile.getStack() != null) {
                tile.setStack(tile.getStack().getContainerItem());
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
        if (isCrafting)
            return false;
        if (!craftingPossible(catalyst, playerEntity)) {
            return false;
        }
        IBotanicRitualRecipe recipe = this.getRecipe(catalyst, playerEntity);
        if (recipe.consumesMana()) ManaUtils.takeManaNearby(worldPosition, level, 10, recipe.getManaCost());
        this.isCrafting = true;
        updateBlock();
        return true;
    }

    public boolean craftingPossible(ItemStack stack, Player playerEntity) {
        if (isCrafting || stack.isEmpty())
            return false;
        IBotanicRitualRecipe recipe = this.getRecipe(stack, playerEntity);

        return recipe != null && (!recipe.consumesMana() || (recipe.consumesMana() && ManaUtils.hasManaNearby(worldPosition, level, 10, recipe.getManaCost())));
    }


    @Override
    public void load(CompoundTag compound) {
        catalystItem = ItemStack.of((CompoundTag) compound.get("itemStack"));
        isCrafting = compound.getBoolean("is_crafting");
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (catalystItem != null) {
            CompoundTag reagentTag = new CompoundTag();
            catalystItem.save(reagentTag);
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
        return catalystItem.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        if (isCrafting)
            return ItemStack.EMPTY;
        return catalystItem;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (isCrafting || stack.isEmpty())
            return false;
        return catalystItem.isEmpty() && craftingPossible(stack, null);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (isCrafting)
            return ItemStack.EMPTY;
        ItemStack stack = catalystItem.copy().split(count);
        catalystItem.shrink(count);
        updateBlock();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (isCrafting)
            return ItemStack.EMPTY;
        return catalystItem;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (isCrafting)
            return;
        this.catalystItem = stack;
        updateBlock();
        attemptCraft(this.catalystItem, null);
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
        this.catalystItem = ItemStack.EMPTY;
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

    private <E extends BlockEntity & IAnimatable> PlayState idlePredicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("floating", true));

        return PlayState.CONTINUE;
    }

    private <E extends BlockEntity & IAnimatable> PlayState craftPredicate(AnimationEvent<E> event) {
        if (!this.isCrafting)
            return PlayState.STOP;
        return PlayState.CONTINUE;
    }

    public List<ItemStack> getPedestalItems() {
        ArrayList<ItemStack> pedestalItems = new ArrayList<>();
        for (BlockPos blockPos : RitualCenterBlock.ritualPedestals().keySet()) {
            if (level.getBlockEntity(worldPosition.offset(blockPos)) instanceof RitualPedestalBlockEntity tile && tile.getStack() != null && !tile.getStack().isEmpty()) {
                pedestalItems.add(tile.getStack());
            }
        }
        return pedestalItems;
    }

}
