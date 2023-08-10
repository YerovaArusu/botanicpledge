package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;
import yerova.botanicpledge.common.utils.BPConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoreAttribute {
    private int maxCharge;
    private int maxShield;
    private int currentCharge;
    private int currentShield;
    private int defRegenPerTick;
    private int manaCostPerTick;
    private HashMap<Integer, Map.Entry<String, Double>> sockets = AttributedItemsUtils.getCoreAttributeDefault();
    ;

    public CoreAttribute(int maxCharge, int maxShield, int defRegenPerTick, int manaCostPerTick) {
        this.maxCharge = maxCharge;
        this.maxShield = maxShield;
        this.defRegenPerTick = defRegenPerTick;
        this.manaCostPerTick = manaCostPerTick;
        this.sockets = AttributedItemsUtils.getCoreAttributeDefault();
        ;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public void setMaxCharge(int maxCharge) {
        this.maxCharge = maxCharge;
    }

    public int getMaxShield() {
        return maxShield;
    }

    public void setMaxShield(int maxShield) {
        this.maxShield = maxShield;
    }

    public int getCurrentCharge() {
        return currentCharge;
    }

    public int getCurrentShield() {
        return currentShield;
    }

    public int getDefRegenPerTick() {
        return defRegenPerTick;
    }

    public int getManaCostPerTick() {
        return manaCostPerTick;
    }

    public void setManaCostPerTick(int manaCostPerTick) {
        this.manaCostPerTick = manaCostPerTick;
    }

    public void setDefRegenPerTick(int defRegenPerTick) {
        this.defRegenPerTick = defRegenPerTick;
    }

    public void addCurrentCharge(int charge) {
        this.currentCharge = Math.min(this.currentCharge + charge, maxCharge);
    }

    public void removeCurrentCharge(int charge) {
        this.currentCharge = Math.max(this.currentCharge - charge, 0);
    }

    public void addCurrentShield(int shield) {
        this.currentShield = Math.min(this.currentShield + shield, maxShield);
    }

    public void removeCurrentShield(int shield) {
        this.currentShield = Math.max(this.currentShield - shield, 0);
    }

    public int getIndexOfSocketAttribute(String s) {
        int indexToReturn = 0;
        for (int i : sockets.keySet()) {
            if (i <= BPConstants.MAX_SOCKETS && sockets.get(i).getKey().equals(s)) {
                indexToReturn = i;
            }
        }
        return indexToReturn;
    }

    public boolean hasSocketAttribute(String toSearchFor) {
        boolean toReturn = false;
        for (int i = 1; i < BPConstants.MAX_SOCKETS; i++) {
            if (toSearchFor.equals(getSocketAttributeNameByIndex(i))) {
                toReturn = true;
                break;
            }
        }
        return toReturn;
    }

    public String getSocketAttributeNameByIndex(int i) {

        return this.sockets.get(i) != null ? this.sockets.get(i).getKey() : BPConstants.NO_RUNE_GEM;

    }

    public double getSocketAttributeValueByIndex(int i) {
        return sockets.get(i).getValue();
    }

    public boolean hasEmptySocket() {
        return sockets.containsValue(Map.entry(BPConstants.NO_RUNE_GEM, 0.0));
    }

    public int getEmptySocketIndex() {
        int toReturn = -1;
        for (int i : sockets.keySet()) {
            if (sockets.get(i).getKey().equals(BPConstants.NO_RUNE_GEM)) {
                toReturn = i;
                break;
            }
        }
        return toReturn;
    }

    public void setSocketAttribute(int index, String AttributeName, double AttributeValue) {
        sockets.put(index, Map.entry(AttributeName, AttributeValue));
    }

    public HashMap<Integer, Map.Entry<String, Double>> getAttributes() {
        return sockets;
    }

    public ArrayList<Map.Entry<String, Double>> getAttributesNamesAndValues() {
        ArrayList<Map.Entry<String, Double>> arrayList = new ArrayList<>();

        for (int i : sockets.keySet()) {
            arrayList.add(Map.entry(sockets.get(i).getKey(), sockets.get(i).getValue()));
        }
        return arrayList;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt(BPConstants.MAX_CHARGE_TAG_NAME, maxCharge);
        nbt.putInt(BPConstants.MAX_SHIELD_TAG_NAME, maxShield);
        nbt.putInt(BPConstants.CHARGE_TAG_NAME, currentCharge);
        nbt.putInt(BPConstants.SHIELD_TAG_NAME, currentShield);
        nbt.putInt(BPConstants.DEF_REGEN_TAG_NAME, defRegenPerTick);
        nbt.putInt(BPConstants.MANA_COST_TAG_NAME, manaCostPerTick);


        for (int i = 1; i <= BPConstants.MAX_SOCKETS; i++) {
            if (sockets.get(i) != null) {
                nbt.putDouble(BPConstants.SOCKET_PRE_TAG + "." + i + "." + getSocketAttributeNameByIndex(i), getSocketAttributeValueByIndex(i));
            } else {
                nbt.putDouble(BPConstants.SOCKET_PRE_TAG + "." + i + "." + BPConstants.NO_RUNE_GEM, 0.0);
            }
        }


    }

    public void loadNBTData(CompoundTag nbt) {
        maxCharge = nbt.getInt(BPConstants.MAX_CHARGE_TAG_NAME);
        maxShield = nbt.getInt(BPConstants.MAX_SHIELD_TAG_NAME);
        currentCharge = nbt.getInt(BPConstants.CHARGE_TAG_NAME);
        currentShield = nbt.getInt(BPConstants.SHIELD_TAG_NAME);
        defRegenPerTick = nbt.getInt(BPConstants.DEF_REGEN_TAG_NAME);
        manaCostPerTick = nbt.getInt(BPConstants.MANA_COST_TAG_NAME);

        for (String s : nbt.getAllKeys()) {
            if (s.contains(BPConstants.SOCKET_PRE_TAG)) {
                String[] tokens = s.split("\\.");
                sockets.put(Integer.parseInt(tokens[1]), Map.entry(tokens[2], nbt.getDouble(s)));
            }
        }
    }


}
