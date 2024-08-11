package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import yerova.botanicpledge.common.utils.BPConstants;

public class CoreAttribute extends Attribute {
    private int maxShield;
    private int currentShield;
    private int defRegenPerTick;
    private int manaCostPerTick;
    private long lastTimeHit;


    public CoreAttribute(int maxShield, int defRegenPerTick, int manaCostPerTick, int maxRunes, Rune.EquipmentType requiredType) {
        super(maxRunes, requiredType);
        this.maxShield = maxShield;
        this.defRegenPerTick = defRegenPerTick;
        this.manaCostPerTick = manaCostPerTick;
        this.lastTimeHit = 0;
    }

    public long getLastTimeHit() {
        return lastTimeHit;
    }

    public void setLastTimeHit(long lastTimeHit) {
        this.lastTimeHit = lastTimeHit;
    }

    public void incrementLastTimeHit(){
        this.lastTimeHit++;
    }


    public int getMaxShield() {
        return maxShield;
    }

    public void setMaxShield(int maxShield) {
        this.maxShield = maxShield;
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

    public void addCurrentShield(int shield) {
        this.currentShield = Math.min(this.currentShield + shield, maxShield);
    }
    public void setCurrentShield(int shield) {
        this.currentShield = shield;
    }


    public void removeCurrentShield(int shield) {
        this.currentShield = Math.max(this.currentShield - shield, 0);
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt(BPConstants.MAX_SHIELD_TAG_NAME, maxShield);
        nbt.putInt(BPConstants.SHIELD_TAG_NAME, currentShield);
        nbt.putInt(BPConstants.DEF_REGEN_TAG_NAME, defRegenPerTick);
        nbt.putInt(BPConstants.MANA_COST_TAG_NAME, manaCostPerTick);
        nbt.putLong("lastTimeHit", lastTimeHit);
        super.saveNBTData(nbt);

    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        maxShield = nbt.getInt(BPConstants.MAX_SHIELD_TAG_NAME);
        currentShield = nbt.getInt(BPConstants.SHIELD_TAG_NAME);
        defRegenPerTick = nbt.getInt(BPConstants.DEF_REGEN_TAG_NAME);
        manaCostPerTick = nbt.getInt(BPConstants.MANA_COST_TAG_NAME);
        lastTimeHit = nbt.getLong("lastTimeHit");
        super.loadNBTData(nbt);
    }


}
