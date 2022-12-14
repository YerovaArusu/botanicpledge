package yerova.botanicpledge.client.render.blocks.ritual_center_block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class RitualCenterBlockModel extends AnimatedGeoModel<RitualCenterBlockEntity> {
    @Override
    public ResourceLocation getModelResource(RitualCenterBlockEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/blocks/ritual_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RitualCenterBlockEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/blocks/ritual_center.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RitualCenterBlockEntity animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/blocks/ritual_center.animation.json");
    }
}
