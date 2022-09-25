package yerova.botanicpledge.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;

public class BotanicPledgeCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> ARMOR_MAX_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARMOR_TOUGHNESS_MAX_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_HEALTH_MAX_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ATTACK_DAMAGE_MAX_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Integer> KNOCKBACK_RESISTANCE_MAX_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MOVEMENT_SPEED_MAX_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ATTACK_SPEED_MAX_VALUE;

    public static HashMap<String, Integer> DivineCoreMaxValuesFromConfig() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("armor", ARMOR_MAX_VALUE.get());
        map.put("armor_toughness", ARMOR_TOUGHNESS_MAX_VALUE.get());
        map.put("max_health", MAX_HEALTH_MAX_VALUE.get());
        map.put("attack_damage", ATTACK_DAMAGE_MAX_VALUE.get());
        map.put("knockback_resistance", KNOCKBACK_RESISTANCE_MAX_VALUE.get());
        map.put("movement_speed", MOVEMENT_SPEED_MAX_VALUE.get());
        map.put("attack_speed", ATTACK_SPEED_MAX_VALUE.get());
        return map;
    }

    static {
        BUILDER.push("Configs for BotanicPledge");

        //HERE DEFINE YOUR CONFIGS
        ARMOR_MAX_VALUE = BUILDER.comment("Max Armor Increase Value for the Divine-Core Items!").define("maxArmor", 20);
        ARMOR_TOUGHNESS_MAX_VALUE = BUILDER.comment("Max Armor Toughness Increase Value for the Divine-Core Items!").define("maxArmorToughness", 20);
        MAX_HEALTH_MAX_VALUE = BUILDER.comment("Max Max Health Increase Value for the Divine-Core Items!").define("maxHealth", 40);
        ATTACK_DAMAGE_MAX_VALUE = BUILDER.comment("Max Damage Increase Value for the Divine-Core Items!").define("maxAttackDamage", 10);
        KNOCKBACK_RESISTANCE_MAX_VALUE = BUILDER.comment("Max Knockback Resistance Increase Value for the Divine-Core Items!").define("maxKnockbackResistance", 8);
        MOVEMENT_SPEED_MAX_VALUE = BUILDER.comment("Max Movement Speed Increase Value for the Divine-Core Items!").define("maxMovementSpeed", 24);
        ATTACK_SPEED_MAX_VALUE = BUILDER.comment("Max Attack Speed Increase Value for the Divine-Core Items!").define("maxAttackSpeed", 4);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
