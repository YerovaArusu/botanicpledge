package yerova.botanicpledge.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.Map;
import java.util.function.Consumer;

public class ModelBakery {

    public static BakedModel asgardBlade;
    public static BakedModel mariaWings;

    public static void onModelRegister(ResourceManager rm, Consumer<ResourceLocation> consumer) {
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "icon/asgard_blade"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "icon/maria_wings"));

    }

    public static void onModelBake(net.minecraft.client.resources.model.ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
        asgardBlade = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "icon/asgard_blade"));
        mariaWings = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "icon/maria_wings"));
    }

}
