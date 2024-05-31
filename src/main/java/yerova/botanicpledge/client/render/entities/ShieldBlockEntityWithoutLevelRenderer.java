package yerova.botanicpledge.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ShieldBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public static ShieldBlockEntityWithoutLevelRenderer instance;

    public ShieldBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher rd, EntityModelSet ems) {
        super(rd, ems);
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        instance = new ShieldBlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        event.registerReloadListener(instance);
    }


    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource bufferSource, int p_108834_, int p_108835_) {

        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        Material material = net.minecraft.client.resources.model.ModelBakery.SHIELD_BASE;

        if (itemStack.is(BPItems.TERRA_SHIELD.get())) {
            material = ModelBakery.TERRA_SHIELD;
        }
        if (itemStack.is(BPItems.MANA_SHIELD.get())) {
            material = ModelBakery.MANA_SHIELD;
        }


        VertexConsumer vertexconsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(bufferSource, this.shieldModel.renderType(material.atlasLocation()), true, itemStack.hasFoil()));
        this.shieldModel.handle().render(poseStack, vertexconsumer, p_108834_, p_108835_, 1.0F, 1.0F, 1.0F, 1.0F);
        this.shieldModel.plate().render(poseStack, vertexconsumer, p_108834_, p_108835_, 1.0F, 1.0F, 1.0F, 1.0F);


        poseStack.popPose();
    }


}
