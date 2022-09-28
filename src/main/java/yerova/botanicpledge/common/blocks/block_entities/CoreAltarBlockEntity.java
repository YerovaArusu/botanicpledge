package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.client.render.screen.CoreAltarMenu;
import yerova.botanicpledge.common.config.BotanicPledgeCommonConfigs;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.recipes.CoreAltarRecipe;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CoreAltarBlockEntity extends BlockEntity implements MenuProvider {

    public static final int slotCount = 10;
    public static int dataContainerSize = 3;
    public final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected final ContainerData data;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int progress = 0;
    private int maxProgress = 128;

    private int hasReachedMaxStat = 0;
    private int maxedStat = -1;
    private CompoundTag savedItemNBT;

    public CoreAltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.CORE_ALTER_BLOCK_ENTITY.get(), blockPos, blockState);

        this.data = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return CoreAltarBlockEntity.this.progress;
                    case 1:
                        return CoreAltarBlockEntity.this.maxProgress;
                    case 2:
                        return CoreAltarBlockEntity.this.hasReachedMaxStat;
                    case 3:
                        return CoreAltarBlockEntity.this.maxedStat;
                    default:
                        return 0;
                }
            }


            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        CoreAltarBlockEntity.this.progress = value;
                        break;
                    case 1:
                        CoreAltarBlockEntity.this.maxProgress = value;
                        break;
                    case 2:
                        CoreAltarBlockEntity.this.hasReachedMaxStat = value;
                    case 3:
                        CoreAltarBlockEntity.this.maxedStat = value;
                }
            }


            public int getCount() {
                return CoreAltarBlockEntity.dataContainerSize = 4;
            }
        };

    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, CoreAltarBlockEntity pBlockEntity) {
        if (hasRecipe(pBlockEntity)) {
            pBlockEntity.progress++;
            setChanged(pLevel, pPos, pState);
            if (pBlockEntity.progress > pBlockEntity.maxProgress) {
                craftItem(pBlockEntity);

            }
        } else {
            pBlockEntity.resetNBT();
            pBlockEntity.resetProgress();
            setChanged(pLevel, pPos, pState);
        }
    }

    private static boolean hasRecipe(CoreAltarBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<CoreAltarRecipe> match = level.getRecipeManager()
                .getRecipeFor(CoreAltarRecipe.Type.INSTANCE, inventory, level);


        return match.isPresent()
                && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getResultItem())
                && entity.transportedNBT(match)
                && validAttributeChange(entity);
    }

    private static void craftItem(CoreAltarBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<CoreAltarRecipe> match = level.getRecipeManager()
                .getRecipeFor(CoreAltarRecipe.Type.INSTANCE, inventory, level);

        if (match.isPresent()) {

            CompoundTag combinedTag = entity.itemHandler.getStackInSlot(4).getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");

            /*
            TODO:Integrate the Value from the config
             */


            ItemStack stack = new ItemStack(match.get().getResultItem().getItem(), entity.itemHandler.getStackInSlot(9).getCount() + 1);

            for (String tagKey : entity.savedItemNBT.getAllKeys()) {
                if (combinedTag.contains(tagKey)) {
                    combinedTag.putDouble(tagKey, combinedTag.getDouble(tagKey) + entity.savedItemNBT.getDouble(tagKey));
                } else {
                    combinedTag.putDouble(tagKey, entity.savedItemNBT.getDouble(tagKey));
                }
            }

            stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats").merge(combinedTag);

            entity.itemHandler.extractItem(0, 1, false);
            entity.itemHandler.extractItem(1, 1, false);
            entity.itemHandler.extractItem(2, 1, false);
            entity.itemHandler.extractItem(3, 1, false);
            entity.itemHandler.extractItem(4, 1, false);
            entity.itemHandler.extractItem(5, 1, false);
            entity.itemHandler.extractItem(6, 1, false);
            entity.itemHandler.extractItem(7, 1, false);
            entity.itemHandler.extractItem(8, 1, false);


            entity.itemHandler.insertItem(9, stack, false);
            entity.resetProgress();
        }
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(9).getItem() == output.getItem() || inventory.getItem(9).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(9).getMaxStackSize() > inventory.getItem(9).getCount();
    }

    private static boolean validAttributeChange(CoreAltarBlockEntity entity) {
        CompoundTag statsTag = entity.itemHandler.getStackInSlot(4).getOrCreateTagElement(AttributedItemsUtils.TAG_STATS_SUBSTAT);
        boolean tmpBoolean = true;
        for (String s : statsTag.getAllKeys()) {
            //BotanicPledge.LOGGER.info(s);
            if (DivineCoreItem.attributeNameList().contains(s)) {
                if (!(statsTag.getDouble(s) < BotanicPledgeCommonConfigs.DivineCoreMaxValuesFromConfig().get(s))) {
                    entity.hasReachedMaxStat = 1;
                    entity.maxedStat = DivineCoreItem.attributeNameList().indexOf(s);
                    tmpBoolean = false;
                    break;
                } else {
                    entity.hasReachedMaxStat = 0;
                    entity.maxedStat = -1;
                }
            }
        }

        return tmpBoolean;
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Core Altar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inv, Player player) {
        return new CoreAltarMenu(containerID, inv, this, this.data);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("core_altar.progress", progress);
        if (savedItemNBT != null) {
            tag.put("item_saved_nbt", savedItemNBT);
        }

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("core_altar.progress");
        if (nbt.contains("item_saved_nbt")) {
            savedItemNBT = nbt.getCompound("item_saved_nbt");
        }
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean transportedNBT(Optional<CoreAltarRecipe> match) {
        this.savedItemNBT = match.get().extraNBT;
        return savedItemNBT.equals(match.get().extraNBT);
    }

    private void resetNBT() {
        this.savedItemNBT = null;
    }
}
