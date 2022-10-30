package yerova.botanicpledge.client.render.entities;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;

public class YggdrafoliumRenderer extends EntityRenderer<YggdrafoliumEntity> {

    public YggdrafoliumRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(YggdrafoliumEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
