package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public final class DivineCorePlayerStats {
    private int maxShield;
    private int shield;
    private int maxCharge;
    private int charge;
    private double armorToughness;
    private int armor;
    private double maxHealth;
    private double knockbackResistance;
    private double attackDamage;

    public DivineCorePlayerStats(int maxShield, int shield, int maxCharge, int charge, double armorToughness, int armor, double maxHealth, double knockbackResistance, double attackDamage) {
        this.maxShield = maxShield;
        this.shield = shield;
        this.maxCharge = maxCharge;
        this.charge = charge;
        this.armorToughness = armorToughness;
        this.armor = armor;
        this.maxHealth = maxHealth;
        this.knockbackResistance = knockbackResistance;
        this.attackDamage = attackDamage;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DivineCorePlayerStats) obj;
        return this.maxShield == that.maxShield &&
                this.shield == that.shield &&
                this.maxCharge == that.maxCharge &&
                this.charge == that.charge &&
                Double.doubleToLongBits(this.armorToughness) == Double.doubleToLongBits(that.armorToughness) &&
                this.armor == that.armor &&
                Double.doubleToLongBits(this.maxHealth) == Double.doubleToLongBits(that.maxHealth) &&
                this.knockbackResistance == that.knockbackResistance &&
                Double.doubleToLongBits(this.attackDamage) == Double.doubleToLongBits(that.attackDamage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxShield, shield, maxCharge, charge, armorToughness, armor, maxHealth, knockbackResistance, attackDamage);
    }

    @Override
    public String toString() {
        return "DivineCorePlayerStats[" +
                "maxShield=" + maxShield + ", " +
                "shield=" + shield + ", " +
                "maxCharge=" + maxCharge + ", " +
                "charge=" + charge + ", " +
                "armorToughness=" + armorToughness + ", " +
                "armor=" + armor + ", " +
                "maxHealth=" + maxHealth + ", " +
                "knockbackResistance=" + knockbackResistance + ", " +
                "attackDamage=" + attackDamage + ']';
    }


    public void saveNBTData(CompoundTag compound) {
        compound.putInt("maxShield", maxShield);
        compound.putInt("shield", shield);
        compound.putInt("maxCharge", maxCharge);
        compound.putInt("charge", charge);

        compound.putDouble("armorToughness", armorToughness);
        compound.putInt("armor", armor);
        compound.putDouble("maxHealth", maxHealth);
        compound.putDouble("knockbackResistance", knockbackResistance);
        compound.putDouble("attackDamage", attackDamage);
    }

    public void loadNBTData(CompoundTag compound) {
        maxShield = compound.getInt("maxShield");
        shield = compound.getInt("shield");
        maxCharge = compound.getInt("maxCharge");
        charge = compound.getInt("charge");

        armorToughness = compound.getDouble("armorToughness");
        armor = compound.getInt("armor");
        maxHealth = compound.getDouble("maxHealth");
        knockbackResistance = compound.getDouble("knockbackResistance");
        attackDamage = compound.getDouble( "attackDamage");
    }

    public void copyFrom(DivineCorePlayerStats oldStore) {
        maxShield = oldStore.maxShield;
        shield = oldStore.shield;
        maxCharge = oldStore.maxCharge;
        charge = oldStore.charge;

        armorToughness = oldStore.armorToughness;
        armor = oldStore.armor;
        maxHealth = oldStore.maxHealth;
        knockbackResistance = oldStore.knockbackResistance;
        attackDamage = oldStore.attackDamage;
    }

    public void set(int maxShield, int shield, int maxCharge, int charge, double armorToughness, int armor,double maxHealth, double knockbackResistance, double attackDamage) {
        this.maxShield = maxShield;
        this.shield = shield;
        this.maxCharge = maxCharge;
        this.charge = charge;
        this.armorToughness = armorToughness;
        this.armor = armor;
        this.maxHealth = maxHealth;
        this.knockbackResistance = knockbackResistance;
        this.attackDamage = attackDamage;
    }
}
