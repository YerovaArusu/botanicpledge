package yerova.botanicpledge.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BotanicPledgeClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    static {
        BUILDER.push("Client Configs for BotanicPledge");

        //HERE DEFINE YOUR CONFIGS

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
