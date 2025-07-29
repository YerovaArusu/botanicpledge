package yerova.botanicpledge.common.aura_node;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    private int nodeRank = 1;


    private final int BASE_ESSENCE_MAX = 12;
    private final int ESSENCE_MAX = 6;

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
        this.baseAmount = Math.min(amount, BASE_ESSENCE_MAX *nodeRank);
    }

    public int getBaseEssenceAmount() {
        return baseAmount;
    }

    public void setBaseEssenceAmount(int amount) {
        this.baseAmount = Math.min(amount, BASE_ESSENCE_MAX * nodeRank);
    }

    public int getEssenceAmount(Essence essence) {
        return essenceList.getEssenceAmount(essence);
    }

    public void setEssenceAmount(Essence essence, int amount) {
        if (essenceList.getEssenceAmount(essence) + amount <= ESSENCE_MAX * nodeRank) {
            essenceList.addEssence(essence, amount);
        }
    }

    public int getNodeRank() {
        return nodeRank;
    }

    public void setNodeRank(int nodeRank) {
        this.nodeRank = nodeRank;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("ID", id);
        tag.putInt("NodeRank", nodeRank);
        if (type != null) tag.putString("Type", type.name());
        tag.put("BaseEssence", baseEssence.toNBT());
        tag.putInt("BaseEssenceAmount", baseAmount);
        tag.put("EssenceList", essenceList.toNBT());
        return tag;
    }

    public static AuraImplementation fromNBT(CompoundTag tag) {
        AuraImplementation impl = new AuraImplementation();
        if (tag.contains("ID")) impl.id = tag.getString("ID");
        if (tag.contains("NodeRank")) impl.nodeRank = tag.getInt("NodeRank");
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
        this.nodeRank = other.nodeRank;
        this.essenceList.copyFrom(other.essenceList);
    }

    public void removeEssence(Essence essence, int amount) {
        essenceList.removeEssence(essence, amount);
    }

    public boolean addEssence(Essence essence, int amount) {
        int maxBase = BASE_ESSENCE_MAX * nodeRank;
        int maxPerEssence = ESSENCE_MAX * nodeRank;

        if (baseEssence == BPEssences.EMPTY_ESSENCE.get() || baseEssence == null) {
            baseEssence = essence;
            baseAmount = Math.min(amount, maxBase);
            return true;
        } else if (essence.equals(baseEssence)) {
            int newAmount = Math.min(baseAmount + amount, maxBase);
            if (newAmount > baseAmount) {
                baseAmount = newAmount;
                return true;
            }
            return false;
        } else {
            boolean isKnown = essenceList.hasEssence(essence);
            boolean hasFreeSlot = !isKnown && essenceList.size() < nodeRank;

            if (isKnown || hasFreeSlot) {
                int current = essenceList.getEssenceAmount(essence);
                int toAdd = Math.min(amount, maxPerEssence - current);
                if (toAdd > 0) {
                    essenceList.addEssence(essence, toAdd);
                    return true;
                }
            }
        }

        return false;
    }


    public Essence removeFirstEssence(int amount) {
        Essence e = essenceList.getFirstEntry();
        removeEssence(e, amount);
        return e;
    }

    public Essence getFirstEssence() {
        return essenceList.getFirstEntry();
    }

    public boolean contains(Essence essence) {
        return essenceList.hasEssence(essence) || baseEssence.equals(essence);
    }

    public void randomize(Level level) {
        if (level == null) return;

        this.setType(AuraNodeType.getRandomType());

        this.nodeRank = level.random.nextInt(1, 8);

        Essence randomEssence = Essence.getRandomEssence();
        int amount = level.random.nextInt(Math.min(5, nodeRank), Math.min(16, nodeRank + 1));
        this.setBaseEssence(randomEssence, amount);

        int extraEssenceCount = level.random.nextInt(1, this.nodeRank+1);
        for (int i = 0; i < extraEssenceCount; i++) {
            Essence extra = Essence.getRandomEssence(randomEssence);
            int extraAmount = level.random.nextInt(1, 8);
            if (!extra.equals(randomEssence)) {
                this.setEssenceAmount(extra, extraAmount);
            }
        }
    }

    public int getNodeColor() {
        int totalWeight = 0;

        double rSum = 0;
        double gSum = 0;
        double bSum = 0;

        final double GAMMA = 2.2;

        for (var entry : essenceList.getEssenceMap().entrySet()) {
            Essence essence = entry.getKey();
            int amount = entry.getValue();
            int color = essence.color();

            double r = Math.pow(((color >> 16) & 0xFF) / 255.0, GAMMA);
            double g = Math.pow(((color >> 8) & 0xFF) / 255.0, GAMMA);
            double b = Math.pow((color & 0xFF) / 255.0, GAMMA);

            rSum += r * amount;
            gSum += g * amount;
            bSum += b * amount;
            totalWeight += amount;
        }

        if (baseEssence != null && baseAmount > 0) {
            int color = baseEssence.color();
            double r = Math.pow(((color >> 16) & 0xFF) / 255.0, GAMMA);
            double g = Math.pow(((color >> 8) & 0xFF) / 255.0, GAMMA);
            double b = Math.pow((color & 0xFF) / 255.0, GAMMA);

            int weightedBase = baseAmount * 2;
            rSum += r * weightedBase;
            gSum += g * weightedBase;
            bSum += b * weightedBase;
            totalWeight += weightedBase;
        }

        if (totalWeight == 0) {
            return 0xAAAAAA;
        }

        double rAvg = Math.pow(rSum / totalWeight, 1.0 / GAMMA);
        double gAvg = Math.pow(gSum / totalWeight, 1.0 / GAMMA);
        double bAvg = Math.pow(bSum / totalWeight, 1.0 / GAMMA);

        int r = Math.min(255, Math.max(0, (int) Math.round(rAvg * 255)));
        int g = Math.min(255, Math.max(0, (int) Math.round(gAvg * 255)));
        int b = Math.min(255, Math.max(0, (int) Math.round(bAvg * 255)));

        return (r << 16) | (g << 8) | b;
    }

    public static void regenerateEssences(Level level, BlockEntity blockEntity) {
        if (level.isClientSide || !(blockEntity instanceof IAuraNode auraNode)) return;

        AuraImplementation impl = auraNode.getImplementation();

        if (impl == null || impl.essenceList.size() <= 0 || impl.baseEssence == null || impl.baseAmount <= 0) return;

        if (level.getGameTime() % 12000L == 0L) {

            impl.setBaseEssenceAmount(impl.getBaseEssenceAmount() + 1);

            for (var entry : impl.getEssenceList().getEssenceMap().entrySet()) {
                Essence essence = entry.getKey();
                if (essence == BPEssences.EMPTY_ESSENCE.get()) continue;

                impl.addEssence(essence,1);
            }

            blockEntity.setChanged();
            level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
        }
    }

}
