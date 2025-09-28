package yerova.botanicpledge.common.aura_node.essence;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class EssenceTransportableImplementation implements IEssenceTransportable{

    Map<Direction, TransferType> transferTypeMap = new HashMap<>();

    @SafeVarargs
    public EssenceTransportableImplementation(Pair<Direction, TransferType>... transferTypes) {
        Arrays.stream(transferTypes).forEach(pair -> transferTypeMap.put(pair.getFirst(), pair.getSecond()));
    }

    public EssenceTransportableImplementation() {
        this.transferTypeMap.put(Direction.UP, TransferType.OUT);
        this.transferTypeMap.put(Direction.DOWN, TransferType.IN);
    }

    public void addIO(Direction direction, TransferType transferType) {
        transferTypeMap.put(direction, transferType);
    }

    public Map<Direction, TransferType> getTransferTypeMap() {
        return transferTypeMap;
    }

    public void setTransferTypeMap(Map<Direction, TransferType> transferTypeMap) {
        this.transferTypeMap = transferTypeMap;
    }

    @Override
    public boolean canReceiveEssenceFromSide(Direction side) {
        return transferTypeMap.get(side) == TransferType.IN;

    }

    @Override
    public boolean canExtractEssenceFromSide(Direction side) {
        return transferTypeMap.get(side) == TransferType.OUT;
    }

    @Override
    public int receiveEssence(Direction from, Essence essence,EssenceCapacitorImplementation impl, int amount, boolean simulate) {
        int actuallyReceived = amount;

        if (!canReceiveEssenceFromSide(from)) {
            return actuallyReceived;
        }
        if (impl == null) return actuallyReceived;

        if (impl.getEssenceCount() + amount > impl.getMaxCapacity()) {
            actuallyReceived = impl.getMaxEssenceAmount() -impl.getEssenceCount();
        }

        if (!simulate) impl.addEssence(essence, actuallyReceived);
        return actuallyReceived;
    }

    @Override
    public int extractEssence(Direction to, Essence essence,EssenceCapacitorImplementation impl, int amount, boolean simulate) {
        if (!canExtractEssenceFromSide(to)) return 0;

        if (impl.hasEssence(essence, amount)) {
            if(!simulate) impl.removeEssence(essence, amount);
            return amount;
        } else if (impl.getEssenceAmount(essence) > 0) {
            int toExtract = impl.getEssenceAmount(essence);
            if(!simulate) impl.removeEssence(essence, toExtract);
            return toExtract;
        }
        return 0;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag ioTag = new CompoundTag();

        for (Map.Entry<Direction, TransferType> entry : transferTypeMap.entrySet()) {
            ioTag.putString(entry.getKey().getName(), entry.getValue().type); // z.B. "north" -> "input"
        }

        tag.put("IOMap", ioTag);
        return tag;
    }


    public static EssenceTransportableImplementation fromNBT(CompoundTag superTag) {

        EssenceTransportableImplementation transportable = new EssenceTransportableImplementation();
        transportable.transferTypeMap.clear();

        CompoundTag tag = superTag.getCompound("essence_transportable");

        if (tag.contains("IOMap")) {
            CompoundTag ioTag = tag.getCompound("IOMap");
            for (Direction dir : Direction.values()) {
                if (ioTag.contains(dir.getName())) {
                    String typeString = ioTag.getString(dir.getName());
                    TransferType type = Arrays.stream(TransferType.values())
                            .filter(t -> t.type.equals(typeString))
                            .findFirst()
                            .orElse(null);

                    if (type != null) {
                        transportable.transferTypeMap.put(dir, type);
                    }
                }
            }
        }
        return transportable;
    }




    public enum TransferType{

        IN("input"),
        OUT("output");


        public final String type;
        TransferType(String type) {
            this.type = type;
        }
    }
}
