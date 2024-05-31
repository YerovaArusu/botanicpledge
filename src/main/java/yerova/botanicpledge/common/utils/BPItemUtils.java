package yerova.botanicpledge.common.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.common.capabilities.provider.CoreAttributeProvider;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncProtector;
import yerova.botanicpledge.integration.curios.ItemHelper;

import java.util.concurrent.atomic.AtomicBoolean;


public class BPItemUtils {


    public static void handleShieldRegenOnCurioTick(LivingEntity player, ItemStack stack) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!((stack.getItem()) instanceof DivineCoreItem)) return;

        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attributes -> {
            if (attributes.getCurrentCharge() < attributes.getMaxCharge()) {
                attributes.addCurrentCharge(ManaItemHandler.instance().requestMana(stack, serverPlayer, attributes.getMaxCharge() - attributes.getCurrentCharge(), true));
            }

            if (attributes.getCurrentShield() < attributes.getMaxShield()) {
                int defRegen = attributes.getDefRegenPerTick();
                if (defRegen + attributes.getCurrentShield() >= attributes.getMaxShield())
                    defRegen = attributes.getMaxShield() + attributes.getCurrentShield();

                if (attributes.getCurrentCharge() >= defRegen * BPConstants.MANA_TO_SHIELD_CONVERSION_RATE) {
                    attributes.removeCurrentCharge(defRegen * BPConstants.MANA_TO_SHIELD_CONVERSION_RATE);
                    attributes.addCurrentShield(defRegen);
                }
            }
        });
    }

    public static void SyncShieldValuesToClient(ServerPlayer serverPlayer) {
        AtomicBoolean success = new AtomicBoolean(false);
        ItemHelper.getDivineCoreCurio(serverPlayer).forEach(slotResult -> {
            if (!(slotResult.stack().getItem() instanceof DivineCoreItem)) return;
            slotResult.stack().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                Networking.sendToPlayer(new SyncProtector(attribute.getCurrentCharge(), attribute.getMaxCharge(), attribute.getCurrentShield(), attribute.getMaxShield()), serverPlayer);
                success.set(true);
            });
        });

        if (!success.get()) {
            Networking.sendToPlayer(new SyncProtector(0, 0, 0, 0), serverPlayer);
        }
    }


}
