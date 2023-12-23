package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.BPItemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BPAttribute {

    private HashMap<Integer, Map.Entry<String, Double>> sockets;

    public BPAttribute() {
        this.sockets = BPItemUtils.getBPAttributeDefault();
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

    public boolean setSocketAttribute(int index, String AttributeName, double AttributeValue) {
        boolean toReturn = false;
        if (index <= BPConstants.MAX_SOCKETS && hasEmptySocket()) {
            sockets.put(index, Map.entry(AttributeName, AttributeValue));
            toReturn = true;
        }
        return toReturn;
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

        for (int i = 1; i <= BPConstants.MAX_SOCKETS; i++) {
            if (sockets.get(i) != null) {
                nbt.putDouble(BPConstants.SOCKET_PRE_TAG + "." + i + "." + getSocketAttributeNameByIndex(i), getSocketAttributeValueByIndex(i));
            } else {
                nbt.putDouble(BPConstants.SOCKET_PRE_TAG + "." + i + "." + BPConstants.NO_RUNE_GEM, 0.0);
            }
        }


    }

    public void loadNBTData(CompoundTag nbt) {

        for (String s : nbt.getAllKeys()) {
            if (s.contains(BPConstants.SOCKET_PRE_TAG)) {
                String[] tokens = s.split("\\.");
                sockets.put(Integer.parseInt(tokens[1]), Map.entry(tokens[2], nbt.getDouble(s)));
            }
        }
    }


}
