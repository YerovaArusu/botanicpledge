package yerova.botanicpledge.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class MarinaRenderer extends GeoEntityRenderer<MarinaEntity> {
    public MarinaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MarinaModel());

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(MarinaEntity instance) {
        return new ResourceLocation(BotanicPledge.MOD_ID, "textures/entity/marina/marina.png");
    }

    @Override
    public RenderType getRenderType(MarinaEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1F, 1F, 1F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
