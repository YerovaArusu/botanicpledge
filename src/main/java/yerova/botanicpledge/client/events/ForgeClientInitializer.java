package yerova.botanicpledge.client.events;


import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.forge.CapabilityUtil;
import yerova.botanicpledge.client.KeyBindings;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.client.render.blocks.ModificationAltarRenderer;
import yerova.botanicpledge.client.render.blocks.RitualCenterRenderer;
import yerova.botanicpledge.client.render.blocks.RitualPedestalRenderer;
import yerova.botanicpledge.client.render.blocks.YggdralSpreaderRenderer;
import yerova.botanicpledge.client.render.items.BotanicPledgeItemProperties;
import yerova.botanicpledge.common.blocks.block_entities.ModificationAltarBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPParticles;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {


    public static void attachBeCapabilities(AttachCapabilitiesEvent<BlockEntity> e) {
        var be = e.getObject();


        var makeWandHud = WAND_HUD.get().get(be.getType());
        if (makeWandHud != null) {
            e.addCapability(prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, makeWandHud.apply(be)));
        }
    }

    private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, WandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
        var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, WandHUD>>();
        BPBlockEntities.registerWandHudCaps((factory, types) -> {
            for (var type : types) {
                ret.put(type, factory);
            }
        });
        BPBlockEntities.registerWandHudCaps((factory, types) -> {
            for (var type : types) {
                ret.put(type, factory);
            }
        });
        return Collections.unmodifiableMap(ret);
    });


    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent e) {
        e.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "hud",
                (Fgui, gui, partialTick, width, height) -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.options.hideGui) {
                        return;
                    }

                    if (mc.hitResult instanceof BlockHitResult result) {
                        BlockPos bpos = result.getBlockPos();
                        BlockEntity tile = mc.level.getBlockEntity(bpos);

                        if (!PlayerHelper.hasHeldItem(mc.player, BotaniaItems.lexicon)) {
                            if (tile instanceof RitualCenterBlockEntity altar) {
                                RitualCenterBlockEntity.Hud.render(altar, gui, mc);
                            }
                            if (tile instanceof ModificationAltarBlockEntity altar) {
                                ModificationAltarBlockEntity.Hud.render(altar, gui, mc);
                            }
                        }
                    }
                });
    }


    @SubscribeEvent
    public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerBlockEntityRenderer(BPBlockEntities.RITUAL_CENTER.get(), RitualCenterRenderer::new);
        evt.registerBlockEntityRenderer(BPBlockEntities.RITUAL_PEDESTAL.get(), RitualPedestalRenderer::new);
        evt.registerBlockEntityRenderer(BPBlockEntities.YGGDRAL_SPREADER.get(), YggdralSpreaderRenderer::new);
        evt.registerBlockEntityRenderer(BPBlockEntities.MODIFICATION_TABLE.get(), ModificationAltarRenderer::new);
    }

    @SubscribeEvent
    public static void particleRegister(RegisterParticleProvidersEvent event) {
        BPParticles.FactoryHandler.registerFactories(new BPParticles.FactoryHandler.Consumer() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
                event.registerSpriteSet(type, constructor::apply);
            }
        });
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional evt) {
        var resourceManager = Minecraft.getInstance().getResourceManager();
        ModelBakery.onModelRegister(resourceManager, evt::register);
        BotanicPledgeItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult evt) {
        ModelBakery.onModelBake(evt.getModelBakery(), evt.getModels());
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.INSTANCE.switchSkillButton);

    }

}
