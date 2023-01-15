package yerova.botanicpledge.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import yerova.botanicpledge.common.config.BotanicPledgeCommonConfigs;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.ArrayList;
import java.util.HashMap;

public class BPConstants {

    public static final String STATS_TAG_NAME = BotanicPledge.MOD_ID + ".stats";

    public static final String SHIELD_TAG_NAME = "Shield";
    public static final String MAX_SHIELD_TAG_NAME = "MaxShield";
    public static final String CHARGE_TAG_NAME = "Charge";
    public static final String MAX_CHARGE_TAG_NAME = "MaxCharge";
    public static final String CORE_RANK_TAG_NAME = "CoreRank";
    public static final String MANA_COST_TAG_NAME = "ManaCost";

    public static final String TAG_RANGED_MODE = "ranged_mode";

    public static final int MAX_CORE_RANK = 5;
    public static final int MIN_CORE_RANK = 1;
    public static final int BASIC_MANA_COST = 100;
    public static final int CORE_MAX_LEVEL_INCREASE_PER_RANK = 10;

    public static final DamageSource HEALTH_SET_DMG_SRC = new DamageSource("health_set");
    public static final String TAG_CORE_UUID = "coreUUID";

    public static final CompoundTag INIT_CORE_TAG(int maxCharge, int maxShield) {
        CompoundTag toReturn = ItemStack.EMPTY.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);

        toReturn.putInt(BPConstants.CORE_RANK_TAG_NAME, 0);
        toReturn.putInt(BPConstants.MAX_CHARGE_TAG_NAME, maxCharge);
        toReturn.putInt(BPConstants.MAX_SHIELD_TAG_NAME, maxShield);
        toReturn.putInt(BPConstants.CHARGE_TAG_NAME, 0);
        toReturn.putInt(BPConstants.SHIELD_TAG_NAME, 0);


        return toReturn;
    }



    public static final HashMap<String, Integer> ATTRIBUTED_STATS(){
        HashMap<String, Integer> hashMap = new HashMap();

        hashMap.put("armor", BotanicPledgeCommonConfigs.ARMOR_MAX_VALUE.get());
        hashMap.put("armor_toughness", BotanicPledgeCommonConfigs.ARMOR_TOUGHNESS_MAX_VALUE.get());
        hashMap.put("max_health", BotanicPledgeCommonConfigs.MAX_HEALTH_MAX_VALUE.get());
        hashMap.put("attack_damage", BotanicPledgeCommonConfigs.ATTACK_DAMAGE_MAX_VALUE.get());
        hashMap.put("knockback_resistance", BotanicPledgeCommonConfigs.KNOCKBACK_RESISTANCE_MAX_VALUE.get());
        hashMap.put("movement_speed", BotanicPledgeCommonConfigs.MOVEMENT_SPEED_MAX_VALUE.get());
        hashMap.put("attack_speed", BotanicPledgeCommonConfigs.ATTACK_SPEED_MAX_VALUE.get());

        return hashMap;
    }

    public final static ArrayList<Attribute> ATTRIBUTE_LIST() {
        ArrayList<Attribute> list = new ArrayList<Attribute>();
        list.add(Attributes.ARMOR);
        list.add(Attributes.ARMOR_TOUGHNESS);
        list.add(Attributes.MAX_HEALTH);
        list.add(Attributes.ATTACK_DAMAGE);
        list.add(Attributes.KNOCKBACK_RESISTANCE);
        list.add(Attributes.MOVEMENT_SPEED);
        list.add(Attributes.ATTACK_SPEED);


        return list;
    }

    public static ArrayList<String> attributeNames() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("armor");
        list.add("armor_toughness");
        list.add("max_health");
        list.add("attack_damage");
        list.add("knockback_resistance");
        list.add("movement_speed");
        list.add("attack_speed");

        return list;
    }
}
