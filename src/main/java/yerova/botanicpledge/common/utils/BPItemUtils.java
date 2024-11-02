package yerova.botanicpledge.common.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.common.capabilities.CoreAttribute;
import yerova.botanicpledge.common.capabilities.provider.CoreAttributeProvider;
import yerova.botanicpledge.common.capabilities.provider.YggdrasilAuraProvider;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncValues;
import yerova.botanicpledge.integration.curios.ItemHelper;

import java.util.Optional;


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
            int manaCost = shieldRegenAmount * 100;

            if (ManaItemHandler.instance().requestManaExactForTool(stack, serverPlayer, manaCost, true)) {
                attributes.addCurrentShield(shieldRegenAmount);
            }
        });
    }

    private static int getShieldRegenAmount(CoreAttribute attributes, long lastTimeHit) {
        double baseRegenAmount = 1;
        double growthRate = 0.05;
        int shieldRegenAmount = (int) (baseRegenAmount * Math.exp(growthRate * Math.max(lastTimeHit / 20, 1)));
        shieldRegenAmount = Math.min(shieldRegenAmount, attributes.getMaxShield() - attributes.getCurrentShield());
        return shieldRegenAmount;
    }


    public static void syncValueToClient(ServerPlayer serverPlayer) {


        int auraValue = serverPlayer.level().getChunkAt(serverPlayer.getOnPos())
                .getCapability(YggdrasilAuraProvider.ESSENCE)
                .map(aura -> aura.getGenPerInstance())
                .orElse(0);

        Optional<CoreAttribute> attribute = ItemHelper.getDivineCoreCurio(serverPlayer).stream().findFirst().map(slotResult -> slotResult.stack().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve().get());

        int def = attribute.map(a -> a.getCurrentShield()).orElse(0);
        int maxDef = attribute.map(a -> a.getMaxShield()).orElse(0);

        Networking.sendToPlayer(new SyncValues(def, maxDef, auraValue), serverPlayer);

    }


}
