package yerova.botanicpledge.common.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.common.capabilities.CoreAttribute;
import yerova.botanicpledge.common.capabilities.YggdrasilAura;
import yerova.botanicpledge.common.capabilities.provider.CoreAttributeProvider;
import yerova.botanicpledge.common.capabilities.provider.YggdrasilAuraProvider;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncValues;
import yerova.botanicpledge.integration.curios.ItemHelper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class BPItemUtils {


    public static void handleShieldRegenOnCurioTick(LivingEntity player, ItemStack stack) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!((stack.getItem()) instanceof DivineCoreItem)) return;

        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attributes -> {
            attributes.incrementLastTimeHit();
            long lastTimeHit = attributes.getLastTimeHit();
            if (lastTimeHit < 60) return;
            if (attributes.getCurrentShield() >= attributes.getMaxShield()) return;

            int shieldRegenAmount = getShieldRegenAmount(attributes, lastTimeHit);
            int manaCost = shieldRegenAmount * 10;

            if (ManaItemHandler.instance().requestManaExactForTool(stack, serverPlayer, manaCost, true)) {
                attributes.addCurrentShield(shieldRegenAmount);
            }
        });
    }

    private static int getShieldRegenAmount(CoreAttribute attributes, long lastTimeHit) {
        double baseRegenAmount = 1;
        double growthRate = 0.05;
        int shieldRegenAmount = (int) (baseRegenAmount * Math.exp(growthRate * Math.max(lastTimeHit/20,1)));
        shieldRegenAmount = Math.min(shieldRegenAmount, attributes.getMaxShield() - attributes.getCurrentShield());
        return shieldRegenAmount;
    }


    public static void syncValueToClient(ServerPlayer serverPlayer) {

        AtomicInteger auraValue = new AtomicInteger();
        serverPlayer.level().getChunkAt(serverPlayer.getOnPos()).getCapability(YggdrasilAuraProvider.ESSENCE).ifPresent(aura -> {
            auraValue.set(aura.getGenPerInstance());
        });

        AtomicBoolean success = new AtomicBoolean(false);
        ItemHelper.getDivineCoreCurio(serverPlayer).forEach(slotResult -> {
            if (!(slotResult.stack().getItem() instanceof DivineCoreItem)) return;
            slotResult.stack().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                Networking.sendToPlayer(new SyncValues(attribute.getCurrentShield(), attribute.getMaxShield(), auraValue.get()), serverPlayer);
                success.set(true);
            });
        });

        if (!success.get()) {
            Networking.sendToPlayer(new SyncValues(0, 0, 0), serverPlayer);
        }
    }


}
