package yerova.botanicpledge.common.blocks.block_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.ParticleUtils;
import yerova.botanicpledge.client.particle.custom.YggdralParticleData;
import yerova.botanicpledge.client.utils.ClientUtils;
import yerova.botanicpledge.common.blocks.RitualCenterBlock;
import yerova.botanicpledge.common.recipes.ritual.IBotanicRitualRecipe;
import yerova.botanicpledge.common.recipes.ritual.RecipeUtils;
import yerova.botanicpledge.common.utils.ManaUtils;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RitualCenterBlockEntity extends RitualBaseBlockEntity implements Wandable {

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    private int counter = 0;

    boolean isCrafting = false;

    public RitualCenterBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BPBlockEntities.RITUAL_CENTER.get(), p_155229_, p_155230_);


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
                tile.setHeldStack(tile.getHeldStack().getCraftingRemainingItem());
                BlockState state = level.getBlockState(blockPos);
                level.sendBlockUpdated(blockPos, state, state, 3);
            }
        }
    }

    public IBotanicRitualRecipe getRecipe(ItemStack stack, @Nullable Player playerEntity) {
        List<ItemStack> pedestalItems = getPedestalItems();
        return RecipeUtils.getBotanicRitualRecipes(level).stream().filter(r -> r.isMatch(pedestalItems, stack, this, playerEntity)).findFirst().orElse(null);
    }

    public boolean attemptCraft(ItemStack catalyst, @Nullable Player playerEntity) {
        IBotanicRitualRecipe recipe = this.getRecipe(catalyst, playerEntity);
        if (recipe == null) return false;

        if (isCrafting) {
            return false;
        }
        if (!craftingPossible(catalyst, playerEntity)) {
            return false;
        }

        if (recipe.consumesMana()) ManaUtils.takeManaFromMultipleSources(worldPosition, level, recipe.getManaCost());
        this.isCrafting = true;
        updateBlock();
        return true;
    }

    public boolean craftingPossible(ItemStack stack, Player playerEntity) {
        if (stack.isEmpty()) return false;
        IBotanicRitualRecipe recipe = this.getRecipe(stack, playerEntity);
        return recipe != null && (!recipe.consumesMana() || (recipe.consumesMana() && ManaUtils.hasEnoughManaInRitual(worldPosition, level, recipe.getManaCost())));
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
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
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

    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        if (player == null || player.isShiftKeyDown()) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }
        return true;
    }

    public static class Hud {
        public static void render(RitualCenterBlockEntity center, GuiGraphics gui, Minecraft mc) {
            PoseStack ms = gui.pose();
            int xc = mc.getWindow().getGuiScaledWidth() / 2;
            int yc = mc.getWindow().getGuiScaledHeight() / 2;

            List<ItemStack> pedestals = center.getPedestalItems();
            ItemStack reagent = center.getHeldStack();
            IBotanicRitualRecipe recipe = center.getRecipe(reagent, mc.player);

            float angle = -90;
            int radius = 48;
            int amt = 0;
            for (int i = 0; i < pedestals.size(); i++) {
                if (pedestals.get(i).isEmpty()) {
                    break;
                }
                amt++;
            }

            if (!reagent.isEmpty()) {
                ms.pushPose();
                ms.translate(xc - 8, yc - 8, 0);
                gui.renderFakeItem(reagent, 0, 0);
                ms.popPose();
            }

            if (amt > 0) {
                float anglePer = 360F / amt;

                for (int i = 0; i < amt; i++) {
                    double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
                    double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
                    ms.pushPose();
                    ms.translate(xPos, yPos, 0);
                    gui.renderFakeItem(pedestals.get(i), 0, 0);
                    ms.popPose();

                    angle += anglePer;
                }
            }

            if (recipe != null) {

                int manaRequired = recipe.getManaCost();
                int manaAvailable = ManaUtils.getAvailableManaInRitual(center.worldPosition, center.level);
                if (manaRequired > 0) {
                    int x = mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width("") / 2;
                    int y = mc.getWindow().getGuiScaledHeight() / 2 + (int) (1.5 * radius);

                    ClientUtils.drawManaHUD(gui, x, y, 0x4444FF, manaAvailable, recipe.getManaCost(), "");

                }


                ms.pushPose();
                ms.translate(xc + (radius * 2) - 8, yc - 8, 0);
                gui.renderFakeItem(recipe.getResult(pedestals, reagent, center), 0, 0);
                ms.popPose();
            }

        }
    }


}
