package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import yerova.botanicpledge.common.aura_node.essence.EssenceCapacitorImplementation;
import yerova.botanicpledge.common.blocks.block_entities.EssenceJarBlockEntity;
import yerova.botanicpledge.setup.BotanicPledge;

import java.awt.*;

public class EssenceJarRenderer implements BlockEntityRenderer<EssenceJarBlockEntity> {

    public static final ResourceLocation TEXTURE =
            new ResourceLocation(BotanicPledge.MOD_ID, "textures/block/essence_fluid.png");

    public EssenceJarRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(EssenceJarBlockEntity jar, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        if (jar.getImplementation() == null) return;

        EssenceCapacitorImplementation imp= jar.getImplementation();

        if (imp.getFirstEssence() == null) return;

        // === Fluid Dimensions (in Blocks) ===
        float width = 8f / 16f;
        float height = 10f / 16f;
        float depth = 8f / 16f;

        float xOffset = (1.01f - width) / 2f;
        float yOffset = 2.01f / 16f; // 2 Pixel hoch
        float zOffset = (1.01f - depth) / 2f;

        // === Color aus HEX ===
        int colorHex = imp.getFirstEssence().getColor(); // z.B. 0xRRGGBB
        Color color = new Color(colorHex);
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = 0.5f + (((float) imp.getEssenceCount() /imp.getMaxCapacity())/2);


        int frameCount = 32; // 512px hoch / 16px pro Frame = 32 Frames
        int ticks = (int) (Minecraft.getInstance().level.getGameTime() % frameCount);

        float uMin = 0f;
        float uMax = 1f;

        float frameHeight = 1f / frameCount;
        float vMin = ticks * frameHeight;
        float vMax = vMin + frameHeight;
        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, zOffset);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        // Vorderseite
        vertexConsumer.vertex(poseStack.last().pose(), 0, 0, depth).color(r, g, b, a).uv(uMin, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), width, 0, depth).color(r, g, b, a).uv(uMax, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), width, height, depth).color(r, g, b, a).uv(uMax, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, height, depth).color(r, g, b, a).uv(uMin, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, 1).endVertex();

        // Rückseite

        vertexConsumer.vertex(poseStack.last().pose(), width, 0, 0).color(r, g, b, a).uv(uMin, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, -1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, 0, 0).color(r, g, b, a).uv(uMax, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, -1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, height, 0).color(r, g, b, a).uv(uMax, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, -1).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), width, height, 0).color(r, g, b, a).uv(uMin, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(0, 0, -1).endVertex();

        // Links
        vertexConsumer.vertex(poseStack.last().pose(), 0, 0, 0).color(r, g, b, a).uv(uMin, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(-1, 0, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, 0, depth).color(r, g, b, a).uv(uMax, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(-1, 0, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, height, depth).color(r, g, b, a).uv(uMax, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(-1, 0, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, height, 0).color(r, g, b, a).uv(uMin, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(-1, 0, 0).endVertex();

        // Rechts
        vertexConsumer.vertex(poseStack.last().pose(), width, 0, depth).color(r, g, b, a).uv(uMin, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(1, 0, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), width, 0, 0).color(r, g, b, a).uv(uMax, vMax).overlayCoords(packedOverlay).uv2(packedLight).normal(1, 0, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), width, height, 0).color(r, g, b, a).uv(uMax, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(1, 0, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), width, height, depth).color(r, g, b, a).uv(uMin, vMin).overlayCoords(packedOverlay).uv2(packedLight).normal(1, 0, 0).endVertex();

        poseStack.popPose();
    }
}
