package yerova.botanicpledge.client.utils;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import yerova.botanicpledge.setup.BotanicPledge;

public class ModelUtils {

    public static ModelLayerLocation make(String name) {
        return make(name, "main");
    }

    public static ModelLayerLocation make(String name, String layer) {
        return new ModelLayerLocation(new ResourceLocation(BotanicPledge.MOD_ID, name), layer);
    }
}
