package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.commons.StaticInitMerger;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.common.block.block_entity.mana.ThrottledPacket;
import yerova.botanicpledge.common.recipes.RecipeUtils;
import yerova.botanicpledge.common.recipes.ore_infusion.IOreInfusionRecipe;
import yerova.botanicpledge.setup.BPBlockEntities;

public class OreInfusionBlockEntity extends RitualBaseBlockEntity
        implements ManaReceiver, SparkAttachable, ThrottledPacket, Wandable {

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
        return false;
    }

    @Override
    public boolean canAttachSpark(ItemStack stack) {
        return false;
    }

    @Override
    public int getAvailableSpaceForMana() {
        return MAX_MANA-getCurrentMana();
    }

    @Override
    public ManaSpark getAttachedSpark() {
        return null;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return false;
    }

    @Override
    public void markDispatchable() {

    }
}
