package yerova.botanicpledge.client.render.blocks.mana_ygdral_buffer_block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import yerova.botanicpledge.common.blocks.block_entities.ManaYggdralBufferBlockEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class ManaYggdralBufferBlockModel extends AnimatedGeoModel<ManaYggdralBufferBlockEntity> {
    @Override
    public ResourceLocation getModelLocation(ManaYggdralBufferBlockEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "geo/blocks/mana_yggdral_buffer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ManaYggdralBufferBlockEntity object) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/blocks/mana_yggdral_buffer.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ManaYggdralBufferBlockEntity animatable) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "animations/blocks/mana_yggdral_buffer.animation.json");
    }
}
