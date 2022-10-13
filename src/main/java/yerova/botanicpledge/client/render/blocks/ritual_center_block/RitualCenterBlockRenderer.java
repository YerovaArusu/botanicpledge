package yerova.botanicpledge.client.render.blocks.ritual_center_block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;

public class RitualCenterBlockRenderer extends GeoBlockRenderer<RitualCenterBlockEntity> {


    public RitualCenterBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new RitualCenterBlockModel());
    }

    @Override
    public RenderType getRenderType(RitualCenterBlockEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }


    @Override
    public void render(RitualCenterBlockEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn) {
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
        tileEntityIn.frames += 1.5f * Minecraft.getInstance().getDeltaFrameTime();
        entityItem.setYHeadRot(tileEntityIn.frames);
        //entityItem.age = (int) tileEntityIn.frames;
        Minecraft.getInstance().getEntityRenderDispatcher().render(entityItem, 0.5, 1, 0.5, entityItem.getYRot(), 2.0f, matrixStack, bufferIn, packedLightIn);

        matrixStack.popPose();

        super.render(tileEntityIn, partialTicks, matrixStack, bufferIn, packedLightIn);
    }

    public void renderFloatingItem(RitualCenterBlockEntity tileEntityIn, ItemEntity entityItem, double x, double y, double z, PoseStack stack, MultiBufferSource iRenderTypeBuffer) {
        stack.pushPose();

        tileEntityIn.frames += 1.5f * Minecraft.getInstance().getDeltaFrameTime();
        entityItem.setYHeadRot(tileEntityIn.frames);
        //entityItem.age = (int) tileEntityIn.frames;
        Minecraft.getInstance().getEntityRenderDispatcher().render(entityItem, 0.5, 1, 0.5, entityItem.getYRot(), 2.0f, stack, iRenderTypeBuffer, 15728880);

        stack.popPose();
    }
}
