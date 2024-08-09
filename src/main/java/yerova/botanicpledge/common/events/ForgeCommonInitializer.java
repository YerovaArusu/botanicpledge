package yerova.botanicpledge.common.events;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.forge.CapabilityUtil;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.provider.AttributeProvider;
import yerova.botanicpledge.common.capabilities.provider.YggdrasilAuraProvider;
import yerova.botanicpledge.common.items.YggdrasilsteelBandOfMana;
import yerova.botanicpledge.common.items.relic.*;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPEntities;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID)
public class ForgeCommonInitializer {

    private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
            BPItems.MARIAS_CORE.get(), DivineCoreItem::makeRelic,
            BPItems.YGGD_RAMUS.get(), DivineCoreItem::makeRelic,
            BPItems.ASGARD_FRACTAL.get(), AsgardFractal::makeRelic,
            BPItems.AESIR_RING.get(), RingOfAesir::makeRelic,
            BPItems.FIRST_RELIC.get(), FirstRelic::makeRelic,
            BPItems.ULL_BOW.get(), UllBow::makeRelic
    ));


    private static final Supplier<Map<Item, Function<ItemStack, ManaItem>>> MANA_ITEM = Suppliers.memoize(() -> Map.of(
            BPItems.MARIAS_CORE.get(), DivineCoreItem.ManaItem::new,
            BPItems.YGGDRASILSTEEL_BAND_OF_MANA.get(), YggdrasilsteelBandOfMana.YggdrasilsteelManaItemImpl::new
    ));

    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> e) {

        ItemStack stack = e.getObject();
        var makeRelic = RELIC.get().get(stack.getItem());
        if (makeRelic != null) {
            e.addCapability(prefix("relic"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.RELIC, makeRelic.apply(stack)));
        }

        var makeManaItem = MANA_ITEM.get().get(stack.getItem());
        if (makeManaItem != null) {
            e.addCapability(prefix("mana_item"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_ITEM, makeManaItem.apply(stack)));
        }

    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(new ResourceLocation(BotanicPledge.MOD_ID, "yggdrasil_aura"), new YggdrasilAuraProvider(event.getObject()));
    }





    @SubscribeEvent
    public static void addItemCaps(AttachCapabilitiesEvent<ItemStack> e) {

        ItemStack stack = e.getObject();

        if (stack.getItem() instanceof VedrfolnirsCore)
            e.addCapability(new ResourceLocation(BotanicPledge.MOD_ID, "attributes"), VedrfolnirsCore.getCoreAttribute());

        if (stack.getItem() instanceof AsgardFractal) {
            e.addCapability(new ResourceLocation(BotanicPledge.MOD_ID, "attributes"), new AttributeProvider(4, Attribute.Rune.EquipmentType.SWORD));
        }
    }

    @SubscribeEvent
    public static void addBECaps(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity blockEntity = e.getObject();


        if (blockEntity.getType() == BPBlockEntities.MODIFICATION_TABLE.get()) {
            e.addCapability(prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE,
                    (Wandable) blockEntity));
        }

    }

}
