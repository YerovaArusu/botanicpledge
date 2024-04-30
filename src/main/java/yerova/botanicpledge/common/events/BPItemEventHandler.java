package yerova.botanicpledge.common.events;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.handler.BotaniaSounds;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.AttributeProvider;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.ConqueringSashItem;
import yerova.botanicpledge.common.items.SoulAmulet;
import yerova.botanicpledge.common.items.TerraShield;
import yerova.botanicpledge.common.items.relic.RingOfAesir;
import yerova.botanicpledge.common.utils.BPItemUtils;
import yerova.botanicpledge.integration.curios.ItemHelper;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BPItemEventHandler {


    @SubscribeEvent
    public static void handleCoreDamage(LivingAttackEvent e) {


        if (!e.getEntity().level().isClientSide) {

            ItemHelper.getDivineCoreCurio(e.getEntity()).forEach(slotResult -> {
                ItemStack stack = slotResult.stack();
                if (!e.isCanceled()) {
                    stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                        if (attribute.getCurrentShield() - e.getAmount() > 0) {
                            attribute.removeCurrentShield((int) Math.ceil(e.getAmount()));
                            e.getEntity().level().playSound(null, e.getEntity().getX(), e.getEntity().getY(), e.getEntity().getZ(), BotaniaSounds.holyCloak, SoundSource.PLAYERS, 1F, 1F);
                            e.setCanceled(true);
                        }
                    });
                }
            });


            if (e.getEntity() instanceof Player target && e.getSource().getEntity() instanceof Player attacker) {

                ItemHelper.getCurio(e.getEntity(), "necklace").forEach(slotResult -> {
                    ItemStack stack = slotResult.stack();
                    if (stack.getItem() instanceof SoulAmulet && SoulAmulet.amuletContainsSoul(stack, target.getUUID())) {
                        e.setCanceled(true);
                    }
                });

            }

            if (e.getEntity() instanceof Player player && RingOfAesir.onPlayerAttacked(player, e.getSource())) {
                e.setCanceled(true);
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock e) {
        RingOfAesir.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
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
    public static void handlePlayerJumps(LivingEvent.LivingJumpEvent evt) {

        LivingEntity entity = evt.getEntity();

        //Handle Conquering Sash
        ConqueringSashItem.onPlayerJump(entity);

        ItemHelper.getDivineCoreCurio(entity).forEach(slotResult -> {
            ItemStack stack = slotResult.stack();
            if (!evt.isCanceled()) {
                AtomicReference<Double> jump = new AtomicReference<>(0.0);
                stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

                    if (attribute.hasRuneType(Attribute.Rune.StatType.JUMP_HEIGHT)) {
                        jump.set(attribute.sumRunesOfType(Attribute.Rune.StatType.JUMP_HEIGHT));
                    }
                });

                Vec3 vec3 = entity.getDeltaMovement();
                if (jump.get() != 0.0) {
                    jump.set(vec3.y + (1 + (jump.get() / 100)));
                    entity.setDeltaMovement(vec3.x, jump.get(), vec3.z);
                } else {
                    entity.setDeltaMovement(vec3.x, vec3.y, vec3.z);
                }

                if (entity.isSprinting()) {
                    float f = entity.getYRot() * ((float) Math.PI / 180F);
                    entity.setDeltaMovement(entity.getDeltaMovement().add((double) (-Mth.sin(f) * 0.2F), 0.0D, (double) (Mth.cos(f) * 0.2F)));
                }

            }
        });
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            ConqueringSashItem.onPlayerFall(player, event.getDistance());
        }
    }


    @SubscribeEvent
    public static void SyncShield(TickEvent.LevelTickEvent e) {

        // Don't do anything client side
        if (e.level.isClientSide) {
            return;
        }
        if (e.phase == TickEvent.Phase.START) {
            for (Player player : e.level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    BPItemUtils.SyncShieldValuesToClient(serverPlayer);
                }
            }
        }
    }


}
