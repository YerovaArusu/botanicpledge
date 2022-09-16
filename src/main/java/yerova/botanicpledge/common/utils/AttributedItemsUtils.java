package yerova.botanicpledge.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncProtector;
import yerova.botanicpledge.common.utils.divine_core_utils.IDivineCoreAttributes;


public class AttributedItemsUtils implements IDivineCoreAttributes {
    public static void handleShieldRegenOnCurioTick(LivingEntity player, ItemStack stack, int maxShield, int defRegenPerTick, int maxCharge) {
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer) player;

        CompoundTag stats = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");



        //Normal Stats
        stats.putInt("MaxShield", maxShield);
        stats.putInt("MaxCharge", maxCharge);

        int charge = stats.getInt("Charge");
        if (charge < maxCharge)
            charge += ManaItemHandler.instance().requestMana(stack, serverPlayer, maxCharge - charge, true);

        int shield = stats.getInt("Shield");
        if (shield < maxShield) {
            if (defRegenPerTick + shield >= maxShield) defRegenPerTick = maxShield - shield;

            if (charge >= defRegenPerTick * 4) {
                charge -= defRegenPerTick * 4;
                shield += defRegenPerTick;
            }
            stats.putInt("Shield", shield);
        }
        stats.putInt("Charge", charge);
    }

    public static void SyncShieldValuesToClient(ServerPlayer serverPlayer) {
        boolean success = false;
        for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(serverPlayer, "necklace", "divine_core")) {
            if (result.stack().hasTag() && result.stack().getTag().contains(BotanicPledge.MOD_ID + ".stats")) {

                CompoundTag shield = result.stack().getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");

                Networking.sendToPlayer(new SyncProtector(
                        shield.getInt("Charge"),
                        shield.getInt("MaxCharge"),
                        shield.getInt("Shield"),
                        shield.getInt("MaxShield")), serverPlayer);

                success = true;
            }
        }

        if (!success) {
            Networking.sendToPlayer(new SyncProtector(0, 0, 0, 0), serverPlayer);
        }
    }


}
