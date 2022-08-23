package yerova.botanicpledge.client.render.entities;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.entitites.projectiles.ManaSlashEntity;

public class ManaSlashModel extends AnimatedGeoModel<ManaSlashEntity> {
    @Override
    public ResourceLocation getModelLocation(ManaSlashEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/mana_slash_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ManaSlashEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/entity/mana_slash/mana_slash.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ManaSlashEntity animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/mana_slash.animation.json");
    }
}
