package yerova.botanicpledge.client.render.entities;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;

public class MarinaModel extends AnimatedGeoModel<MarinaEntity> {
    @Override
    public ResourceLocation getModelLocation(MarinaEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/marina_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MarinaEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/entity/marina/marina.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MarinaEntity animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/marina_animations.geo.json");
    }
}
