package yerova.botanicpledge.client.events;


import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.client.fx.BotaniaParticles;
import yerova.botanicpledge.client.KeyBindsInit;
import yerova.botanicpledge.client.render.blocks.mana_ygdral_buffer_block.ManaYggdralBufferBlockRenderer;
import yerova.botanicpledge.client.render.blocks.ritual_center_block.RitualCenterBlockRenderer;
import yerova.botanicpledge.client.render.blocks.ritual_pedestal_block.RitualPedestalBlockRenderer;
import yerova.botanicpledge.client.render.items.BotanicPledgeItemProperties;
import yerova.botanicpledge.common.blocks.block_entities.BlockEntityInit;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.function.Function;

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
    public static void onModelRegister(ModelEvent evt) {
        BotanicPledgeItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));
    }


    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent e) {
        e.register(KeyBindsInit.yggdRamusSwitchMode);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent evt) {
        BotaniaParticles.FactoryHandler.registerFactories(new BotaniaParticles.FactoryHandler.Consumer() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
                evt.register(type, constructor::apply);
            }
        });
    }


}
