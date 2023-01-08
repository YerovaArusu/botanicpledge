package yerova.botanicpledge.client.events;


import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.render.items.BotanicPledgeItemProperties;
import yerova.botanicpledge.client.render.blocks.mana_ygdral_buffer_block.ManaYggdralBufferBlockRenderer;
import yerova.botanicpledge.client.render.blocks.ritual_center_block.RitualCenterBlockRenderer;
import yerova.botanicpledge.client.render.blocks.ritual_pedestal_block.RitualPedestalBlockRenderer;
import yerova.botanicpledge.common.blocks.block_entities.BlockEntityInit;
import yerova.botanicpledge.setup.BotanicPledge;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {

    @SubscribeEvent
    public static void registerArmorRenderer(final EntityRenderersEvent.AddLayers evt) {

    }


    @SubscribeEvent
    public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerBlockEntityRenderer(BlockEntityInit.MANA_YGGDRAL_BUFFER_BLOCK_ENTITY.get(), ManaYggdralBufferBlockRenderer::new);
        evt.registerBlockEntityRenderer(BlockEntityInit.RITUAL_CENTER_BLOCK_ENTITY.get(), RitualCenterBlockRenderer::new);
        evt.registerBlockEntityRenderer(BlockEntityInit.RITUAL_PEDESTAL_BLOCK_ENTITY.get(), RitualPedestalBlockRenderer::new);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent evt) {
        BotanicPledgeItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent evt) {
        //Miscellaneous.INSTANCE.onModelBake(evt.getModelLoader(), evt.getModelRegistry());
    }
}
