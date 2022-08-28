package yerova.botanicpledge.client.synched;

public class ClientSyncedProtector {
    private static int charge, maxCharge, defense, maxDefense;

    public static void set(int mana, int maxMana, int defense, int maxDefense) {
        ClientSyncedProtector.charge = mana;
        ClientSyncedProtector.maxCharge = maxMana;
        ClientSyncedProtector.defense = defense;
        ClientSyncedProtector.maxDefense = maxDefense;
    }

    public static int getCharge() {
        return charge;
    }

    public static int getMaxCharge() {
        return maxCharge;
    }

    public static int getDefense() {
        return defense;
    }

    public static int getMaxDefense() {
        return maxDefense;
    }

}
