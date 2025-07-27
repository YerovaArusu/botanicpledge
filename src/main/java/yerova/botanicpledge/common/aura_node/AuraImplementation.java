package yerova.botanicpledge.common.aura_node;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceList;
import yerova.botanicpledge.setup.BPEssences;

import java.util.UUID;

public class AuraImplementation {

    private String id;
    private AuraNodeType type;
    private final EssenceList essenceList = new EssenceList();
    private Essence baseEssence = BPEssences.EMPTY_ESSENCE.get();
    private int baseAmount = 0;

    public AuraImplementation() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public AuraNodeType getType() {
        return type;
    }

    public void setType(AuraNodeType type) {
        this.type = type;
    }

    public EssenceList getEssenceList() {
        return essenceList;
    }

    public Essence getBaseEssence() {
        return baseEssence;
    }

    public void setBaseEssence(Essence essence, int amount) {
        this.baseEssence = essence;
        this.baseAmount = amount;
    }

    public int getBaseEssenceAmount() {
        return baseAmount;
    }
    public void setBaseEssenceAmount(int amount) {baseAmount = amount;}

    public int getEssenceAmount(Essence essence) {
        return essenceList.getEssenceAmount(essence);
    }

    public void setEssenceAmount(Essence essence, int amount) {
        essenceList.addEssence(essence, amount);
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("ID", id);
        if (type != null) tag.putString("Type", type.name());
        tag.put("BaseEssence", baseEssence.toNBT());
        tag.putInt("BaseEssenceAmount", baseAmount);
        tag.put("EssenceList", essenceList.toNBT());
        return tag;
    }

    public static AuraImplementation fromNBT(CompoundTag tag) {
        AuraImplementation impl = new AuraImplementation();
        if (tag.contains("ID")) impl.id = tag.getString("ID");
        if (tag.contains("Type")) {
            try {
                impl.type = AuraNodeType.valueOf(tag.getString("Type"));
            } catch (IllegalArgumentException ignored) {}
        }
        if (tag.contains("BaseEssence")) {
            impl.baseEssence = Essence.fromNBT(tag.getCompound("BaseEssence"));
        }
        impl.baseAmount = tag.getInt("BaseEssenceAmount");
        if (tag.contains("EssenceList")) {
            EssenceList list = EssenceList.fromNBT(tag.getCompound("EssenceList"));
            impl.essenceList.copyFrom(list);
        }
        return impl;
    }

    public void copyFrom(AuraImplementation other) {
        if (other == null) return;
        this.id = other.id;
        this.type = other.type;
        this.baseEssence = other.baseEssence;
        this.baseAmount = other.baseAmount;
        this.essenceList.copyFrom(other.essenceList);
    }

    public void removeEssence(Essence essence, int amount) {
        essenceList.removeEssence(essence, amount);
    }

    public Essence removeFirstEssence(int amount) {
        Essence e = essenceList.getFirstEntry();

        removeEssence(e,amount);
        return e;
    }


    public void randomize(Level level) {
        if (level == null) return;

        this.setType(AuraNodeType.getRandomType());

        // Beispiel: zufällige Auswahl aus registrierten Essenzen
        Essence randomEssence = Essence.getRandomEssence();
        int amount = level.random.nextInt(5, 16); // z. B. 5–15 Base Amount
        this.setBaseEssence(randomEssence, amount);

        // Optional: zusätzliche zufällige Nebenessenzen setzen
        int extraEssenceCount = level.random.nextInt(0, 3); // bis zu 2 extra Essenzen
        for (int i = 0; i < extraEssenceCount; i++) {
            Essence extra = Essence.getRandomEssence();
            int extraAmount = level.random.nextInt(1, 8);
            if (!extra.equals(randomEssence)) {
                this.setEssenceAmount(extra, extraAmount);
            }
        }
    }

}
