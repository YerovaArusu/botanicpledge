package yerova.botanicpledge.client.render.items.mana_yggdral_buffer_block_item;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.items.block_items.ManaYggdralBufferBlockItem;

public class ManaYggdralBufferBlockItemModel extends AnimatedGeoModel<ManaYggdralBufferBlockItem> {
    @Override
    public ResourceLocation getModelLocation(ManaYggdralBufferBlockItem object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/blocks/mana_yggdral_buffer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ManaYggdralBufferBlockItem object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/blocks/mana_yggdral_buffer.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ManaYggdralBufferBlockItem animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/blocks/mana_yggdral_buffer.animation.json");
    }
}
