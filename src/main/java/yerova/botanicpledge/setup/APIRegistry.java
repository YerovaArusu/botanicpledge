package yerova.botanicpledge.setup;

import yerova.botanicpledge.api.BotanicPledgeAPI;

public class APIRegistry {
    private APIRegistry() {
    }

    public static void setup() {
        BotanicPledgeAPI api = BotanicPledgeAPI.getInstance();

        api.getEnchantingRecipeTypes().add(BPRecipes.BOTANIC_RITUAL_TYPE);

    }
}
