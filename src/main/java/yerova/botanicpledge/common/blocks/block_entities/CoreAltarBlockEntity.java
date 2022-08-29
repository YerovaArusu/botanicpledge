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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
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
import yerova.botanicpledge.client.screen.CoreAltarMenu;
import yerova.botanicpledge.common.items.ItemInit;
import yerova.botanicpledge.common.recipes.CoreAltarRecipes;

import javax.annotation.Nonnull;

public class CoreAltarBlockEntity extends BlockEntity implements MenuProvider {

    public static final int slotCount = 10;
    public final ItemStackHandler itemHandler = new ItemStackHandler(slotCount) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public CoreAltarBlockEntity( BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.CORE_ALTER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Core Altar");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inv, Player player) {
        return new CoreAltarMenu(containerID, inv, this);
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
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public static void  tick(Level pLevel, BlockPos pPos, BlockState pState, CoreAltarBlockEntity pBlockEntity) {
/*        if(hasRecipe(pBlockEntity) && hasNotReachedStackLimit(pBlockEntity)) {
            craftItem(pBlockEntity);
        }*/

        CoreAltarRecipes.recipeForMariasCore(pBlockEntity);
    }

    private static void craftItem(CoreAltarBlockEntity entity) {
        //entity.itemHandler.getStackInSlot(2).hurt(1, new Random(), null);

        entity.itemHandler.setStackInSlot(9, new ItemStack(ItemInit.MARIAS_CORE.get(),
                entity.itemHandler.getStackInSlot(9).getCount()+1));

        if(entity.itemHandler.getStackInSlot(9).isEmpty()){
            entity.itemHandler.extractItem(0, 1, false);
            entity.itemHandler.extractItem(1, 1, false);
            entity.itemHandler.extractItem(2, 1, false);
            entity.itemHandler.extractItem(3, 1, false);
        }
    }

    private static boolean hasRecipe(CoreAltarBlockEntity entity) {
        boolean hasItemInWaterSlot = PotionUtils.getPotion(entity.itemHandler.getStackInSlot(0)) == Potions.WATER;
        boolean hasItemInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ItemInit.MARINAS_CORE.get();
        boolean hasItemInSecondSlot = entity.itemHandler.getStackInSlot(2).getItem() == Items.DIAMOND_PICKAXE;
        boolean hasItemInThirdSlot = entity.itemHandler.getStackInSlot(3).getItem() == Items.DIAMOND_PICKAXE;

        return hasItemInWaterSlot && hasItemInFirstSlot && hasItemInSecondSlot && hasItemInThirdSlot;
    }

    private static boolean hasNotReachedStackLimit(CoreAltarBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(9).getCount() < entity.itemHandler.getStackInSlot(3).getMaxStackSize();
    }


}
