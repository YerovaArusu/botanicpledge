package yerova.botanicpledge;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.SlotTypeMessage;
import yerova.botanicpledge.client.config.BotanicPledgeClientConfigs;
import yerova.botanicpledge.client.render.entities.ManaSlashRenderer;
import yerova.botanicpledge.client.render.entities.MarinaRenderer;
import yerova.botanicpledge.client.render.screen.CoreAltarScreen;
import yerova.botanicpledge.client.render.screen.MenuTypesInit;
import yerova.botanicpledge.client.render.screen.ProtectorHUD;
import yerova.botanicpledge.common.blocks.BlockInit;
import yerova.botanicpledge.common.blocks.block_entities.BlockEntityInit;
import yerova.botanicpledge.common.config.BotanicPledgeCommonConfigs;
import yerova.botanicpledge.common.entitites.EntityInit;
import yerova.botanicpledge.common.events.ForgeCommonInitializer;
import yerova.botanicpledge.common.items.ItemInit;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.recipes.RecipesInit;

import static net.minecraftforge.client.gui.ForgeIngameGui.HOTBAR_ELEMENT;

@Mod(BotanicPledge.MOD_ID)
public class BotanicPledge {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "botanicpledge";

    public BotanicPledge() {

        IEventBus forgeBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;


        eventBus.addGenericListener(ItemStack.class, ForgeCommonInitializer::attachItemCaps);

        ItemInit.ITEMS.register(forgeBus);
        EntityInit.ENTITY.register(forgeBus);
        BlockInit.BLOCKS.register(forgeBus);
        BlockEntityInit.BLOCK_ENTITIES.register(forgeBus);
        MenuTypesInit.MENUS.register(forgeBus);
        RecipesInit.SERIALIZERS.register(forgeBus);

        forgeBus.addListener(this::setup);
        forgeBus.addListener(this::enqueueIMC);
        forgeBus.addListener(this::processIMC);
        forgeBus.addListener(this::doClientStuff);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BotanicPledgeClientConfigs.SPEC, BotanicPledge.MOD_ID + "-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BotanicPledgeCommonConfigs.SPEC, BotanicPledge.MOD_ID + "-common.toml");

        GeckoLib.initialize();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Networking.register();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("divine_core")
                        .icon(new ResourceLocation("curios:slot/divine_core"))
                        .priority(16)
                        .size(1)
                        .build());
    }

    private void processIMC(final InterModProcessEvent event) {

    }
    private void doClientStuff(final FMLClientSetupEvent event) {
        EntityRenderers.register(EntityInit.MANA_SLASH.get(), ManaSlashRenderer::new);
        EntityRenderers.register(EntityInit.MARINA.get(), MarinaRenderer::new);

        ItemBlockRenderTypes.setRenderLayer(BlockInit.CORE_ALTAR.get(), RenderType.translucent());

        OverlayRegistry.registerOverlayAbove(HOTBAR_ELEMENT, "name", ProtectorHUD.PROTECTOR_HUD);

        MenuScreens.register(MenuTypesInit.CORE_ALTAR_MENU.get(), CoreAltarScreen::new);
    }


}
