package yerova.botanicpledge.common.blocks.block_entities.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import yerova.botanicpledge.common.aura_node.essence.*;

import javax.annotation.Nullable;

public class EssenceCapableBlockEntityBase extends BlockEntity implements IEssenceTransportable, IEssenceCapacitor {

    EssenceCapacitorImplementation capacitor;
    EssenceTransportableImplementation transportable;

    public EssenceCapableBlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        transportable = new EssenceTransportableImplementation();
        capacitor = new EssenceCapacitorImplementation();
    }

    public EssenceTransportableImplementation getTransportable() {
        return transportable;
    }

    public void setTransportable(EssenceTransportableImplementation transportable) {
        this.transportable = transportable;
    }

    @Override
    public boolean canReceiveEssenceFromSide(Direction from) {
        return transportable.canReceiveEssenceFromSide(from);
    }

    @Override
    public boolean canExtractEssenceFromSide(Direction to) {
        return transportable.canExtractEssenceFromSide(to);
    }

    @Override
    public int receiveEssence(Direction from, Essence essence, EssenceCapacitorImplementation impl, int amount, boolean simulate) {
        return transportable.receiveEssence(from, essence, impl, amount, simulate);
    }

    @Override
    public int extractEssence(Direction to, Essence essence,EssenceCapacitorImplementation impl, int amount, boolean simulate) {
        return transportable.extractEssence(to, essence, impl, amount, simulate);
    }


    public void essenceTick(Level level, BlockPos blockPos, BlockState blockState, EssenceCapableBlockEntityBase entity) {
        if (level.isClientSide()) return;
        if (entity.getImplementation() == null) setImplementation(new EssenceCapacitorImplementation());
        if (transportable == null) transportable = new EssenceTransportableImplementation();

        for (Direction direction : entity.transportable.getTransferTypeMap().keySet()) {
            BlockPos pos  = blockPos.relative(direction);

            BlockEntity neighbor = level.getBlockEntity(pos);
            if (neighbor instanceof EssenceCapableBlockEntityBase additionalBe) {
                if (entity.canExtractEssenceFromSide(direction) && additionalBe.canReceiveEssenceFromSide(direction.getOpposite())) {
                    if(level.getGameTime()%20 == 0) {
                        transferEssence(entity, additionalBe, direction, 1);
                    }
                }
            }
        }
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
            if (getImplementation() == null) setImplementation(new EssenceCapacitorImplementation());
            setImplementation(EssenceCapacitorImplementation.fromNBT(compound.getCompound("essence_capacitor")));
        }
        if (getImplementation() == null) setImplementation(new EssenceCapacitorImplementation());


        if (compound.contains("essence_transportable")) {
            transportable = EssenceTransportableImplementation.fromNBT(compound);
        }
        if (transportable == null) transportable = new EssenceTransportableImplementation();

    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        if (getImplementation() != null) {tag.put("essence_capacitor", getImplementation().toNBT());}
        if (transportable != null) {tag.put("essence_transportable", transportable.toNBT());}
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

    public static void transferEssence(
            EssenceCapableBlockEntityBase from,
            EssenceCapableBlockEntityBase to,
            Direction direction,
            Essence essence,
            int maxAmount
    ) {
        // 1. Simulieren, wieviel vom "from" extrahiert werden kann
        int extractable = from.extractEssence(direction, essence, from.getImplementation(), maxAmount, true);
        System.out.println(extractable);
        if (extractable <= 0) return;

        // 2. Simulieren, wieviel "to" aufnehmen kann
        int receivable = to.receiveEssence(direction.getOpposite(), essence, to.getImplementation(),extractable, true);
        System.out.println(receivable);
        if (receivable <= 0) return;

        // 3. Tatsächlich extrahieren und einfüllen
        int actuallyExtracted = from.extractEssence(direction, essence,from.getImplementation(), receivable, false);
        System.out.println(actuallyExtracted);
        if (actuallyExtracted > 0) {
            to.receiveEssence(direction.getOpposite(), essence,to.getImplementation(), actuallyExtracted, false);
        }
    }

    public static void transferEssence(
            EssenceCapableBlockEntityBase from,
            EssenceCapableBlockEntityBase to,
            Direction direction,
            int maxAmount
    ) {
        Essence essence = from.getImplementation().getFirstEssence();
        if (essence == null) return;

        transferEssence(from, to, direction, essence, maxAmount);
    }



}
