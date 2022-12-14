package yerova.botanicpledge.client.render.entities;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class MarinaModel extends AnimatedGeoModel<MarinaEntity> {
    @Override
    public ResourceLocation getModelResource(MarinaEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/marina_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarinaEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/entity/marina/marina.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarinaEntity animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/marina_animations.geo.json");
    }
}
