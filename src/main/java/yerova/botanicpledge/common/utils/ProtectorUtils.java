package yerova.botanicpledge.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.BotanicPledge;

public class ProtectorUtils {

    public static void handleProtectorTick(LivingEntity player, ItemStack stack, int maxDefense, int defRegenPerTick, int maxCharge) {
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer) player;

        CompoundTag shield = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".shield");


        shield.putInt("MaxDefense", maxDefense);
        shield.putInt("MaxCharge", maxCharge);

        int charge = shield.getInt("Charge");
        if (charge < maxCharge)
            charge += ManaItemHandler.instance().requestMana(stack, serverPlayer, maxCharge - charge, true);

        int def = shield.getInt("Defense");
        if (def < maxDefense) {
            if(defRegenPerTick + def >= maxDefense) defRegenPerTick = maxDefense-def;

            if(charge >= defRegenPerTick * 4) {
                charge -= defRegenPerTick * 4;
                def += defRegenPerTick;
            }
            shield.putInt("Defense", def);
        }
        shield.putInt("Charge", charge);
    }
}
