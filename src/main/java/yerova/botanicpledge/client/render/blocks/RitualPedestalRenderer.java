package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.mixin.ItemEntityAccessor;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.common.blocks.block_entities.RitualBaseBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import com.mojang.math.SymmetricGroup3;


public class RitualPedestalRenderer implements BlockEntityRenderer<RitualPedestalBlockEntity> {

    private final BlockRenderDispatcher blockRenderDispatcher;

    public RitualPedestalRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(RitualPedestalBlockEntity pedestal, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {

        renderItem(pedestal, partialTicks, poseStack, buffer, packedLight, packedOverlay);
        renderRitualTop(blockRenderDispatcher, ModelBakery.ritualPedestalTop ,pedestal, partialTicks, poseStack, buffer, packedLight, packedOverlay);

    }



    public static void renderItem(RitualBaseBlockEntity tileEntityIn, float pPartialTick, PoseStack matrixStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {


        double x = tileEntityIn.getBlockPos().getX();
        double y = tileEntityIn.getBlockPos().getY();
        double z = tileEntityIn.getBlockPos().getZ();

        if (tileEntityIn.getHeldStack() == null)
            return;

        if (tileEntityIn.entity == null || !ItemStack.matches(tileEntityIn.entity.getItem(), tileEntityIn.getHeldStack())) {
            tileEntityIn.entity = new ItemEntity(tileEntityIn.getLevel(), x, y, z, tileEntityIn.getHeldStack());
        }
        ItemEntity entityItem = tileEntityIn.entity;
        matrixStack.pushPose();

        ((ItemEntityAccessor) tileEntityIn.entity).setAge(ClientTickHandler.ticksInGame);
        entityItem.setItem(tileEntityIn.getHeldStack());

        Minecraft.getInstance().getEntityRenderDispatcher().render(entityItem, 0.5, 1, 0.5, pPartialTick, 2.0f, matrixStack, pBufferSource, pPackedLight);

        matrixStack.popPose();
    }

    public static void renderRitualTop(BlockRenderDispatcher blockRenderDispatcher, BakedModel model,
                                       RitualPedestalBlockEntity pedestal, float partialTicks,
                                       PoseStack poseStack, MultiBufferSource buffer,
                                       int packedLight, int packedOverlay) {
        poseStack.pushPose();

        float progress = pedestal.getAnimationProgress(partialTicks);
        float yOffset = 0.3125f + progress * 0.6f;

        // Höhe verschieben
        poseStack.translate(0, yOffset, 0);

        // prüfen ob Center craftet -> nur dann soll rotiert/gekipt werden
        boolean rotate = false;
        BlockPos centerPos = pedestal.ritualCenterPos;
        RitualCenterBlockEntity centerEntity = null;
        if (!centerPos.equals(BlockPos.ZERO) && pedestal.getLevel() != null) {
            if (pedestal.getLevel().getBlockEntity(centerPos) instanceof RitualCenterBlockEntity c) {
                centerEntity = c;
                rotate = c.isCrafting;
            }
        }


        if (rotate && progress > 0f && centerPos != null) {
            // Richtung zum Center in Block-Koordinaten
            double dx = (centerPos.getX() + 0.5) - (pedestal.getBlockPos().getX() + 0.5);
            double dy = (centerPos.getY() + 0.5) - (pedestal.getBlockPos().getY() + 0.5);
            double dz = (centerPos.getZ() + 0.5) - (pedestal.getBlockPos().getZ() + 0.5);

            // Länge horizontal und insgesamt
            double lenHoriz = Math.sqrt(dx*dx + dz*dz);
            double lenTotal = Math.sqrt(dx*dx + dy*dy + dz*dz);

            if (lenTotal > 1e-6) {
                // Yaw: drehen um Y, damit +Z des Modells horizontal Richtung Center zeigt
                float yawDeg = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90f;

                // Pitch: Kippen um X, damit +Y des Modells Richtung Center zeigt
                // atan2(horizontalDistance, dy)
                float pitchDeg = (float)Math.toDegrees(Math.atan2(lenHoriz, dy)) - 90f;

                // Transformation um Blockmitte
                poseStack.translate(0.5, 0.5, 0.5); // +0.5 in Y, damit Y-Achse um Blockmitte zeigt
                poseStack.mulPose(Axis.YP.rotationDegrees(yawDeg));
                poseStack.mulPose(Axis.XP.rotationDegrees(pitchDeg));
                poseStack.translate(-0.5, -0.5, -0.5);
            }
        }

        // Rendern
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
