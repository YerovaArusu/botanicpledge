package yerova.botanicpledge.common.aura_node.essence;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import yerova.botanicpledge.common.aura_node.essence.Essence;

import java.util.LinkedHashMap;
import java.util.Map;

public class EssenceList {
    private final LinkedHashMap<Essence, Integer> essenceMap;

    public EssenceList() {
        this.essenceMap = new LinkedHashMap<>();
    }

    public void addEssence(Essence essence, int amount) {
        essenceMap.put(essence, essenceMap.getOrDefault(essence, 0) + amount);
    }

    public boolean removeEssence(Essence essence, int amount) {
        if (essenceMap.containsKey(essence)) {
            int newAmount = essenceMap.get(essence) - amount;
            if (newAmount <= 0) {
                essenceMap.remove(essence);
            } else {
                essenceMap.put(essence, newAmount);
            }
            return true;
        }
        return false;
    }

    public int getEssenceAmount(Essence essence) {
        return essenceMap.getOrDefault(essence, 0);
    }

    public boolean hasEssence(Essence essence) {
        return essenceMap.containsKey(essence);
    }

    public int getTotalEssence() {
        return essenceMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<Essence, Integer> getEssences() {
        return essenceMap;
    }

    public void clear() {
        essenceMap.clear();
    }

    public void copyFrom(EssenceList other) {
        this.essenceMap.clear();
        this.essenceMap.putAll(other.getEssences());
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();

        for (Map.Entry<Essence, Integer> entry : essenceMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            Essence essence = entry.getKey();
            entryTag.putString("Item", net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(essence.getItemBase()).toString());
            entryTag.putInt("Color", essence.getColor());
            entryTag.putInt("Amount", entry.getValue());
            listTag.add(entryTag);
        }

        tag.put("Essences", listTag);
        return tag;
    }

    public static EssenceList fromNBT(CompoundTag tag) {
        EssenceList list = new EssenceList();
        if (!tag.contains("Essences", Tag.TAG_LIST)) return list;

        ListTag listTag = tag.getList("Essences", Tag.TAG_COMPOUND);

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag entryTag = listTag.getCompound(i);
            String itemName = entryTag.getString("Item");
            int color = entryTag.getInt("Color");
            int amount = entryTag.getInt("Amount");

            for (Essence essence : Essence.getRegisteredEssences()) {
                if (net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(essence.getItemBase()).toString().equals(itemName)
                        && essence.getColor() == color) {
                    list.addEssence(essence, amount);
                    break;
                }
            }
        }

        return list;
    }

    public Essence getFirstEntry() {
        return essenceMap.keySet().stream().findFirst().orElse(null);
    }

    public boolean isEmpty() {
        return essenceMap.isEmpty();
    }
}
