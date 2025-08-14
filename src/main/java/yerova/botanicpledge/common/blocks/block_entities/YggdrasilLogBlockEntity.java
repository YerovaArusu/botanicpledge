package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.moddingx.libx.base.tile.BlockEntityBase;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.IAuraNode;
import yerova.botanicpledge.common.aura_node.essence.IEssenceHolder;

import javax.annotation.Nullable;

public class YggdrasilLogBlockEntity extends BlockEntityBase implements IAuraNode {

    public AuraImplementation auraData = new AuraImplementation();

    public YggdrasilLogBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public AuraImplementation getImplementation() {
        return auraData;
    }

    @Override
    public void setImplementation(AuraImplementation implementation) {
        auraData = implementation;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Aura")) {
            auraData.copyFrom(AuraImplementation.fromNBT(compound.getCompound("Aura")));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        if (auraData != null) {tag.put("Aura", auraData.toNBT());}
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

    public static void tick(Level level, BlockPos pos, BlockState state, YggdrasilLogBlockEntity entity) {
        if (entity.getImplementation() != null) {
            AuraImplementation.regenerateEssences(level, entity);
        }
    }

}
