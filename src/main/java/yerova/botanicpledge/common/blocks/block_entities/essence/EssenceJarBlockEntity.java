package yerova.botanicpledge.common.blocks.block_entities.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import yerova.botanicpledge.common.aura_node.essence.EssenceCapacitorImplementation;
import yerova.botanicpledge.common.aura_node.essence.IEssenceCapacitor;
import yerova.botanicpledge.common.blocks.essence.EssenceJar;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nullable;

public class EssenceJarBlockEntity extends EssenceCapableBlockEntityBase {

    boolean connected = false;

    public EssenceJarBlockEntity(BlockPos pos, BlockState state) {
        super(BPBlockEntities.ESSENCE_JAR.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, EssenceJarBlockEntity entity) {
        if (!level.isClientSide) {
            entity.essenceTick(level,blockPos,blockState,entity);

            entity.connected = level.getBlockEntity(blockPos.above()) instanceof IEssenceCapacitor;

            blockState = blockState.setValue(EssenceJar.CONNECTED, entity.connected);
            level.setBlock(blockPos, blockState, 3);
            entity.updateBlock();
        }

    }



    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("connected")) {
            connected = compound.getBoolean("connected");
        } else connected = false;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putBoolean("connected", connected);
        super.saveAdditional(tag);
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag() == null ? new CompoundTag() : pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public boolean updateBlock() {
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            setChanged();
            return true;
        }
        return false;
    }
}
