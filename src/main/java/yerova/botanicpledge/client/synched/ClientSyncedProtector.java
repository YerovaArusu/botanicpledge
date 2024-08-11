package yerova.botanicpledge.client.synched;

public class ClientSyncedProtector {
    private static int  defense, maxDefense;

    public static void set(int defense, int maxDefense) {
        ClientSyncedProtector.defense = defense;
        ClientSyncedProtector.maxDefense = maxDefense;
    }

    public static int getDefense() {
        return defense;
    }

    public static int getMaxDefense() {
        return maxDefense;
    }

}
