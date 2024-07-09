package yerova.botanicpledge.common.events;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.entity.PixieEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.armor.elementium.ElementiumHelmItem;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.CoreAttribute;
import yerova.botanicpledge.common.capabilities.provider.CoreAttributeProvider;
import yerova.botanicpledge.common.items.ConqueringSashItem;
import yerova.botanicpledge.common.items.SoulAmulet;
import yerova.botanicpledge.common.items.TerraShield;
import yerova.botanicpledge.common.items.armor.CommonYggdrasilsteelArmor;
import yerova.botanicpledge.common.items.armor.YggdrasilsteelArmor;
import yerova.botanicpledge.common.items.armor.YggdrasilsteelHelmet;
import yerova.botanicpledge.common.items.relic.RingOfAesir;
import yerova.botanicpledge.common.utils.BPItemUtils;
import yerova.botanicpledge.integration.curios.ItemHelper;
import yerova.botanicpledge.setup.BPEnchantments;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static vazkii.botania.common.handler.PixieHandler.PIXIE_SPAWN_CHANCE;
import static yerova.botanicpledge.common.items.armor.YggdrasilsteelArmor.potions;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BPItemEventHandler {
    private static final List<ResourceKey<DamageType>> ENV_SOURCES = List.of(
            DamageTypes.IN_FIRE,
            DamageTypes.ON_FIRE,
            DamageTypes.LAVA,
            DamageTypes.HOT_FLOOR,
            DamageTypes.IN_WALL,
            DamageTypes.CRAMMING,
            DamageTypes.CACTUS
    );

    private static final int SHIELD_THRESHOLD_FIRE = 10;
    private static final int SHIELD_DECREMENT_ENV = 4;
    private static final int HIT_INTERVAL_MS = 1500;
    private static final float SPRINT_MODIFIER = 0.2F;
    private static final double JUMP_HEIGHT_MULTIPLIER = 0.01;

    @SubscribeEvent
    public static void handleCoreDamage(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();



        if (entity.level().isClientSide()) {
            return;
        }

        if (blockEnvironmentalDamage(event, event.getSource())) {
            return;
        }

        handleDivineCoreCurio(event, entity);

        if (entity instanceof Player player) {
            onGeneratePixie(player, event.getSource());
            handlePlayerSpecificDamage(event, player);
        }
    }

    private static void handleDivineCoreCurio(LivingAttackEvent event, LivingEntity entity) {
        ItemHelper.getDivineCoreCurio(entity).forEach(slotResult -> {
            ItemStack stack = slotResult.stack();
            stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                double remainingShield = attribute.getCurrentShield() - event.getAmount();
                if (remainingShield > 0) {
                    attribute.removeCurrentShield((int) Math.ceil(event.getAmount()));
                    playSound(entity);
                    event.setCanceled(true);
                }
            });
        });
    }

    private static void handlePlayerSpecificDamage(LivingAttackEvent event, Player player) {
        handleSoulAmulet(event, player);

        if (RingOfAesir.onPlayerAttacked(player, event.getSource())) {
            event.setCanceled(true);
        }
    }

    private static void handleSoulAmulet(LivingAttackEvent event, Player player) {
        ItemHelper.getCurio(player, "necklace").forEach(slotResult -> {
            ItemStack stack = slotResult.stack();
            if (stack.getItem() instanceof SoulAmulet && SoulAmulet.amuletContainsSoul(stack, player.getUUID())) {
                event.setCanceled(true);
            }
        });
    }

    private static void onGeneratePixie(Player player, DamageSource source){


        if (!player.level().isClientSide && source.getEntity() instanceof LivingEntity livingSource) {
            double chance = player.getAttributes().hasAttribute(PIXIE_SPAWN_CHANCE)
                    ? player.getAttributeValue(PIXIE_SPAWN_CHANCE) : 0;
            ItemStack sword = PlayerHelper.getFirstHeldItem(player, s -> s.is(BPItems.ASGARD_FRACTAL.get()));

            if (Math.random() < chance) {
                PixieEntity pixie = new PixieEntity(player.level());
                pixie.setPos(player.getX(), player.getY() + 2, player.getZ());

                if (CommonYggdrasilsteelArmor.hasArmorSet(player)) {
                    pixie.setApplyPotionEffect(new MobEffectInstance(potions[player.level().random.nextInt(potions.length)], 40, 2));
                }

                float dmg = 4;
                if (!sword.isEmpty()) {
                    dmg += 8;
                }

                pixie.setProps(livingSource, player, 0, dmg);
                pixie.finalizeSpawn((ServerLevelAccessor) player.level(), player.level().getCurrentDifficultyAt(pixie.blockPosition()),
                        MobSpawnType.EVENT, null, null);
                player.level().addFreshEntity(pixie);
            }
        }

    }

    private static boolean blockEnvironmentalDamage(LivingAttackEvent event, DamageSource source) {
        LivingEntity entity = event.getEntity();

        boolean isEnvironmentalDamage = ENV_SOURCES.stream().anyMatch(source::is);
        if (!isEnvironmentalDamage) {
            return false;
        }

        ItemHelper.getDivineCoreCurio(entity).forEach(slotResult -> {
            Optional<CoreAttribute> attributeOpt = slotResult.stack().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve();
            if (attributeOpt.isEmpty()) {
                return;
            }

            CoreAttribute attribute = attributeOpt.get();
            long currentTime = System.currentTimeMillis();

            if (source.is(DamageTypeTags.IS_FIRE) && attribute.getCurrentShield() > SHIELD_THRESHOLD_FIRE) {
                entity.clearFire();
            }

            if (attribute.getCurrentShield() >= SHIELD_DECREMENT_ENV) {
                if (currentTime - attribute.getLastTimeHit() < HIT_INTERVAL_MS) {
                    event.setCanceled(true);
                } else {
                    attribute.setLastTimeHit(currentTime);
                    attribute.removeCurrentShield(SHIELD_DECREMENT_ENV);
                    playSound(entity);
                    event.setCanceled(true);
                }
            }
        });

        return true;
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        RingOfAesir.onPlayerInteract(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (event.getEntity().getUseItem().is(BPItems.TERRA_SHIELD.get())) {
            if (event.getDamageSource().getEntity() instanceof LivingEntity attacker) {
                TerraShield.onShieldBlock(event.getEntity().level(), event.getEntity(), attacker, event.getEntity().getUseItem());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingWoodChop(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock().equals(BotaniaBlocks.livingwoodLog)
                && event.getPlayer().getItemInHand(event.getPlayer().getUsedItemHand()).getItem() instanceof AxeItem) {

            int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.YGGDRASIL_DEBARKING_BOUNTY.get(), event.getPlayer());
            int random = event.getLevel().getRandom().nextInt(100);

            if (random <= enchantmentLevel) {
                Vec3 pos = event.getPos().getCenter();
                event.getLevel().addFreshEntity(new ItemEntity(event.getPlayer().level(), pos.x, pos.y, pos.z, new ItemStack(BPItems.WORLD_ASH_BRANCH.get())));
            }
        }
    }

    @SubscribeEvent
    public static void handlePlayerJumps(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();

        ConqueringSashItem.onPlayerJump(entity);
        handleJumpHeightBoost(entity);
    }

    private static void handleJumpHeightBoost(LivingEntity entity) {
        ItemHelper.getDivineCoreCurio(entity).forEach(slotResult -> {
            ItemStack stack = slotResult.stack();
            AtomicReference<Double> jumpBoost = new AtomicReference<>(0.0);

            stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                if (attribute.hasRuneType(Attribute.Rune.StatType.JUMP_HEIGHT)) {
                    jumpBoost.set(attribute.sumRunesOfType(Attribute.Rune.StatType.JUMP_HEIGHT));
                }
            });

            applyJumpBoost(entity, jumpBoost.get());
        });
    }

    private static void applyJumpBoost(LivingEntity entity, double jumpBoost) {
        Vec3 movement = entity.getDeltaMovement();
        if (jumpBoost != 0.0) {
            entity.setDeltaMovement(movement.x, movement.y + (1 + (jumpBoost * JUMP_HEIGHT_MULTIPLIER)), movement.z);
        }

        if (entity.isSprinting()) {
            float rotation = entity.getYRot() * ((float) Math.PI / 180F);
            entity.setDeltaMovement(entity.getDeltaMovement().add(-Mth.sin(rotation) * SPRINT_MODIFIER, 0.0D, Mth.cos(rotation) * SPRINT_MODIFIER));
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            ConqueringSashItem.onPlayerFall(player, event.getDistance());
        }
    }

    @SubscribeEvent
    public static void syncShield(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide() || event.phase != TickEvent.Phase.START) {
            return;
        }

        event.level.players().stream()
                .filter(player -> player instanceof ServerPlayer)
                .forEach(player -> BPItemUtils.SyncShieldValuesToClient((ServerPlayer) player));
    }

    private static void playSound(LivingEntity entity) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BotaniaSounds.holyCloak, SoundSource.PLAYERS, 1F, 1F);
    }
}
