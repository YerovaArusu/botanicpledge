package yerova.botanicpledge.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.system.CallbackI;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.ArrayList;

public final class BPConstants {

    public static final String STATS_TAG_NAME = BotanicPledge.MOD_ID + ".stats";

    public static final String SHIELD_TAG_NAME = "Shield";
    public static final String MAX_SHIELD_TAG_NAME = "MaxShield";
    public static final String CHARGE_TAG_NAME = "Charge";
    public static final String MAX_CHARGE_TAG_NAME = "MaxCharge";
    public static final String CORE_RANK_TAG_NAME = "CoreRank";
    public static final String MANA_COST_TAG_NAME = "ManaCost";
    public static final String DEF_REGEN_TAG_NAME = "DefRegen";

    public static final String TAG_RANGED_MODE = "ranged_mode";

    public static final int MANA_TO_SHIELD_CONVERSION_RATE = 40;
    public static final int MANA_TICK_COST_WHILE_FLIGHT_CONVERSION_RATE = 10;

    public static final int MAX_CORE_RANK = 4;
    public static final int MIN_CORE_RANK = 1;
    public static final int BASIC_MANA_COST = 100;
    public static final int CORE_MAX_LEVEL_INCREASE_PER_RANK = 10;
    public static final int CORE_RANK_REQUIRED_FOR_YGGD_RAMUS = 2;

    public static final DamageSource HEALTH_SET_DMG_SRC = new DamageSource("health_set");
    public static final String TAG_CORE_UUID = "coreUUID";

    public static final String ARMOR_TAG_NAME = "armor";
    public static final String ARMOR_TOUGHNESS_TAG_NAME = "armor_toughness";
    public static final String MAX_HEALTH_TAG_NAME = "max_health";
    public static final String ATTACK_DAMAGE_TAG_NAME = "attack_damage";
    public static final String KNOCKBACK_RESISTANCE_TAG_NAME = "knockback_resistance";
    public static final String MOVEMENT_SPEED_TAG_NAME = "movement_speed";
    public static final String ATTACK_SPEED_TAG_NAME = "attack_speed";
    public static final String MAY_FLY_TAG_NAME = "may_fly";
    public static final String JUMP_HEIGHT_TAG_NAME = "jump_height";

    public static final String MANA_EFFICIENCY_TAG_NAME = "mana_efficiency";

    public static final int MAX_SOCKETS = 4;
    public static final String SOCKET_PRE_TAG = "socket";

    public static final double ARMOR_LEVEL_UP_VALUE = 0.5;
    public static final double ARMOR_TOUGHNESS_LEVEL_UP_VALUE = 0.5;
    public static final double MAX_HEALTH_LEVEL_UP_VALUE = 0.5;
    public static final double ATTACK_DAMAGE_LEVEL_UP_VALUE = 0.4;
    public static final double KNOCKBACK_RESISTANCE_LEVEL_UP_VALUE = 0.2;
    public static final double MOVEMENT_SPEED_LEVEL_UP_VALUE = 0.05;
    public static final double ATTACK_SPEED_LEVEL_UP_VALUE = 0.1;
    public static final double MAY_FLY_LEVEL_UP_VALUE = 0;
    public static final double JUMP_HEIGHT_LEVEL_UP_VALUE = 0.2;

    public static final String PROJECTILE_COUNT_TRACKER_TAG_NAME = "projectile_count";

    public static final String DRACONIC_EVOLUTION_MODID = "draconicevolution";

    public static final String FLOWER_TAG_BURN_TIME = "burnTime";
    public static final String FLOWER_TAG_COOLDOWN = "cooldown";
    public static final String FLOWER_TAG_CD = "cd";

    public static final String RARITY_EPIC = "rarity_epic";
    public static final String RARITY_RARE = "rarity_rare";
    public static final String RARITY_UNCOMMON = "rarity_uncommon";
    public static final String RARITY_COMMON = "rarity_common";

    public static final String NO_RUNE_GEM = "no_rune_gem";


    public static CompoundTag INIT_CORE_TAG(ItemStack stack ,int maxCharge, int maxShield) {
        CompoundTag toReturn = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);

        toReturn.putInt(BPConstants.CORE_RANK_TAG_NAME, 0);
        toReturn.putInt(BPConstants.MAX_CHARGE_TAG_NAME, DivineCoreItem.getShieldValueAccordingToRank(stack,maxCharge));
        toReturn.putInt(BPConstants.MAX_SHIELD_TAG_NAME, DivineCoreItem.getShieldValueAccordingToRank(stack,maxShield));
        toReturn.putInt(BPConstants.CHARGE_TAG_NAME, 0);
        toReturn.putInt(BPConstants.SHIELD_TAG_NAME, 0);


        return toReturn;
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
        list.add(ARMOR_TAG_NAME);
        list.add(ARMOR_TOUGHNESS_TAG_NAME);
        list.add(MAX_HEALTH_TAG_NAME);
        list.add(ATTACK_DAMAGE_TAG_NAME);
        list.add(KNOCKBACK_RESISTANCE_TAG_NAME);
        list.add(MOVEMENT_SPEED_TAG_NAME);
        list.add(ATTACK_SPEED_TAG_NAME);

        return list;
    }
}
