package yerova.botanicpledge.client.events;


import com.google.common.base.Suppliers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.forge.CapabilityUtil;
import vazkii.botania.forge.mixin.client.ForgeAccessorModelBakery;
import yerova.botanicpledge.client.model.ModelBakery;
import yerova.botanicpledge.client.render.blocks.RitualCenterRenderer;
import yerova.botanicpledge.client.render.blocks.RitualPedestalRenderer;
import yerova.botanicpledge.client.render.blocks.YggdralSpreaderRenderer;
import yerova.botanicpledge.client.render.items.BotanicPledgeItemProperties;
import yerova.botanicpledge.setup.BPBlockEntities;
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

    private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, IWandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
        var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, IWandHUD>>();
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
    public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerBlockEntityRenderer(BPBlockEntities.RITUAL_CENTER_BLOCK_ENTITY.get(), RitualCenterRenderer::new);
        evt.registerBlockEntityRenderer(BPBlockEntities.RITUAL_PEDESTAL_BLOCK_ENTITY.get(), RitualPedestalRenderer::new);
        evt.registerBlockEntityRenderer(BPBlockEntities.YGGDRAL_SPREADER.get(), YggdralSpreaderRenderer::new);
    }
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent evt) {
        var resourceManager = ((ForgeAccessorModelBakery) (Object) ForgeModelBakery.instance()).getResourceManager();
        ModelBakery.onModelRegister(resourceManager, ForgeModelBakery::addSpecialModel);
        BotanicPledgeItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));

    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent evt) {
        ModelBakery.onModelBake(evt.getModelLoader(), evt.getModelRegistry());
    }

}
