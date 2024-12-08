package yerova.botanicpledge.common.blocks.block_entities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.commons.StaticInitMerger;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.block_entity.mana.ThrottledPacket;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.ManaTabletItem;
import yerova.botanicpledge.common.recipes.RecipeUtils;
import yerova.botanicpledge.common.recipes.ore_infusion.IOreInfusionRecipe;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nullable;

public class OreInfusionBlockEntity extends RitualBaseBlockEntity
        implements ManaReceiver, ThrottledPacket, Wandable {

    private final int MAX_MANA = 4_000_000;
    private final int MAX_TIME = 200; //max duration in ticks (10s)
    private int mana;
    private boolean infusing;
    private int timer;

    private static final String TAG_MANA = "mana";
    private static final String TAG_INFUSION = "infusing";
    private static final String TAG_TIMER = "timer";

    public OreInfusionBlockEntity(BlockPos blockPos, BlockState state) {
        super(BPBlockEntities.ORE_INFUSION.get(), blockPos, state);
    }

    @Override
    public Level getManaReceiverLevel() {
        return level;
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return this.getBlockPos();
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public boolean isFull() {
        return getCurrentMana() >= MAX_MANA;
    }

    @Override
    public void receiveMana(int mana) {
        int old = this.mana;
        this.mana = Math.max(0, Math.min(getCurrentMana() + mana, MAX_MANA));
        if (old != this.mana) {
            setChanged();
        }
    }

    public boolean hasEnoughMana(int manaToHave){
        return getCurrentMana() >= manaToHave;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    public void load(CompoundTag compound) {
        mana = compound.getInt(TAG_MANA);
        infusing = compound.getBoolean(TAG_INFUSION);
        timer = compound.getInt(TAG_TIMER);
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt(TAG_MANA, mana);
        tag.putBoolean(TAG_INFUSION, infusing);
        tag.putInt(TAG_TIMER, timer);
        super.saveAdditional(tag);
    }



    public static void tick(Level level, BlockPos blockPos, BlockState blockState, OreInfusionBlockEntity oreInfusionBlockEntity) {
        if (level.isClientSide) return;
        ItemStack stack = oreInfusionBlockEntity.getHeldStack();



        if (stack.isEmpty()) {
            oreInfusionBlockEntity.infusing = false;
            return;
        }

        IOreInfusionRecipe recipe = RecipeUtils.getOreInfusionRecipes(level).stream().filter(r -> r.isMatch(stack, oreInfusionBlockEntity, null)).findFirst().orElse(null);

        if (recipe == null) {
            oreInfusionBlockEntity.infusing = false;
            return;
        }

        if (recipe != null && !oreInfusionBlockEntity.infusing && oreInfusionBlockEntity.hasEnoughMana(recipe.getManaCost())) {
            oreInfusionBlockEntity.infusing = true;
            oreInfusionBlockEntity.timer = oreInfusionBlockEntity.MAX_TIME;
        }

        if (oreInfusionBlockEntity.infusing) {
            if (oreInfusionBlockEntity.timer > 0) {
                oreInfusionBlockEntity.timer--;
                oreInfusionBlockEntity.mana -= Math.ceil(recipe.getManaCost()/oreInfusionBlockEntity.MAX_TIME);
            }
            if (oreInfusionBlockEntity.timer == 0) {
                oreInfusionBlockEntity.infusing = false;
                oreInfusionBlockEntity.heldStack =  recipe.getResult(stack, oreInfusionBlockEntity);
            }
        }
        oreInfusionBlockEntity.setChanged();
    }


    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        if (player == null || player.isShiftKeyDown()) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }
        return true;
    }

    @Override
    public void markDispatchable() {

    }

    public static class WandHud implements WandHUD {
        private final OreInfusionBlockEntity pool;

        public WandHud(OreInfusionBlockEntity pool) {
            this.pool = pool;
        }

        @Override
        public void renderHUD(GuiGraphics ms, Minecraft mc) {
            ItemStack poolStack = new ItemStack(pool.getBlockState().getBlock());
            String name = poolStack.getHoverName().getString();
            int color = 0x4444FF;
            BotaniaAPIClient.instance().drawSimpleManaHUD(ms, color, pool.getCurrentMana(), ManaBufferBlockEntity.MAX_MANA, name);

            int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 11;
            int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + 30;

            int u = 22;
            int v = 38;

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            RenderSystem.setShaderTexture(0, HUDHandler.manaBar);
            RenderHelper.drawTexturedModalRect(ms, HUDHandler.manaBar, x, y, u, v, 22, 15);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

            ItemStack tablet = new ItemStack(BotaniaItems.manaTablet);
            ManaTabletItem.setStackCreative(tablet);

            RenderHelper.renderItemWithNameCentered(ms, mc, tablet, x - 20, color);
            RenderHelper.renderItemWithNameCentered(ms, mc, poolStack, x + 26, color);


            RenderSystem.disableBlend();
        }
    }
}
