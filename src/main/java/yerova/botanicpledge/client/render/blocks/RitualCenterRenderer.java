package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.mixin.ItemEntityAccessor;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.common.blocks.block_entities.RitualBaseBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;

import static yerova.botanicpledge.client.render.blocks.RitualPedestalRenderer.renderItem;

public class RitualCenterRenderer implements BlockEntityRenderer<RitualCenterBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public RitualCenterRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(RitualCenterBlockEntity tileEntityIn, float pPartialTick, PoseStack matrixStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        renderItem(tileEntityIn, pPartialTick, matrixStack, pBufferSource, pPackedLight, pPackedOverlay);
        renderRitualTop(blockRenderDispatcher, ModelBakery.ritualCenterTop ,tileEntityIn, pPartialTick, matrixStack, pBufferSource, pPackedLight, pPackedOverlay);
    }
    public static void renderRitualTop(BlockRenderDispatcher blockRenderDispatcher, BakedModel model, RitualBaseBlockEntity pedestal, float partialTicks , PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        float progress = pedestal.getAnimationProgress(partialTicks);
        float yOffset = 0.3125f + progress*0.6f;

        poseStack.translate(0, yOffset, 0);

        blockRenderDispatcher.getModelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                null,
                model,
                1f, 1f, 1f,
                packedLight,
                packedOverlay
        );
        poseStack.popPose();
    }
}
