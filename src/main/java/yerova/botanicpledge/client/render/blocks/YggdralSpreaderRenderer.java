package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.common.helper.VecHelper;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.common.blocks.block_entities.YggdralSpreaderBlockEntity;

import javax.annotation.Nonnull;
import java.util.Random;

public class YggdralSpreaderRenderer implements BlockEntityRenderer<YggdralSpreaderBlockEntity> {

    public YggdralSpreaderRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(@Nonnull YggdralSpreaderBlockEntity spreader, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
        ms.pushPose();

        ms.translate(0.5F, 0.5, 0.5F);

        Quaternionf transform = VecHelper.rotateY(spreader.rotationX + 90F);
        transform.mul(VecHelper.rotateX(spreader.rotationY));
        ms.mulPose(transform);

        ms.translate(-0.5F, -0.5F, -0.5F);

        double time = ClientTickHandler.ticksInGame + partialTicks;

        float r = 1, g = 1, b = 1;

        int color = Mth.hsvToRgb((float) ((time * 2 + new Random(spreader.getBlockPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
        r = (color >> 16 & 0xFF) / 255F;
        g = (color >> 8 & 0xFF) / 255F;
        b = (color & 0xFF) / 255F;


        VertexConsumer buffer = buffers.getBuffer(ItemBlockRenderTypes.getRenderType(spreader.getBlockState(), false));
        BakedModel spreaderModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(spreader.getBlockState());
        Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                .renderModel(ms.last(), buffer, spreader.getBlockState(),
                        spreaderModel, r, g, b, light, overlay);

        ms.pushPose();
        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(VecHelper.rotateY((float) time % 360));
        ms.translate(-0.5, -0.5, -0.5);
        ms.translate(0F, (float) Math.sin(time / 20.0) * 0.05F, 0F);
        BakedModel core = getCoreModel();
        Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                .renderModel(ms.last(), buffer, spreader.getBlockState(),
                        core, 1, 1, 1, light, overlay);
        ms.popPose();

        ItemStack stack = spreader.getItemHandler().getItem(0);
        if (!stack.isEmpty()) {
            ms.pushPose();
            ms.translate(0.5F, 0.5F, 0.094F);
            ms.mulPose(VecHelper.rotateZ(180));
            ms.mulPose(VecHelper.rotateX(180));
            // Prevents z-fighting. Otherwise not noticeable.
            ms.scale(0.997F, 0.997F, 1F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
                    light, overlay, ms, buffers, spreader.getLevel(), 0);
            ms.popPose();
        }

        if (spreader.paddingColor != null) {
            ms.pushPose();
            // The padding model is facing up so that the textures are rotated the correct way
            // It's simpler to do this than mess with rotation and UV in the json model
            ms.translate(0.5F, 0.5F, 0.5F);
            ms.mulPose(VecHelper.rotateZ(-90));
            ms.mulPose(VecHelper.rotateX(180));
            ms.translate(-0.5F, -0.5F, -0.5F);
            BakedModel paddingModel = getPaddingModel(spreader.paddingColor);
            Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                    .renderModel(ms.last(), buffer, spreader.getBlockState(),
                            paddingModel, r, g, b, light, overlay);
            ms.popPose();
        }

        ms.popPose();

        if (spreader.getBlockState().getValue(BotaniaStateProperties.HAS_SCAFFOLDING)) {
            BakedModel scaffolding = getScaffoldingModel();
            Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                    .renderModel(ms.last(), buffer, spreader.getBlockState(),
                            scaffolding, r, g, b, light, overlay);
        }

    }

    private BakedModel getCoreModel() {
        return ModelBakery.yggralSpreaderCore;
    }

    private BakedModel getPaddingModel(DyeColor color) {
        return MiscellaneousModels.INSTANCE.spreaderPaddings.get(color);
    }

    private BakedModel getScaffoldingModel() {
        return ModelBakery.yggdralSpreaderScaffolding;
    }


}
