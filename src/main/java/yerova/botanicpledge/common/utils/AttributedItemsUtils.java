package yerova.botanicpledge.common.utils;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.capabilities.DivineCorePlayerStats;
import yerova.botanicpledge.common.capabilities.DivineCorePlayerStatsProvider;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncProtector;
import yerova.botanicpledge.common.utils.divine_core_utils.IDivineCoreAttributes;

import java.util.UUID;


public class AttributedItemsUtils implements IDivineCoreAttributes {
    public static void handleShieldRegenOnCurioTick(LivingEntity player, ItemStack stack, int maxShield, int defRegenPerTick, int maxCharge) {
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer) player;

        CompoundTag stats = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");

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

    public static void updateDivineCoreAttributes(ServerPlayer serverPlayer) {
        for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(serverPlayer, "divine_core")) {
            setAttributeNBTs(result.stack());
        }
        for (ItemStack stack : serverPlayer.inventoryMenu.getItems()) {
            setAttributeNBTs(stack);
        }
    }

    public static CompoundTag setDefaultNBT(ItemStack stack, CompoundTag nbt, int maxShield, int maxCharge) {
        CompoundTag shield = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");

        shield.putInt("Shield", 0);
        shield.putInt("Charge", 0);

        shield.putInt("ArmorToughness", 0);
        shield.putInt("Armor", 20);
        shield.putInt("Health_Boost", 0);
        shield.putInt("KockbackResistance", 0);
        shield.putInt("DamageBonus", 0);

        shield.putInt("MaxShield", maxShield);
        shield.putInt("MaxCharge", maxCharge);


        if (nbt != null) {
            nbt.merge(shield);
        } else {
            nbt = shield;
        }

        return shield;
    }

    public static String getDivineCoreUUID(ItemStack stack) {
        return stack.getOrCreateTag().getString(NBT_UUID);
    }

    public static void setAttributeNBTs(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(BotanicPledge.MOD_ID + ".stats")) {

            String uuid = getDivineCoreUUID(stack);
            CompoundTag stats = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");


            if(!stack.getAttributeModifiers(stack.getEquipmentSlot()).containsKey(Attributes.ARMOR)) {
                stack.addAttributeModifier(Attributes.ARMOR,
                        new AttributeModifier(uuid, 10, AttributeModifier.Operation.ADDITION),stack.getEquipmentSlot());
            }

        }
    }

    public static void syncHighestPriorityStatsToCapability(ServerPlayer serverPlayer) {
        ItemStack prioStack = ItemStack.EMPTY;
        for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(serverPlayer, "divine_core", "necklace")) {
            if(result.stack().getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats").getInt("MaxCharge")
                    >= prioStack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats").getInt("MaxCharge")) {
                prioStack = result.stack();
            }
        }


        ItemStack finalPrioStack = prioStack;
        serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).ifPresent(divineCorePlayerStats -> {
            CompoundTag tag = finalPrioStack.getOrCreateTagElement(BotanicPledge.MOD_ID + "stats");
            divineCorePlayerStats.set(
                    tag.getInt("maxShield"),
                    tag.getInt("shield"),
                    tag.getInt("maxCharge"),
                    tag.getInt("charge"),
                    tag.getDouble("armorToughness"),
                    tag.getInt("armor"),
                    tag.getDouble("maxHealth"),
                    tag.getDouble("knockbackResistance"),
                    tag.getDouble("attackDamage"));
        });
    }
}
