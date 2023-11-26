package yerova.botanicpledge.common.utils;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncProtector;
import yerova.botanicpledge.setup.BPAttributes;

import java.util.HashMap;
import java.util.Map;
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
        for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(serverPlayer, "divine_core")) {
            if (!(result.stack().getItem() instanceof DivineCoreItem)) return;

            result.stack().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                Networking.sendToPlayer(new SyncProtector(attribute.getCurrentCharge(), attribute.getMaxCharge(), attribute.getCurrentShield(), attribute.getMaxShield()), serverPlayer);

                success.set(true);
            });
        }

        if (!success.get()) {
            Networking.sendToPlayer(new SyncProtector(0, 0, 0, 0), serverPlayer);
        }
    }

    public static HashMap<Integer, Map.Entry<String, Double>> getBPAttributeDefault() {
        HashMap<Integer, Map.Entry<String, Double>> map = new HashMap<>();

        for (int i = 1; i < BPConstants.MAX_SOCKETS; i++) {
            map.put(i, Map.entry(BPConstants.NO_RUNE_GEM, 0.0));
        }

        return map;
    }


    public static Lazy<? extends Multimap<Attribute, AttributeModifier>> createLazyAttributeList(float damage, float atkSpeed, float manaDamage) {
        return Lazy.of(() -> {
            Multimap<Attribute, AttributeModifier> map;
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", damage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", atkSpeed, AttributeModifier.Operation.ADDITION));

            if (BPAttributes.MANA_DAMAGE.isPresent()) {
                builder.put(BPAttributes.MANA_DAMAGE.get(), new AttributeModifier(BPAttributes.BASE_MANA_DAMAGE_UUID, "Weapon modifier", manaDamage, AttributeModifier.Operation.ADDITION));
            }
            map = builder.build();
            return map;
        });
    }

}
