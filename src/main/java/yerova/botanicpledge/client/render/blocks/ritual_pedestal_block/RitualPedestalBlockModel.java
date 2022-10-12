package yerova.botanicpledge.client.render.blocks.ritual_pedestal_block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class RitualPedestalBlockModel extends AnimatedGeoModel<RitualPedestalBlockEntity> {
    @Override
    public ResourceLocation getModelLocation(RitualPedestalBlockEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/blocks/ritual_pedestal.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RitualPedestalBlockEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/blocks/ritual_pedestal.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RitualPedestalBlockEntity animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/blocks/ritual_pedestal.animation.json");
    }
}
