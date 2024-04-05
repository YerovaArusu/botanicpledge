package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import yerova.botanicpledge.common.items.RuneGemItem;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BPItems;

import java.util.*;

public class Attribute {

    private int maxRunes;
    private List<Rune> runesOnSockets;
    private Rune.EquipmentType requiredType;

    public Attribute(int maxRunes, Rune.EquipmentType requiredType) {
        this.maxRunes = maxRunes;
        runesOnSockets = new ArrayList<>(maxRunes); // Pre-allocate for efficiency
        this.requiredType = requiredType;
    }

    public boolean hasEmptySocket() {
        return runesOnSockets.size() < maxRunes;
    }

    public void clearSockets() {
        runesOnSockets.clear();
    }

    public void setMaxRunes(int maxRunes) {
        this.maxRunes = maxRunes;
    }

    public int getMaxRunes() {
        return this.maxRunes;
    }

    public List<Rune> getAllRunes() {
        return Collections.unmodifiableList(runesOnSockets);
    }

    public boolean addRune(Rune rune) {
        if (!hasEmptySocket()|| !isCorrectRuneType(rune)) {
            return false;
        }
        runesOnSockets.add(rune);
        return true;
    }

    public boolean isCorrectRuneType(Rune rune) {
        return rune.getEquipmentType() == requiredType;
    }

    public boolean removeRune(Rune rune) {
        return runesOnSockets.remove(rune);
    }
    public void removeRuneAt(int i) {
        runesOnSockets.remove(i);
    }
    public boolean hasRuneType(Rune.StatType type) {
        return runesOnSockets.stream().map(r -> r.statType).toList().contains(type);
    }
    public double sumRunesOfType(Rune.StatType type) {
        return runesOnSockets.stream()
                .filter(r -> r.statType == type)
                .mapToDouble(r -> r.value)
                .sum();
    }
    public void update(List<Rune> runes, boolean isMax) {
        if (isMax) {
            maxRunes = runes.size();
        }
        runesOnSockets = runes;
    }

    public Rune getRune(int index) {
        return runesOnSockets.get(index);
    }

    public int getIndexOfRune(Rune rune) {
        return runesOnSockets.contains(rune) ? runesOnSockets.indexOf(rune) : 0;
    }

    public void saveNBTData(CompoundTag nbt) {
        for (int i = 0; i < Math.min(runesOnSockets.size(), maxRunes); i++) {
            runesOnSockets.get(i).toNBT(i, nbt);
        }

        nbt.putInt("sockets.max", maxRunes);
        nbt.putString("sockets.equipment_type", requiredType.name().toLowerCase());
    }

    public void loadNBTData(CompoundTag nbt) {
        runesOnSockets = Rune.getAll(nbt);
        maxRunes = nbt.getInt("sockets.max");
        requiredType = nbt.get("sockets.equipment_type") != null ?
                Rune.EquipmentType.valueOf(nbt.getString("sockets.equipment_type").toUpperCase()) : Rune.EquipmentType.NONE;

    }

    public static class Rune {

        public enum EquipmentType {
            SWORD, DIVINE_CORE, NONE
        }

        public enum StatType {
            ARMOR,ARMOR_TOUGHNESS,MAX_HEALTH,MOVEMENT_SPEED,ATTACK_SPEED,ATTACK_DAMAGE,JUMP_HEIGHT,NONE;
        }

        private final EquipmentType equipmentType;
        private final StatType statType;
        private final double value;

        public Rune(EquipmentType equipmentType, StatType statType, double statValue) {
            this.equipmentType = equipmentType;
            this.statType = statType;
            this.value = statValue;
        }


        public ItemStack getAsStack() {
            ItemStack stack = new ItemStack(BPItems.SOCKET_GEM.get());
            CompoundTag tag = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
            tag.putString("gem_equipment_type",equipmentType.name().toLowerCase());
            tag.putString("gem_stat_type", statType.name().toLowerCase());
            tag.putDouble("gem_stat_value", value);

            return stack;
        }

        public EquipmentType getEquipmentType() {
            return equipmentType;
        }

        public StatType getStatType() {
            return statType;
        }

        public double getValue() {
            return value;
        }

        public Rune get(CompoundTag tag, int index) {
            return getAll(tag).get(index);
        }

        public static Rune getRuneFromStack(ItemStack stack) {
            return new Rune(RuneGemItem.getGemEquipmentType(stack), RuneGemItem.getGemAttributeType(stack), RuneGemItem.getGemAttributeValue(stack));
        }


        public static List<Rune> getAll(CompoundTag tag) {
            List<Rune> runes = new ArrayList<>();
            tag.getAllKeys().forEach(keyString -> {
                for (StatType statType : Arrays.stream(StatType.values()).filter(et1 -> et1 != StatType.NONE).toList()) {
                    if (!keyString.contains(statType.name().toLowerCase())) continue;
                    for (EquipmentType equipmentType : Arrays.stream(EquipmentType.values()).filter(et1 -> et1 != EquipmentType.NONE).toList()) {
                        if (keyString.contains(equipmentType.name().toLowerCase())) {
                            runes.add(new Rune(equipmentType, statType, tag.getDouble(keyString)));
                            break;
                        }
                    }
                }
            });
            return runes;
        }


        public void toNBT(int index, CompoundTag tag) {
            tag.putDouble(BPConstants.SOCKET_PRE_TAG + "." + index + "." + equipmentType.name().toLowerCase() + "." + statType.name().toLowerCase(), value);
        }
    }

}
