package yerova.botanicpledge.client.render.items;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.common.items.YggdralScepter;
import yerova.botanicpledge.setup.BotanicPledge;

public class YggdralScepterModel extends AnimatedGeoModel<YggdralScepter> {
    @Override
    public ResourceLocation getModelLocation(YggdralScepter object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/yggdral_scepter.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(YggdralScepter object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/items/yggdral_scepter.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(YggdralScepter animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/yggdral_scepter_animations.json");
    }
}
