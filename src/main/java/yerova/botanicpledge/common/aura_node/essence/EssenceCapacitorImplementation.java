package yerova.botanicpledge.common.aura_node.essence;

import net.minecraft.nbt.CompoundTag;
import yerova.botanicpledge.setup.BPEssences;
public class EssenceCapacitorImplementation {

    public EssenceList essenceList = new EssenceList();
    private int maxCapacity = 100;
    private int maxEssenceAmount = 1;

    public EssenceCapacitorImplementation() {
    }

    public EssenceCapacitorImplementation(int maxCapacity, int maxEssenceAmount) {
        this.maxCapacity = maxCapacity;
        this.maxEssenceAmount = maxEssenceAmount;
    }

    public boolean addEssence(Essence essence, int amount) {
        if (essence == null || essence.equals(BPEssences.EMPTY_ESSENCE.get()) || amount <= 0) return false;
        if (isFull(amount)) return false;

        boolean alreadyPresent = essenceList.containsEssence(essence);
        if (!alreadyPresent && essenceList.getEssenceTypes().size() >= maxEssenceAmount) {
            return false;
        }
        this.essenceList.addEssence(essence, amount);
        return true;
    }


    public EssenceList getEssenceList() {
        return this.essenceList;
    }


    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getMaxEssenceAmount() {
        return maxEssenceAmount;
    }

    public void setMaxEssenceAmount(int maxEssenceAmount) {
        this.maxEssenceAmount = maxEssenceAmount;
    }

    public boolean isEmpty() {
        return this.essenceList.isEmpty() && this.getEssenceCount() <= 0;
    }

    public boolean isFull() {
        return this.getEssenceCount() >= this.maxCapacity;
    }

    public boolean isFull(int amount) {
        return this.getEssenceCount() + amount > this.maxCapacity;
    }

    public int getEssenceCount() {
        return this.essenceList.getTotalEssence();
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("MaxCapacity", maxCapacity);
        tag.putInt("MaxEssenceAmount", maxEssenceAmount);
        tag.put("EssenceList", essenceList.toNBT());
        return tag;
    }

    public boolean hasEssence(Essence essence) {
        return this.essenceList.hasEssence(essence);
    }

    public boolean hasEssence(Essence essence, int amount) {
        return this.essenceList.hasEssence(essence) && essenceList.getEssenceAmount(essence) >= amount;
    }

    public static EssenceCapacitorImplementation fromNBT(CompoundTag tag) {

        EssenceCapacitorImplementation result = new EssenceCapacitorImplementation();
        if (tag.contains("MaxCapacity")) {
            result.maxCapacity = tag.getInt("MaxCapacity");
        }
        if (tag.contains("MaxEssenceAmount")) {
            result.maxEssenceAmount = tag.getInt("MaxEssenceAmount");
        }
        if (tag.contains("EssenceList")) {
            result.essenceList = EssenceList.fromNBT(tag.getCompound("EssenceList"));
        }
        return result;
    }

    public void copyFrom(EssenceCapacitorImplementation other) {
        if (other == null) return;

        this.maxCapacity = other.maxCapacity;
        this.maxEssenceAmount = other.maxEssenceAmount;

        // Kopie über NBT, um neue Instanz zu erzwingen
        this.essenceList = EssenceList.fromNBT(other.essenceList.toNBT());
    }

    public int getEssenceAmount(Essence essence) {
        return essenceList.getEssenceAmount(essence);
    }

    public boolean removeEssence(Essence essence, int amount) {
        return essenceList.removeEssence(essence, amount);
    }

    public Essence removeFirstEssence(int amount) {
        Essence e = essenceList.getFirstEntry();
        removeEssence(e, amount);
        return e;
    }

    public Essence getFirstEssence() {
        return essenceList.getFirstEntry();
    }

}
