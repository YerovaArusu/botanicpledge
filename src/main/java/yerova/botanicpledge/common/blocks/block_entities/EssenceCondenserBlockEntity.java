package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import yerova.botanicpledge.common.aura_node.essence.EssenceCapacitorImplementation;
import yerova.botanicpledge.common.aura_node.essence.IEssenceCapacitor;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nullable;

public class EssenceCondenserBlockEntity extends BlockEntity implements IEssenceCapacitor {

    EssenceCapacitorImplementation capacitor;

    public EssenceCondenserBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BPBlockEntities.ESSENCE_CONDENSER.get(), pPos, pBlockState);
    }

    @Override
    public EssenceCapacitorImplementation getImplementation() {
        return capacitor;
    }

    @Override
    public void setImplementation(EssenceCapacitorImplementation impl) {
        capacitor = impl;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("essence_capacitor")) {
            if (capacitor == null) { capacitor = new EssenceCapacitorImplementation(); }
            capacitor.copyFrom(EssenceCapacitorImplementation.fromNBT(compound.getCompound("essence_capacitor")));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        if (capacitor != null) {tag.put("essence_capacitor", capacitor.toNBT());}
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
}
