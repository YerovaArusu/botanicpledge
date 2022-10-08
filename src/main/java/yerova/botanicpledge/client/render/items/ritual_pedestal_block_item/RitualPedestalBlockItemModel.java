package yerova.botanicpledge.client.render.items.ritual_pedestal_block_item;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.common.items.block_items.RitualPedestalBlockItem;

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
