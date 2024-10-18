package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.lwjgl.openal.SOFTDeferredUpdates;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.item.relic.RelicBaubleItem;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.CoreAttribute;
import yerova.botanicpledge.common.capabilities.provider.CoreAttributeProvider;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public abstract class DivineCoreItem extends RelicBaubleItem implements ICurioItem {

    private static final String TAG_MANA = "mana";
    public static final int MAX_LEVEL_MANA = 2_000_000_000;
    private static final int TICK_INTERVAL = 20;

    public static final int[] LEVELS = {
            0, 10_000, 1_000_000, 10_000_000, 100_000_000, 1_000_000_000, MAX_LEVEL_MANA
    };

    public DivineCoreItem(Properties props) {
        super(props);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.PROJECTILE_PROTECTION
                || enchantment == Enchantments.BLAST_PROTECTION
                || enchantment == Enchantments.FIRE_PROTECTION
                || enchantment == Enchantments.ALL_DAMAGE_PROTECTION;
    }

    @Override
    public int getEnchantmentValue() {
        return 25;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        handleFlight(player,stack);

    }

    public static void handleFlight(Player player,ItemStack stack) {
        if (stack.isEmpty() || player.isCreative() || player.isSpectator()) return;

        if (checkIfAllowedToFly(player,stack) && !player.getAbilities().mayfly) {
            startFlying(player);
        } else if (!checkIfAllowedToFly(player,stack) && player.getAbilities().mayfly) {
            stopFlying(player);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.translatable("botanicpledge.attributes.cant_fly").withStyle(ChatFormatting.DARK_RED));
            }
        }

        if (player.tickCount % TICK_INTERVAL == 0 &&player.getAbilities().mayfly && player.getAbilities().flying) {
            if(stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).isPresent()) {
                CoreAttribute attribute = stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve().get();

                ManaItemHandler.INSTANCE.requestManaExactForTool(stack,player,attribute.getManaCostPerTick(), true);
            }
        }
    }

    private static boolean checkIfAllowedToFly(Player player, ItemStack stack) {
        if(!stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).isPresent()) return false;
        CoreAttribute attribute = stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve().get();

        return ManaItemHandler.INSTANCE.requestManaExactForTool(stack,player,attribute.getManaCostPerTick(), false);
    }


    private boolean isDraconicEvolutionArmorEquipped(Player player) {
        return PlayerUtils.checkForArmorFromMod(player, BPConstants.DRACONIC_EVOLUTION_MODID);
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        slotContext.entity().playSound(SoundEvents.ARMOR_EQUIP_NETHERITE,
                1.0F, 1.0F);

    }

    @Override
    public Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
            addAttributeModifier(builder, Attributes.ARMOR, uuid, BPConstants.ARMOR_TAG_NAME, attribute, Attribute.Rune.StatType.ARMOR);
            addAttributeModifier(builder, Attributes.ARMOR_TOUGHNESS, uuid, BPConstants.ARMOR_TOUGHNESS_TAG_NAME, attribute, Attribute.Rune.StatType.ARMOR_TOUGHNESS);
            addAttributeModifier(builder, Attributes.MAX_HEALTH, uuid, BPConstants.MAX_HEALTH_TAG_NAME, attribute, Attribute.Rune.StatType.MAX_HEALTH);
            addMovementSpeedModifier(builder, uuid, BPConstants.MOVEMENT_SPEED_TAG_NAME, attribute);
        });
        return builder.build().isEmpty() ? ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack) : builder.build();
    }

    private void addAttributeModifier(ImmutableMultimap.Builder<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> builder,
                                      net.minecraft.world.entity.ai.attributes.Attribute attribute,
                                      UUID uuid,
                                      String tagName,
                                      CoreAttribute coreAttribute,
                                      Attribute.Rune.StatType type) {
        builder.put(attribute, new AttributeModifier(uuid, tagName, coreAttribute.sumRunesOfType(type), AttributeModifier.Operation.ADDITION));
    }

    private void addMovementSpeedModifier(ImmutableMultimap.Builder<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> builder,
                                          UUID uuid,
                                          String tagName,
                                          CoreAttribute coreAttribute) {
        double speedBoost = coreAttribute.sumRunesOfType(Attribute.Rune.StatType.MOVEMENT_SPEED) / 100.0;
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, tagName, speedBoost, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && !stack.getItem().equals(newStack.getItem())) {
            stopFlying(player);
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return isEquipable((Player) slotContext.entity(), stack);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return !isDraconicEvolutionArmorEquipped((Player) slotContext.entity());
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        return isEquipable(player, stack) && !(player.getOffhandItem().getItem() instanceof DivineCoreItem);
    }

    private boolean isEquipable(Player player, ItemStack stack) {
        return !isDraconicEvolutionArmorEquipped(player);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
            addRunesTooltip(tooltip, attribute);
            addEmptySocketsTooltip(tooltip, attribute);
        });

        super.appendHoverText(stack, world, tooltip, flags);
    }


    private void addRunesTooltip(List<Component> tooltip, CoreAttribute attribute) {
        attribute.getAllRunes().forEach(rune ->
                tooltip.add(Component.literal("+ " + rune.getValue() + " " + Component.translatable(rune.getStatType().name().toLowerCase()).getString())
                        .withStyle(ChatFormatting.BLUE))
        );
    }

    private void addEmptySocketsTooltip(List<Component> tooltip, CoreAttribute attribute) {
        if (attribute.hasEmptySocket()) {
            tooltip.add(Component.literal(Component.translatable(BPConstants.NO_RUNE_GEM).getString() + ": " + (attribute.getMaxRunes() - attribute.getAllRunes().size()))
                    .withStyle(ChatFormatting.GOLD));
        }
    }



    public static void startFlying(Player player) {
        player.getAbilities().mayfly = true;
        player.onUpdateAbilities();
    }

    private static void stopFlying(Player player) {
        if (player.isSpectator() || player.isCreative()) return;
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
    }


    public static int getShieldValueAccordingToRank(ItemStack stack, int defaultValue) {
        return stack.getItem() instanceof DivineCoreItem ? getLevel(stack) * defaultValue : 0;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        int level = getLevel(stack);
        int max = LEVELS[Math.min(LEVELS.length - 1, level )];
        int curr = new ManaItem(stack).getMana();
        float percent = (float) curr / max;
        return Optional.of(new ManaBarTooltip(percent, level));
    }

    public static int getLevel(ItemStack stack) {
        ManaItem item = new ManaItem(stack);
        long mana = item.getMana();

        for (int i = LEVELS.length - 1; i >= 0; i--) {
            if (mana >= LEVELS[i]) {
                return i;
            }
        }
        return 0;
    }


    public static class ManaItem implements vazkii.botania.api.mana.ManaItem {
        private final ItemStack stack;

        public ItemStack getStack() {
            return stack;
        }

        public ManaItem(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int getMana() {
            return ItemNBTHelper.getInt(stack, TAG_MANA, 0) * stack.getCount();
        }

        @Override
        public int getMaxMana() {
            int level = getLevel(stack);
            if (level == 0) return LEVELS[1];
            if (getMana() >= LEVELS[level]) level++;
            return LEVELS[Math.min(LEVELS.length - 1, level)] * stack.getCount();
        }

        @Override
        public void addMana(int mana) {

            ItemNBTHelper.setInt(stack, TAG_MANA, Math.min(getMana() + mana, MAX_LEVEL_MANA));
        }


        @Override
        public boolean canReceiveManaFromPool(BlockEntity pool) {
            return true;
        }

        @Override
        public boolean canReceiveManaFromItem(ItemStack otherStack) {
            return true;
        }

        @Override
        public boolean canExportManaToPool(BlockEntity pool) {
            return false;
        }

        @Override
        public boolean canExportManaToItem(ItemStack otherStack) {
            return false;
        }

        @Override
        public boolean isNoExport() {
            return true;
        }
    }


    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        DivineCoreItem.ManaItem item = new ManaItem(stack);
        return (int) Math.ceil(13 * (item.getMana() / (float) item.getMaxMana()));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(getBarWidth(stack) / 3.0F, 1.0F, 1.0F);
    }

    @Nonnull
    @Override
    public Rarity getRarity(@Nonnull ItemStack stack) {
        int level = getLevel(stack);
        if (stack.isEnchanted()) {
            level++;
        }

        return switch (level) {
            case 5, 6, 7 -> Rarity.EPIC;
            case 3, 4 -> Rarity.RARE;
            default -> Rarity.UNCOMMON;
        };
    }
}
