package yerova.botanicpledge.common.aura_node;

import net.minecraft.nbt.CompoundTag;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceList;

public class AuraNodeNBTHandler {

    private static final String KEY_ID = "NodeID";
    private static final String KEY_TYPE = "NodeType";
    private static final String KEY_BASE_ESSENCE = "BaseEssence";
    private static final String KEY_BASE_ESSENCE_AMOUNT = "BaseEssenceAmount";
    private static final String KEY_ESSENCES = "EssenceList";

    public static CompoundTag writeToNBT(CompoundTag tag, IAuraNode node) {

        AuraImplementation imp = node.getImplementation();

        if (imp.getType() == null || imp.getBaseEssenceAmount() <= 0) return tag;

        tag.putString(KEY_ID, imp.getId());
        tag.putString(KEY_TYPE, imp.getType().name());



        // Base Essence
        Essence baseEssence = imp.getBaseEssence();
        tag.put(KEY_BASE_ESSENCE, baseEssence.toNBT());
        tag.putInt(KEY_BASE_ESSENCE_AMOUNT, imp.getBaseEssenceAmount());


        // Full essence list
        tag.put(KEY_ESSENCES, imp.getEssenceList().toNBT());

        return tag;
    }

    public static void readFromNBT(IAuraNode node, CompoundTag tag) {
        if (!tag.contains(KEY_ID)) return;
        AuraImplementation imp = node.getImplementation();

        // Aura Node Type
        if (tag.contains(KEY_TYPE)) {
            try {
                imp.setType(AuraNodeType.valueOf(tag.getString(KEY_TYPE)));
            } catch (IllegalArgumentException ignored) {
                // Invalid enum string, skip
            }
        }

        // Base Essence and amount
        if (tag.contains(KEY_BASE_ESSENCE) && tag.contains(KEY_BASE_ESSENCE_AMOUNT)) {
            Essence baseEssence = Essence.fromNBT(tag.getCompound(KEY_BASE_ESSENCE));
            int baseAmount = tag.getInt(KEY_BASE_ESSENCE_AMOUNT);
            imp.setBaseEssence(baseEssence, baseAmount);
        }

        // EssenceList
        if (tag.contains(KEY_ESSENCES)) {
            EssenceList loadedList = EssenceList.fromNBT(tag.getCompound(KEY_ESSENCES));
            imp.getEssenceList().copyFrom(loadedList);
        }
    }
}
