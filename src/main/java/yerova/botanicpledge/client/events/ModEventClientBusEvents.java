package yerova.botanicpledge.client.events;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.setup.BotanicPledge;
import yerova.botanicpledge.client.render.blocks.mana_ygdral_buffer_block.ManaYggdralBufferBlockRenderer;
import yerova.botanicpledge.client.render.blocks.ritual_center_block.RitualCenterBlockRenderer;
import yerova.botanicpledge.client.render.blocks.ritual_pedestal_block.RitualPedestalBlockRenderer;
import yerova.botanicpledge.common.blocks.block_entities.BlockEntityInit;

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

}
