package yerova.botanicpledge.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import yerova.botanicpledge.common.config.BotanicPledgeCommonConfigs;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.HashMap;

public class BotanicPledgeConstants {

    public static final String TAG_STATS_SUBSTAT = BotanicPledge.MOD_ID + ".stats";

    public static final String SHIELD_TAG_NAME = "Shield";
    public static final String MAX_SHIELD_TAG_NAME = "MaxShield";
    public static final String CHARGE_TAG_NAME = "Charge";
    public static final String MAX_CHARGE_TAG_NAME = "MaxCharge";

    public static final String TAG_RANGED_MODE = "ranged_mode";


    public static final CompoundTag INIT_CORE_SHIELD_TAG (int maxCharge,int maxShield) {
        CompoundTag toReturn = ItemStack.EMPTY.getOrCreateTagElement(BotanicPledgeConstants.TAG_STATS_SUBSTAT);


        toReturn.putInt(BotanicPledgeConstants.MAX_CHARGE_TAG_NAME, maxCharge);
        toReturn.putInt(BotanicPledgeConstants.MAX_SHIELD_TAG_NAME, maxShield);
        toReturn.putInt(BotanicPledgeConstants.CHARGE_TAG_NAME, 0);
        toReturn.putInt(BotanicPledgeConstants.SHIELD_TAG_NAME, 0);


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
}
