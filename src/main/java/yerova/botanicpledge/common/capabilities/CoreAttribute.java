package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import yerova.botanicpledge.common.utils.BPConstants;

public class CoreAttribute extends Attribute {
    private int maxCharge;
    private int maxShield;
    private int currentCharge;
    private int currentShield;
    private int defRegenPerTick;
    private int manaCostPerTick;


    public CoreAttribute(int maxCharge, int maxShield, int defRegenPerTick, int manaCostPerTick, int maxRunes, Rune.EquipmentType requiredType) {
        super(maxRunes, requiredType);
        this.maxCharge = maxCharge;
        this.maxShield = maxShield;
        this.defRegenPerTick = defRegenPerTick;
        this.manaCostPerTick = manaCostPerTick;

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

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt(BPConstants.MAX_CHARGE_TAG_NAME, maxCharge);
        nbt.putInt(BPConstants.MAX_SHIELD_TAG_NAME, maxShield);
        nbt.putInt(BPConstants.CHARGE_TAG_NAME, currentCharge);
        nbt.putInt(BPConstants.SHIELD_TAG_NAME, currentShield);
        nbt.putInt(BPConstants.DEF_REGEN_TAG_NAME, defRegenPerTick);
        nbt.putInt(BPConstants.MANA_COST_TAG_NAME, manaCostPerTick);

        super.saveNBTData(nbt);

    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        maxCharge = nbt.getInt(BPConstants.MAX_CHARGE_TAG_NAME);
        maxShield = nbt.getInt(BPConstants.MAX_SHIELD_TAG_NAME);
        currentCharge = nbt.getInt(BPConstants.CHARGE_TAG_NAME);
        currentShield = nbt.getInt(BPConstants.SHIELD_TAG_NAME);
        defRegenPerTick = nbt.getInt(BPConstants.DEF_REGEN_TAG_NAME);
        manaCostPerTick = nbt.getInt(BPConstants.MANA_COST_TAG_NAME);

        super.loadNBTData(nbt);
    }


}
