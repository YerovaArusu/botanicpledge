package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.moddingx.libx.base.tile.BlockEntityBase;
import oshi.util.tuples.Pair;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.AuraNodeNBTHandler;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.IAuraNode;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceList;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPEssences;

import javax.annotation.Nullable;
import java.util.*;

public class AuraNodeBlockEntity extends BlockEntityBase implements IAuraNode {

    public AuraImplementation auraData = new AuraImplementation();



    public AuraNodeBlockEntity( BlockPos pos, BlockState state) {
        super(BPBlockEntities.AURA_NODE.get(), pos, state);
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
        tag.put("Aura", auraData.toNBT());
        super.saveAdditional(tag);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AuraNodeBlockEntity entity) {

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
