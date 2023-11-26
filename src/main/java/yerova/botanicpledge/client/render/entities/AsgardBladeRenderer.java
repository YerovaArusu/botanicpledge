package yerova.botanicpledge.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.helper.VecHelper;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.common.entitites.projectiles.AsgardBladeEntity;
import yerova.botanicpledge.setup.BPItems;

public class AsgardBladeRenderer extends EntityRenderer<AsgardBladeEntity> {
    public AsgardBladeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(AsgardBladeEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(AsgardBladeEntity weapon, float pEntityYaw, float pPartialTick, PoseStack matrixStackIn, MultiBufferSource pBuffer, int pPackedLight) {

        Minecraft mc = Minecraft.getInstance();
        if (weapon.getDelay() > 0)
            return;

        matrixStackIn.pushPose();

        matrixStackIn.pushPose();
        float s = 0.8F;
        matrixStackIn.scale(s, s, s);
        matrixStackIn.mulPose(new Quaternionf(VecHelper.rotateY(weapon.getRotation() + 90F)));
        matrixStackIn.mulPose(VecHelper.rotateZ(weapon.getPitch()));
        matrixStackIn.mulPose(VecHelper.rotateZ(-45));


        float alpha = weapon.getFake() ? Math.max(0F, 0.75F - weapon.tickCount * (0.75F / AsgardBladeEntity.LIVE_TICKS) * 1.5F) : 1F;
        BakedModel model = ModelBakery.asgardBlade;
        int color = 0xFFFFFF | ((int) (alpha * 255F)) << 24;
        RenderHelper.renderItemCustomColor(mc.player, new ItemStack(BPItems.ASGARD_FRACTAL.get()), color, matrixStackIn, pBuffer, 0xF000F0, OverlayTexture.NO_OVERLAY, model);

        matrixStackIn.scale(1 / s, 1 / s, 1 / s);
        matrixStackIn.popPose();

        matrixStackIn.popPose();
        super.render(weapon, pEntityYaw, pPartialTick, matrixStackIn, pBuffer, pPackedLight);

    }

    @Override
    public boolean shouldRender(AsgardBladeEntity asgardBlade, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
