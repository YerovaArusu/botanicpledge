package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.model.BotaniaModelLayers;
import vazkii.botania.client.model.ManaPylonModel;
import vazkii.botania.client.model.PylonModel;
import vazkii.botania.common.helper.VecHelper;
import yerova.botanicpledge.common.blocks.YggdrasilPylon;
import yerova.botanicpledge.common.blocks.block_entities.YggdrasilPylonBlockEntity;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nonnull;
import java.util.Random;

public class YggdrasilPylonRenderer implements BlockEntityRenderer<YggdrasilPylonBlockEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(BotanicPledge.MOD_ID,"textures/model/yggdrasil_pylon.png");

    private final ManaPylonModel model;

    private static ItemDisplayContext forceTransform = ItemDisplayContext.NONE;

    private final BlockRenderDispatcher blockRenderDispatcher;

    public YggdrasilPylonRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
        this.model = new ManaPylonModel(ctx.bakeLayer(BotaniaModelLayers.PYLON_MANA));
    }

    @Override
    public void render(YggdrasilPylonBlockEntity pylon, float pticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
        boolean renderingItem = pylon == null;
        boolean direct = renderingItem && (forceTransform == ItemDisplayContext.GUI || forceTransform.firstPerson()); // loosely based off ItemRenderer logic
        PylonModel model = this.model;

        ResourceLocation texture = TEXTURE;
        RenderType shaderLayer = direct ? RenderHelper.MANA_PYLON_GLOW_DIRECT : RenderHelper.MANA_PYLON_GLOW;

        ms.pushPose();

        float worldTime = ClientTickHandler.ticksInGame + pticks;

        worldTime += pylon == null ? 0 : new Random(pylon.getBlockPos().hashCode()).nextInt(360);

        ms.translate(0, pylon == null ? 1.35 : 1.5, 0);
        ms.scale(1.0F, -1.0F, -1.0F);

        ms.pushPose();
        ms.translate(0.5F, 0F, -0.5F);
        if (pylon != null) {
            ms.mulPose(VecHelper.rotateY(worldTime * 1.5F));
        }

        RenderType layer = RenderType.entityTranslucent(texture);

        VertexConsumer buffer = buffers.getBuffer(layer);
        model.renderRing(ms, buffer, light, overlay);
        if (pylon != null) {
            ms.translate(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
        }
        ms.popPose();

        ms.pushPose();
        if (pylon != null) {
            ms.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);
        }

        ms.translate(0.5F, 0F, -0.5F);
        if (pylon != null) {
            ms.mulPose(VecHelper.rotateY(-worldTime));
        }

        buffer = buffers.getBuffer(shaderLayer);
        model.renderCrystal(ms, buffer, light, overlay);

        ms.popPose();

        ms.popPose();
    }

    public static class ItemRenderer extends BlockEntityWithoutLevelRenderer {
        private static YggdrasilPylonBlockEntity DUMMY;

        public ItemRenderer() {
            super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }

        private static YggdrasilPylonBlockEntity getDummy() {
            if (DUMMY == null) {
                DUMMY = new YggdrasilPylonBlockEntity(BlockPos.ZERO, BPBlocks.YGGDRASIL_PYLON.get().defaultBlockState());
            }
            return DUMMY;
        }

        @Override
        public void renderByItem(ItemStack stack, @Nonnull ItemDisplayContext context, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
            if (Block.byItem(stack.getItem()) instanceof YggdrasilPylon) {
                BlockEntityRenderer<YggdrasilPylonBlockEntity> renderer = this.blockEntityRenderDispatcher.getRenderer(getDummy());
                //noinspection ConstantConditions
                forceTransform = context;
                ((YggdrasilPylonRenderer) renderer).render(null, 0f, poseStack, buffer, light, overlay);
            }
        }
    }
}

