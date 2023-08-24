package yerova.botanicpledge.common.events;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.common.handler.ModSounds;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.RuneGemItem;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BotanicPledge;
import yerova.botanicpledge.setup.BPItems;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AttributedItemsEventHandler {


    @SubscribeEvent
    public static void handleDamage(LivingAttackEvent e) {
        if (!e.getEntityLiving().level.isClientSide) {

            if (e.getEntityLiving() instanceof Player player) {
                for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(e.getEntityLiving(), "divine_core")) {
                    ItemStack stack = result.stack();
                    if (!e.isCanceled()) {
                        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

                            if (attribute.getCurrentShield() - e.getAmount() > 0) {
                                attribute.removeCurrentShield((int) Math.ceil(e.getAmount()));
                                player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.holyCloak, SoundSource.PLAYERS, 1F, 1F);
                                e.setCanceled(true);
                            }
                        });
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void handleDivineCoreJumps(LivingEvent.LivingJumpEvent evt) {

        LivingEntity entity = evt.getEntityLiving();
        for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(evt.getEntityLiving(), "divine_core")) {
            ItemStack stack = result.stack();

            if (!evt.isCanceled()) {
                AtomicReference<Double> jump = new AtomicReference<>(0.0);
                stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                    if (attribute.hasSocketAttribute(BPConstants.JUMP_HEIGHT_TAG_NAME)) {
                        double jumpHeight = 0;
                        for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.JUMP_HEIGHT_TAG_NAME)).toList()) {
                            jumpHeight += entry.getValue();
                        }
                        jump.set(jumpHeight);
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
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent e) {

        // Don't do anything client side
        if (e.world.isClientSide) {
            return;
        }
        if (e.phase == TickEvent.Phase.START) {
            for (Player player : e.world.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    AttributedItemsUtils.SyncShieldValuesToClient(serverPlayer);
                }
            }
        }
    }


}
