package yerova.botanicpledge.setup;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import vazkii.patchouli.api.PatchouliAPI;
import yerova.botanicpledge.client.events.ForgeClientInitializer;
import yerova.botanicpledge.client.render.entities.AsgardBladeRenderer;
import yerova.botanicpledge.client.render.entities.YggdFocusRenderer;
import yerova.botanicpledge.client.render.entities.YggdrafoliumRenderer;
import yerova.botanicpledge.client.render.entities.YggdrasilGuardianRenderer;
import yerova.botanicpledge.client.render.items.VedrfolnirCoreRenderer;
import yerova.botanicpledge.common.blocks.ManaBufferBlock;
import yerova.botanicpledge.common.blocks.OreInfusionBlock;
import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;
import yerova.botanicpledge.common.events.ForgeCommonInitializer;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.config.BPConfig;


@Mod(BotanicPledge.MOD_ID)
public class BotanicPledge {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "botanicpledge";
    public static BPConfig CONFIG;



    public BotanicPledge() {



        IEventBus forgeBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

        eventBus.addGenericListener(ItemStack.class, ForgeCommonInitializer::attachItemCaps);
        eventBus.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> DivineCoreItem.playerLoggedOut((ServerPlayer) e.getEntity()));
        eventBus.addListener((LivingEvent.LivingTickEvent e) -> {
            if (e.getEntity() instanceof Player player) {
                DivineCoreItem.updatePlayerFlyStatus(player);
            }
        });


        BPParticles.PARTICLES.register(forgeBus);
        BPEnchantments.ENCHANTMENTS.register(forgeBus);

        BPTabs.TABS.register(forgeBus);
        BPRecipes.SERIALIZERS.register(forgeBus);

        BPItems.ITEMS.register(forgeBus);
        BPEntities.ENTITY.register(forgeBus);
        BPBlocks.BLOCKS.register(forgeBus);
        BPBlockEntities.BLOCK_ENTITIES.register(forgeBus);
        BPLootModifiers.LOOT_MODIFIERS.register(forgeBus);
        BPEssences.ESSENCES.register(forgeBus);
        BPTrunkPlacerTypes.PLACER_TYPES.register(forgeBus);


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
        PatchouliAPI.get().registerMultiblock(new ResourceLocation(BotanicPledge.MOD_ID,"ore_infusion_structure"), OreInfusionBlock.ORE_INFUSION_STRUCTURE.get());

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
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.MANA_BUFFER.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.RITUAL_CENTER.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.RITUAL_PEDESTAL.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.YGGDRAL_SPREADER.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.MODIFICATION_TABLE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.YGGDRASIL_PYLON.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.ORE_INFUSION.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.YGGDRASIL_TRAPDOOR.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.ESSENCE_JAR.get(), RenderType.cutoutMipped());
        //ItemBlockRenderTypes.setRenderLayer(BPBlocks.ESSENCE_TRANSPORTER.get(), RenderType.translucent());
        //ItemBlockRenderTypes.setRenderLayer(BPBlocks.AURA_NODE.get(), RenderType.cutoutMipped());

        ItemBlockRenderTypes.setRenderLayer(BPBlocks.YGGDRASIL_LOG.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.YGGDRASIL_WOOD.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.STRIPPED_YGGDRASIL_LOG.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.STRIPPED_YGGDRASIL_WOOD.get(), RenderType.cutoutMipped());

    }


}
