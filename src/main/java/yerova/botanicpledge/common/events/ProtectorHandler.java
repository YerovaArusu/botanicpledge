package yerova.botanicpledge.common.events;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.common.handler.ModSounds;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncProtector;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ProtectorHandler {


    @SubscribeEvent
    public static void handleDamage(LivingAttackEvent e) {
        if (!e.getEntityLiving().level.isClientSide) {

            if (e.getEntityLiving() instanceof Player) {
                for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(e.getEntityLiving(), "necklace")) {

                    ItemStack stack = result.stack();


                    if (!e.isCanceled() && stack.hasTag() && stack.getTag().contains(BotanicPledge.MOD_ID + ".shield")) {

                        CompoundTag shield = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".shield");

                        int def = Math.max(0, shield.getInt("Defense") - (int) Math.ceil(e.getAmount()));
                        shield.putInt("Defense", def);

                        if (def <= 0) {
                            break;
                        } else {
                            e.getEntityLiving().level.playSound(null,
                                    e.getEntityLiving().getX(),
                                    e.getEntityLiving().getY(),
                                    e.getEntityLiving().getZ(),
                                    ModSounds.holyCloak, SoundSource.PLAYERS, 1F, 1F);

                            e.setCanceled(true);
                        }
                    }
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
                    boolean success = false;
                    for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(serverPlayer, "necklace")) {
                        if (result.stack().hasTag() && result.stack().getTag().contains(BotanicPledge.MOD_ID + ".shield")) {

                            CompoundTag shield = result.stack().getOrCreateTagElement(BotanicPledge.MOD_ID + ".shield");

                            Networking.sendToPlayer(new SyncProtector(
                                    shield.getInt("Charge"),
                                    shield.getInt("MaxCharge"),
                                    shield.getInt("Defense"),
                                    shield.getInt("MaxDefense")), serverPlayer);

                            success = true;
                        }
                    }

                    if(!success) {
                        Networking.sendToPlayer(new SyncProtector(0, 0, 0, 0), serverPlayer);
                    }
                }
            }
        }
    }
}
