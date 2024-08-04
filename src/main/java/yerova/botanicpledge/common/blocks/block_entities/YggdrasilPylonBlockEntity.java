package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;

public class YggdrasilPylonBlockEntity extends BlockEntity {
    private boolean canGenerateMana;

    public YggdrasilPylonBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BPBlockEntities.YGGDRASIL_PYLON.get(), pPos, pBlockState);
        this.canGenerateMana = false;
    }


    public boolean canGenerateMana() {
        return canGenerateMana;
    }

    public void setCanGenerateMana(boolean canGenerateMana) {
        this.canGenerateMana = canGenerateMana;
        setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("CanGenerateMana", canGenerateMana);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.canGenerateMana = nbt.getBoolean("CanGenerateMana");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("CanGenerateMana", canGenerateMana);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        this.canGenerateMana = nbt.getBoolean("CanGenerateMana");
    }


    public static void tick(Level level, BlockPos pos, BlockState blockState, YggdrasilPylonBlockEntity entity) {
        if (entity.canGenerateMana() && level.getBlockEntity(pos.below()) instanceof ManaPoolBlockEntity pool){
            pool.receiveMana(500);
        }
    }
}
