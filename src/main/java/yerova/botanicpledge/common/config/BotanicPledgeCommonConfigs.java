package yerova.botanicpledge.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;

public class BotanicPledgeCommonConfigs {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.ConfigValue<Integer> ARMOR_MAX_VALUE;
    public static ForgeConfigSpec.ConfigValue<Integer> ARMOR_TOUGHNESS_MAX_VALUE;
    public static ForgeConfigSpec.ConfigValue<Integer> MAX_HEALTH_MAX_VALUE;
    public static ForgeConfigSpec.ConfigValue<Integer> ATTACK_DAMAGE_MAX_VALUE;
    public static ForgeConfigSpec.ConfigValue<Integer> KNOCKBACK_RESISTANCE_MAX_VALUE;
    public static ForgeConfigSpec.ConfigValue<Integer> MOVEMENT_SPEED_MAX_VALUE;
    public static ForgeConfigSpec.ConfigValue<Integer> ATTACK_SPEED_MAX_VALUE;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();

    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {

        builder.push("Configs for BotanicPledge");


        ARMOR_MAX_VALUE = builder.comment("Max Armor Increase Value for the Divine-Core Items!").define("maxArmor", 20);
        ARMOR_TOUGHNESS_MAX_VALUE = builder.comment("Max Armor Toughness Increase Value for the Divine-Core Items!").define("maxArmorToughness", 20);
        MAX_HEALTH_MAX_VALUE = builder.comment("Max Max Health Increase Value for the Divine-Core Items!").define("maxHealth", 40);
        ATTACK_DAMAGE_MAX_VALUE = builder.comment("Max Damage Increase Value for the Divine-Core Items!").define("maxAttackDamage", 10);
        KNOCKBACK_RESISTANCE_MAX_VALUE = builder.comment("Max Knockback Resistance Increase Value for the Divine-Core Items!").define("maxKnockbackResistance", 8);
        MOVEMENT_SPEED_MAX_VALUE = builder.comment("Max Movement Speed Increase Value for the Divine-Core Items!").define("maxMovementSpeed", 24);
        ATTACK_SPEED_MAX_VALUE = builder.comment("Max Attack Speed Increase Value for the Divine-Core Items!").define("maxAttackSpeed", 4);

        builder.pop();
    }

}
