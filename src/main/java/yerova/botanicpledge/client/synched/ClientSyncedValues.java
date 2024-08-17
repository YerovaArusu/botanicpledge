package yerova.botanicpledge.client.synched;

public class ClientSyncedValues {
    private static int  defense, maxDefense, yggdrasilPower;

    public static void set(int defense, int maxDefense, int yggdrasilPower) {
        ClientSyncedValues.defense = defense;
        ClientSyncedValues.maxDefense = maxDefense;
        ClientSyncedValues.yggdrasilPower = yggdrasilPower;
    }

    public static int getDefense() {
        return defense;
    }

    public static int getMaxDefense() {
        return maxDefense;
    }

    public static int getYggdrasilPower() {
        return yggdrasilPower;
    }
}
