package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.common.block.block_entity.mana.ThrottledPacket;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPBlocks;

public class OreInfusionBlockEntity extends RitualBaseBlockEntity
        implements ManaReceiver, SparkAttachable, ThrottledPacket, Wandable {

    private final int MAX_MANA = 4_000_000;
    private int mana;
    private boolean infusing;

    private static final String TAG_MANA = "mana";
    private static final String TAG_INFUSION = "infusing";

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

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    public void load(CompoundTag compound) {
        mana = compound.getInt(TAG_MANA);
        infusing = compound.getBoolean(TAG_INFUSION);
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt(TAG_MANA, mana);
        tag.putBoolean(TAG_INFUSION, infusing);
        super.saveAdditional(tag);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, OreInfusionBlockEntity oreInfusionBlockEntity) {
        if (level.isClientSide) return;
        ItemStack stack = oreInfusionBlockEntity.getHeldStack();
        boolean infusing = oreInfusionBlockEntity.infusing;

        if (!stack.isEmpty() && !infusing) {

            //TODO: Do this here

            oreInfusionBlockEntity.infusing = true;
            oreInfusionBlockEntity.setChanged();
        }
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
        return 0;
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
