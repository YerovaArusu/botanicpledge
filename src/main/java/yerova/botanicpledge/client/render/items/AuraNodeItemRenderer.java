package yerova.botanicpledge.client.render.items;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.render.AuraRenderType;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.setup.BotanicPledge;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AuraNodeItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation NODE_TEXTURE = new ResourceLocation("botanicpledge", "textures/misc/nodes.png");
    private static final int TEXTURE_SIZE = 2048;
    private static final int FRAME_SIZE = 64;
    private static final int TOTAL_FRAMES = 32;

    public static AuraNodeItemRenderer instance;

    public AuraNodeItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        instance = new AuraNodeItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        event.registerReloadListener(instance);
    }




    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext pDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int pPackedOverlay) {
        if (!stack.hasTag() || !stack.getTag().contains("BlockEntityTag")) return;

        CompoundTag blockEntityTag = stack.getTagElement("BlockEntityTag");

        if (!blockEntityTag.contains("Aura")) return;

        AuraImplementation imp = new AuraImplementation();
        imp = AuraImplementation.fromNBT(imp,blockEntityTag.getCompound("Aura"));

        if (imp == null) return;

        AuraNodeType nodeType = imp.getType();
        int color = imp.getNodeColor();

        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        long gameTime = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
        int frameDuration = 1;
        int currentFrame = (int) ((gameTime / frameDuration) % TOTAL_FRAMES);

        float minV = (nodeType.ordinal() * FRAME_SIZE) / (float) TEXTURE_SIZE;
        float maxV = minV + (FRAME_SIZE / (float) TEXTURE_SIZE);

        float minUCurrent = (currentFrame * FRAME_SIZE) / (float) TEXTURE_SIZE;
        float maxUCurrent = minUCurrent + (FRAME_SIZE / (float) TEXTURE_SIZE);


        var vertexConsumer = buffer.getBuffer(AuraRenderType.getAuraRenderType());
        poseStack.pushPose();

        RenderSystem.setShaderTexture(0, NODE_TEXTURE);
        RenderSystem.setShaderColor(red, green, blue, 1.0F);

        if (pDisplayContext == ItemDisplayContext.GUI) {
            poseStack.scale(1.5F, 1.5F, 1.5F);
            poseStack.translate(0.3125, 0.3125, 0.3125);
        } else if (pDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND){
            poseStack.translate(0.45, 0.75, 0.45);
            poseStack.scale(1F, 1F, 1F);
        } else if (pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND){
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(1F, 1F, 1F);
        }else {
            poseStack.translate(0.3125, 0.5, 0.35);
            poseStack.scale(1F, 1F, 1F);
        }



        float halfSize = 0.4F;

        vertexConsumer.vertex(poseStack.last().pose(), -halfSize, -halfSize, 0.0F).color(red, green, blue, 1.0F).uv(minUCurrent, maxV).uv2(light).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), halfSize, -halfSize, 0.0F).color(red, green, blue, 1.0F).uv(maxUCurrent, maxV).uv2(light).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), halfSize, halfSize, 0.0F).color(red, green, blue, 1.0F).uv(maxUCurrent, minV).uv2(light).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), -halfSize, halfSize, 0.0F).color(red, green, blue, 1.0F).uv(minUCurrent, minV).uv2(light).endVertex();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        super.renderByItem(stack,pDisplayContext,poseStack,buffer,light,pPackedOverlay);
    }

}
