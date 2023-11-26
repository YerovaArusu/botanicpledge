package yerova.botanicpledge.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.mixin.ItemEntityAccessor;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;

public class RitualCenterRenderer implements BlockEntityRenderer<RitualCenterBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public RitualCenterRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(RitualCenterBlockEntity tileEntityIn, float pPartialTick, PoseStack matrixStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        double x = tileEntityIn.getBlockPos().getX();
        double y = tileEntityIn.getBlockPos().getY();
        double z = tileEntityIn.getBlockPos().getZ();

        if (tileEntityIn.getHeldStack() == null)
            return;

        if (tileEntityIn.entity == null || !ItemStack.matches(tileEntityIn.entity.getItem(), tileEntityIn.getHeldStack())) {
            tileEntityIn.entity = new ItemEntity(tileEntityIn.getLevel(), x, y, z, tileEntityIn.getHeldStack());
        }


        ItemEntity entityItem = tileEntityIn.entity;

        ((ItemEntityAccessor) tileEntityIn.entity).setAge(ClientTickHandler.ticksInGame);
        entityItem.setItem(tileEntityIn.getHeldStack());

        matrixStack.pushPose();

        Minecraft.getInstance().getEntityRenderDispatcher().render(entityItem, 0.5, 1, 0.5, pPartialTick, 2.0f, matrixStack, pBufferSource, pPackedLight);

        matrixStack.popPose();
    }
}
