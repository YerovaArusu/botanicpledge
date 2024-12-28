package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.InventoryHelper;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.relic.RelicBaubleItem;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.CoreAttribute;
import yerova.botanicpledge.common.capabilities.provider.CoreAttributeProvider;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class DivineCoreItem extends RelicBaubleItem {

    private static final String TAG_MANA = "mana";
    private static final String TAG_GLIDING = "gliding";
    private static final String TAG_DASH_COOLDOWN = "dashCooldown";
    private static final String TAG_IS_SPRINTING = "isSprinting";
    private static final String TAG_BOOST_PENDING = "boostPending";

    private static final String UUID_ARMOR = "01940a5c-b0f3-7ff9-bb8c-e0884a8cc18d";
    private static final String UUID_ARMOR_TOUGHNESS = "01940a5c-b0f3-7ff9-bb8c-e0884a8cc18d";
    private static final String UUID_HEALTH = "01940a5c-b0f3-7ff9-bb8c-e0884a8cc18d";
    private static final String UUID_SPEED = "01940a5c-b0f3-7ff9-bb8c-e0884a8cc18d";

    private static final List<String> playersWithFlight = Collections.synchronizedList(new ArrayList<>());

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
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        handleFlight(entity,stack);
        super.onWornTick(stack, entity);
    }


    public void handleFlight(LivingEntity living, ItemStack stack) {

        if (living instanceof Player player) {
            boolean flying = player.getAbilities().flying;

            boolean wasSprting = ItemNBTHelper.getBoolean(stack, TAG_IS_SPRINTING, false);
            boolean isSprinting = player.isSprinting();
            if (isSprinting != wasSprting) {
                ItemNBTHelper.setBoolean(stack, TAG_IS_SPRINTING, isSprinting);
            }

            Vec3 look = player.getLookAngle().multiply(1, 0, 1).normalize();

            if (flying) {
                final int maxCd = 80;
                int cooldown = ItemNBTHelper.getInt(stack, TAG_DASH_COOLDOWN, 0);
                if (!wasSprting && isSprinting && cooldown == 0) {
                    player.setDeltaMovement(player.getDeltaMovement().add(look.x, 0, look.z));
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.dash, SoundSource.PLAYERS, 1F, 1F);
                    ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, maxCd);
                    ItemNBTHelper.setBoolean(stack, TAG_BOOST_PENDING, true);
                } else if (cooldown > 0) {;
                    if (ItemNBTHelper.getBoolean(stack, TAG_BOOST_PENDING, false)) {
                        living.moveRelative(5F, new Vec3(0F, 0F, 1F));
                        ItemNBTHelper.removeEntry(stack, TAG_BOOST_PENDING);
                    }
                    ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, cooldown - 2);
                }
            } else {
                boolean wasGliding = ItemNBTHelper.getBoolean(stack, TAG_GLIDING, false);
                boolean doGlide = living.isShiftKeyDown() && !living.onGround() && (living.getDeltaMovement().y() < -.7F || wasGliding);


                if (doGlide) {
                    float mul = 0.6F;
                    living.setDeltaMovement(look.x * mul, Math.max(-0.15F, living.getDeltaMovement().y()), look.z * mul);
                    living.fallDistance = 2F;
                }
                ItemNBTHelper.setBoolean(stack, TAG_GLIDING, doGlide);
            }
        }

    }

    public static void updatePlayerFlyStatus(Player player) {
        ItemStack tiara = EquipmentHandler.findOrEmpty(BPItems.MARIAS_CORE.get(), player);

        if (playersWithFlight.contains(playerStr(player))) {
            if (shouldPlayerHaveFlight(player)) {
                player.getAbilities().mayfly = true;
                if (player.getAbilities().flying) {
                    if (!player.level().isClientSide) {
                        if (!player.isCreative() && !player.isSpectator()) {

                            if (player.tickCount %20 == 0) {
                                if (tiara.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).isPresent()) {
                                    CoreAttribute attribute = tiara.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve().get();
                                    ManaItemHandler.instance().requestManaExact(tiara, player, attribute.getManaCostPerTick(), true);
                                }
                            }
                        }
                    } else if (Math.abs(player.getDeltaMovement().x()) > 0.1 || Math.abs(player.getDeltaMovement().z()) > 0.1) {
                        double x = player.getX() - 0.5;
                        double y = player.getY() - 0.5;
                        double z = player.getZ() - 0.5;

                        float r = 1F;
                        float g = 1F;
                        float b = 1F;


                        for (int i = 0; i < 2; i++) {
                            SparkleParticleData data = SparkleParticleData.sparkle(2F * (float) Math.random(), r, g, b, 20);
                            player.level().addParticle(data, x + Math.random() * player.getBbWidth(), y + Math.random() * 0.4, z + Math.random() * player.getBbWidth(), 0, 0, 0);
                        }
                    }
                }
            } else {
                if (!player.isSpectator() && !player.isCreative()) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.getAbilities().invulnerable = false;
                }
                playersWithFlight.remove(playerStr(player));
            }
        } else if (shouldPlayerHaveFlight(player)) {
            playersWithFlight.add(playerStr(player));
            player.getAbilities().mayfly = true;
        }
    }

    public static void playerLoggedOut(ServerPlayer player) {
        String username = player.getGameProfile().getName();
        playersWithFlight.remove(username + ":false");
        playersWithFlight.remove(username + ":true");
    }

    private static String playerStr(Player player) {
        return player.getGameProfile().getName() + ":" + player.level().isClientSide;
    }

    private static boolean shouldPlayerHaveFlight(Player player) {
        ItemStack armor = EquipmentHandler.findOrEmpty(BPItems.MARIAS_CORE.get(), player);
        if (!armor.isEmpty()) {

            if (armor.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).isPresent()) {
                CoreAttribute attribute = armor.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve().get();

                return ManaItemHandler.instance().requestManaExactForTool(armor, player,attribute.getManaCostPerTick(),false);
            }
            return true;
        }

        return false;
    }



    private static boolean isDraconicEvolutionArmorEquipped(Player player) {
        return PlayerUtils.checkForArmorFromMod(player, BPConstants.DRACONIC_EVOLUTION_MODID);
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void onEquipped(ItemStack stack, LivingEntity entity) {
        super.onEquipped(stack, entity);

        entity.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE, 1.0F, 1.0F);
    }

    @Override
    public Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
        ImmutableMultimap.Builder<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
            addAttributeModifier(builder, Attributes.ARMOR, UUID.fromString(UUID_ARMOR), BPConstants.ARMOR_TAG_NAME, attribute, Attribute.Rune.StatType.ARMOR);
            addAttributeModifier(builder, Attributes.ARMOR_TOUGHNESS, UUID.fromString(UUID_ARMOR_TOUGHNESS), BPConstants.ARMOR_TOUGHNESS_TAG_NAME, attribute, Attribute.Rune.StatType.ARMOR_TOUGHNESS);
            addAttributeModifier(builder, Attributes.MAX_HEALTH, UUID.fromString(UUID_HEALTH), BPConstants.MAX_HEALTH_TAG_NAME, attribute, Attribute.Rune.StatType.MAX_HEALTH);
            addMovementSpeedModifier(builder, UUID.fromString(UUID_SPEED), BPConstants.MOVEMENT_SPEED_TAG_NAME, attribute);
        });
        return builder.build().isEmpty() ?  super.getEquippedAttributeModifiers(stack) : builder.build();
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
    public boolean canEquip(ItemStack stack, LivingEntity entity) {
        return !isDraconicEvolutionArmorEquipped((Player) entity);
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
        int max = LEVELS[Math.min(LEVELS.length - 1, level)];
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
        ManaItem item = new ManaItem(stack);
        return item.getMana() >= item.getMaxMana() - 1;
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
