package yerova.botanicpledge.setup;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.patchouli.api.PatchouliAPI;
import yerova.botanicpledge.client.events.ForgeClientInitializer;
import yerova.botanicpledge.client.render.entities.AsgardBladeRenderer;
import yerova.botanicpledge.client.render.entities.YggdFocusRenderer;
import yerova.botanicpledge.client.render.entities.YggdrafoliumRenderer;
import yerova.botanicpledge.client.render.entities.YggdrasilGuardianRenderer;
import yerova.botanicpledge.client.render.items.VedrfolnirCoreRenderer;
import yerova.botanicpledge.client.render.screen.YggdrasilBossBar;
import yerova.botanicpledge.common.blocks.ManaBufferBlock;
import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;
import yerova.botanicpledge.common.events.ForgeCommonInitializer;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.config.BPConfig;
import yerova.botanicpledge.integration.expanded_combat.ExpandedCombatPlugin;


@Mod(BotanicPledge.MOD_ID)
public class BotanicPledge {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "botanicpledge";
    public static BPConfig CONFIG;



    public BotanicPledge() {



        IEventBus forgeBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

        eventBus.addGenericListener(ItemStack.class, ForgeCommonInitializer::attachItemCaps);


        BPParticles.PARTICLES.register(forgeBus);
        BPEnchantments.ENCHANTMENTS.register(forgeBus);

        BPTabs.TABS.register(forgeBus);
        BPRecipes.SERIALIZERS.register(forgeBus);

        BPItems.ITEMS.register(forgeBus);
        BPEntities.ENTITY.register(forgeBus);
        BPBlocks.BLOCKS.register(forgeBus);
        BPBlockEntities.BLOCK_ENTITIES.register(forgeBus);
        BPLootModifiers.LOOT_MODIFIERS.register(forgeBus);


        forgeBus.addListener(this::setup);
        forgeBus.addListener(this::processIMC);
        forgeBus.addListener(this::doClientStuff);
        forgeBus.addListener(this::addEntityAttributes);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BPEntities.YGGDRASIL_GUARDIAN.get(), Animal.createMobAttributes().build());
    }

    private void setup(final FMLCommonSetupEvent event) {
        Networking.register();
        PatchouliAPI.get().registerMultiblock(new ResourceLocation(BotanicPledge.MOD_ID,"yggdrasil_ritual"), YggdrasilGuardian.ARENA_MULTIBLOCK.get());
        PatchouliAPI.get().registerMultiblock(new ResourceLocation(BotanicPledge.MOD_ID,"mana_buffer_structure"), ManaBufferBlock.MANA_BUFFER_STRUCTURE.get());


    }


    private void processIMC(final InterModProcessEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, ForgeClientInitializer::attachBeCapabilities);



        EntityRenderers.register(BPEntities.YGGD_FOCUS.get(), YggdFocusRenderer::new);
        EntityRenderers.register(BPEntities.YGGDRAFOLIUM.get(), YggdrafoliumRenderer::new);
        EntityRenderers.register(BPEntities.ASGARD_BLADE.get(), AsgardBladeRenderer::new);
        EntityRenderers.register(BPEntities.YGGDRASIL_GUARDIAN.get(), YggdrasilGuardianRenderer::new);

        CuriosRendererRegistry.register(BPItems.MARIAS_CORE.get(), VedrfolnirCoreRenderer::new);

        ItemBlockRenderTypes.setRenderLayer(BPBlocks.THUNDER_LILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.MANA_BUFFER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.RITUAL_CENTER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.RITUAL_PEDESTAL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.YGGDRAL_SPREADER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.MODIFICATION_TABLE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.YGGDRASIL_PYLON.get(), RenderType.translucent());

    }


}
