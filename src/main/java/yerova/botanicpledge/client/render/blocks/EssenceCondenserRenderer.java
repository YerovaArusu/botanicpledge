package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.common.blocks.block_entities.essence.EssenceCondenserBlockEntity;

public class EssenceCondenserRenderer implements BlockEntityRenderer<EssenceCondenserBlockEntity> {

    private final BlockRenderDispatcher blockRenderDispatcher;

    public EssenceCondenserRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(EssenceCondenserBlockEntity entity, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {

        // Safety: Modell prüfen
        BakedModel connectorModel = ModelBakery.condenserConnector;
        if (connectorModel == null) return;

        poseStack.pushPose();

        // Für jede Richtung prüfen, ob verbunden -> Connector rendern
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            boolean connected = entity.connections.getOrDefault(dir, false);
            if (!connected) continue;

            poseStack.pushPose();

            // Pivot in Blockmitte setzen (dreht sauber innerhalb des Blocks)
            poseStack.translate(0.5, 0.5, 0.5);

            // Y-Rotation abhängig von der Richtung
            float yRot;
            switch (dir) {
                case NORTH -> yRot = 0f;    // Modell ist für NORTH ausgelegt
                case EAST  -> yRot = 270f;
                case SOUTH -> yRot = 180f;
                case WEST  -> yRot = 90f;
                default    -> yRot = 0f;
            }
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

            // Zurück in Block-Koordinaten
            poseStack.translate(-0.5, -0.5, -0.5);

            // Model rendern (wie in deinen anderen Renderern)
            blockRenderDispatcher.getModelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(Sheets.cutoutBlockSheet()),
                    null,
                    connectorModel,
                    1f, 1f, 1f,
                    packedLight,
                    packedOverlay
            );

            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
