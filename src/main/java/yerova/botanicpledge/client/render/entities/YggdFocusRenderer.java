package yerova.botanicpledge.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocusEntity;

public class YggdFocusRenderer extends EntityRenderer<YggdFocusEntity> {
    public YggdFocusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(YggdFocusEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        Minecraft mc = Minecraft.getInstance();
        pPoseStack.pushPose();

        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }


    @Override
    public ResourceLocation getTextureLocation(YggdFocusEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public boolean shouldRender(YggdFocusEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
