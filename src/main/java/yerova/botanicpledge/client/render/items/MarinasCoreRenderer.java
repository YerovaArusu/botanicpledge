package yerova.botanicpledge.client.render.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import vazkii.botania.common.helper.VecHelper;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.common.items.relic.MarinasCore;

public class MarinasCoreRenderer implements ICurioRenderer {


    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        M contextModel = renderLayerParent.getModel();
        if (!(stack.getItem() instanceof MarinasCore)) {
            return;
        }

        if (!(contextModel instanceof HumanoidModel<?>)) {
            return;
        }


        BakedModel model = ModelBakery.marinaWings;
        boolean flying = slotContext.entity() instanceof Player player && player.getAbilities().flying;
        float flap = 20F + (float) ((Math.sin((double) (slotContext.entity().tickCount + partialTicks) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));

        matrixStack.pushPose();
        ((HumanoidModel<?>) contextModel).body.translateAndRotate(matrixStack);
        matrixStack.translate(0, -1, 0.2);

        for (int j = 0; j < 2; j++) {
            matrixStack.mulPose(VecHelper.rotateX(j == 0 ? 0 : 180));

            for (int i = 0; i < 2; i++) {
                matrixStack.pushPose();

                if (j == 0) {
                    matrixStack.mulPose(VecHelper.rotateY(i == 0 ? flap : 180 - flap));
                } else matrixStack.mulPose(VecHelper.rotateY(i == 0 ? flap - 180 : -flap));


                matrixStack.translate(-0.5, j == 0 ? 0.7 : -1.7, 0);

                matrixStack.scale(1F, -1F, -1F);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, matrixStack, renderTypeBuffer, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
                matrixStack.popPose();
            }
        }

        matrixStack.popPose();

    }
}
