package yerova.botanicpledge.client.render.items.ritual_pedestal_block_item;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.common.items.block_items.RitualPedestalBlockItem;
import yerova.botanicpledge.setup.BotanicPledge;

public class RitualPedestalBlockItemModel extends AnimatedGeoModel<RitualPedestalBlockItem> {
    @Override
    public ResourceLocation getModelLocation(RitualPedestalBlockItem object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/blocks/ritual_pedestal.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RitualPedestalBlockItem object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/blocks/ritual_pedestal.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RitualPedestalBlockItem animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/blocks/ritual_pedestal.animation.json");
    }
}
