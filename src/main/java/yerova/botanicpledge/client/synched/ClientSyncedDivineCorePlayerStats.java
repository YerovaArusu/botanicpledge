package yerova.botanicpledge.client.synched;

public class ClientSyncedDivineCorePlayerStats {
    private static int maxShield;
    private static int shield;
    private static int maxCharge;
    private static int charge;
    private static double armorToughness;
    private static int armor;
    private static double maxHealth;
    private static double knockbackResistance;
    private static double attackDamage;


    public int getMaxShield() {
        return maxShield;
    }

    public int getShield() {
        return shield;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public int getCharge() {
        return charge;
    }

    public double getArmorToughness() {
        return armorToughness;
    }

    public int getArmor() {
        return armor;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getKnockbackResistance() {
        return knockbackResistance;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public static void set(int maxShield, int shield, int maxCharge, int charge, double armorToughness, int armor, double maxHealth, double knockbackResistance, double attackDamage) {
        ClientSyncedDivineCorePlayerStats.maxShield = maxShield;
        ClientSyncedDivineCorePlayerStats.shield = shield;
        ClientSyncedDivineCorePlayerStats.maxCharge = maxCharge;
        ClientSyncedDivineCorePlayerStats.charge = charge;

        ClientSyncedDivineCorePlayerStats.armorToughness = armorToughness;
        ClientSyncedDivineCorePlayerStats.armor = armor;
        ClientSyncedDivineCorePlayerStats.maxHealth = maxHealth;
        ClientSyncedDivineCorePlayerStats.knockbackResistance = knockbackResistance;
        ClientSyncedDivineCorePlayerStats.attackDamage = attackDamage;
    }

}
