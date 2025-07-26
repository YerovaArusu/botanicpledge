package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import yerova.botanicpledge.client.render.AuraRenderType;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.IAuraNode;

public class AuraNodeRenderer implements BlockEntityRenderer<BlockEntity> {
    private static final ResourceLocation NODE_TEXTURE = new ResourceLocation("botanicpledge", "textures/misc/nodes.png");
    private static final int TEXTURE_SIZE = 2048;
    private static final int FRAME_SIZE = 64;
    private static final int TOTAL_FRAMES = 32;

    public AuraNodeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (!(blockEntity instanceof IAuraNode node)) return;
        if (node.getImplementation() == null) return;

        AuraImplementation imp = node.getImplementation();
        AuraNodeType nodeType = imp.getType();
        if (nodeType == null) return;

        int color = imp.getBaseEssence().getColor();

        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderTexture(0, NODE_TEXTURE);

        long gameTime = Minecraft.getInstance().level.getGameTime();
        int frameDuration = 1;
        int currentFrame = (int) ((gameTime / frameDuration) % TOTAL_FRAMES);
        int nextFrame = (currentFrame + 1) % TOTAL_FRAMES;
        float blendFactor = (gameTime % frameDuration + partialTicks) / (float) frameDuration;

        float minV = (nodeType.ordinal() * FRAME_SIZE) / (float) TEXTURE_SIZE;
        float maxV = minV + (FRAME_SIZE / (float) TEXTURE_SIZE);

        float minUCurrent = (currentFrame * FRAME_SIZE) / (float) TEXTURE_SIZE;
        float maxUCurrent = minUCurrent + (FRAME_SIZE / (float) TEXTURE_SIZE);
        float minUNext = (nextFrame * FRAME_SIZE) / (float) TEXTURE_SIZE;
        float maxUNext = minUNext + (FRAME_SIZE / (float) TEXTURE_SIZE);

        poseStack.pushPose();

        if (isPartOf2x2AuraCluster(blockEntity)) {
            poseStack.translate(1.0, 0.5, 1.0); // Mitte des 2x2-Bereichs
        } else {
            poseStack.translate(0.5, 0.5, 0.5); // Mitte des Blocks
        }

        poseStack.scale(2F, 2F, 2F);
        poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());

        VertexConsumer vertexConsumer = bufferSource.getBuffer(AuraRenderType.getAuraRenderType());

        float halfSize = 0.4F;

        RenderSystem.setShaderColor(red, green, blue, 1.0F - blendFactor);
        renderQuad(poseStack, vertexConsumer, halfSize, minUCurrent, maxUCurrent, minV, maxV, combinedLight, combinedOverlay, red, green, blue);

        RenderSystem.setShaderColor(red, green, blue, blendFactor);
        renderQuad(poseStack, vertexConsumer, halfSize, minUNext, maxUNext, minV, maxV, combinedLight, combinedOverlay, red, green, blue);

        poseStack.popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderQuad(PoseStack poseStack, VertexConsumer vertexConsumer, float halfSize, float minU, float maxU, float minV, float maxV, int combinedLight, int combinedOverlay, float red, float green, float blue) {
        vertexConsumer.vertex(poseStack.last().pose(), -halfSize, -halfSize, 0.0F).color(red, green, blue, 1.0F).uv(minU, maxV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), halfSize, -halfSize, 0.0F).color(red, green, blue, 1.0F).uv(maxU, maxV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), halfSize, halfSize, 0.0F).color(red, green, blue, 1.0F).uv(maxU, minV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), -halfSize, halfSize, 0.0F).color(red, green, blue, 1.0F).uv(minU, minV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(0, 0, 1).endVertex();
    }

    private boolean isPartOf2x2AuraCluster(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level == null) return false;

        var pos = blockEntity.getBlockPos();
        BlockState base = level.getBlockState(pos);

        return level.getBlockState(pos.east()).getBlock() == base.getBlock()
                && level.getBlockState(pos.south()).getBlock() == base.getBlock()
                && level.getBlockState(pos.east().south()).getBlock() == base.getBlock();
    }
}
