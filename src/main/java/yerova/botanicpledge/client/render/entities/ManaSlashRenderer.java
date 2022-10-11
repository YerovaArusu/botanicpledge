package yerova.botanicpledge.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import yerova.botanicpledge.setup.BotanicPledge;
import yerova.botanicpledge.common.entitites.projectiles.ManaSlashEntity;

public class ManaSlashRenderer extends GeoProjectilesRenderer<ManaSlashEntity> {

    public ManaSlashRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ManaSlashModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ManaSlashEntity instance) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/entity/mana_slash/mana_slash.png");
    }

    @Override
    public RenderType getRenderType(ManaSlashEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(8F, 4F, 8F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
