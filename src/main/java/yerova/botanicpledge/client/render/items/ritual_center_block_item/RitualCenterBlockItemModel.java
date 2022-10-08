package yerova.botanicpledge.client.render.items.ritual_center_block_item;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.items.block_items.RitualCenterBlockItem;

public class RitualCenterBlockItemModel extends AnimatedGeoModel<RitualCenterBlockItem> {
    @Override
    public ResourceLocation getModelLocation(RitualCenterBlockItem object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/blocks/ritual_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RitualCenterBlockItem object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/blocks/ritual_center.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RitualCenterBlockItem animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/blocks/ritual_center.animation.json");
    }
}
