package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.mixin.ItemEntityAccessor;
import yerova.botanicpledge.common.blocks.block_entities.ModificationAltarBlockEntity;
import yerova.botanicpledge.common.capabilities.Attribute;

import java.util.List;

public class ModificationAltarRenderer implements BlockEntityRenderer<ModificationAltarBlockEntity> {

    private final BlockRenderDispatcher blockRenderDispatcher;

    public ModificationAltarRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(ModificationAltarBlockEntity tileEntityIn, float pPartialTick, PoseStack matrixStack, MultiBufferSource buffers, int light, int overlay) {


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

        ((ItemEntityAccessor) tileEntityIn.entity).setAge((int) (ClientTickHandler.ticksInGame + pPartialTick));
        entityItem.setItem(tileEntityIn.getHeldStack());


        Minecraft.getInstance().getEntityRenderDispatcher().render(entityItem, 0.5, 1, 0.5, pPartialTick, 2.0f, matrixStack, buffers, light);

        matrixStack.popPose();


        matrixStack.pushPose();
        List<ItemStack> runes = tileEntityIn.getRunes(tileEntityIn).stream().map(Attribute.Rune::getAsStack).toList();

        float[] angles = new float[runes.size()];

        float anglePer = 360F / runes.size();
        float totalAngle = 0F;
        for (int i = 0; i < angles.length; i++) {
            angles[i] = totalAngle += anglePer;
        }

        double time = ClientTickHandler.ticksInGame + pPartialTick;

        for (int i = 0; i < runes.size(); i++) {
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 1.25F, 0.5F);
            matrixStack.mulPose(VecHelper.rotateY(angles[i] + (float) time));
            matrixStack.translate(1.125F, 0F, 0.25F);
            matrixStack.mulPose(VecHelper.rotateY(90F));
            matrixStack.translate(0D, 0.075 * Math.sin((time + i * 10) / 5D), 0F);
            ItemStack stack = runes.get(i);
            Minecraft mc = Minecraft.getInstance();
            if (!stack.isEmpty()) {
                mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND,
                        light, overlay, matrixStack, buffers, tileEntityIn.getLevel(), 0);
            }
            matrixStack.popPose();
        }

        matrixStack.popPose();


    }
}
