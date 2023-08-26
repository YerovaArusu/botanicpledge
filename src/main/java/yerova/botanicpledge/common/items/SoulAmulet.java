package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import yerova.botanicpledge.common.utils.BPConstants;

import java.util.*;

public class SoulAmulet extends Item implements ICurioItem {
    public SoulAmulet(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return true;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            return !player.isCrouching();
        } else return false;

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pLevel != null) {
            pTooltipComponents.add(new TextComponent("Protecting:"));

            getAppliedSouls(pStack).forEach((uuid, name) -> {
                pTooltipComponents.add(new TextComponent(" - " + name).withStyle(ChatFormatting.GOLD));
            });
        }


        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pPlayer.isCrouching() && pPlayer.getMainHandItem().getItem() instanceof SoulAmulet) {

            Map.Entry<UUID, String> soul = getLastSoulAndRemoveFromList(pPlayer.getMainHandItem());

            pLevel.addFreshEntity(new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                    SoulShard.createSoulShard(soul.getKey(),soul.getValue())));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void applyShardToAmulet(ItemStack amulet, ItemStack soulShard) {
        if (!(amulet.getItem() instanceof SoulAmulet && soulShard.getItem() instanceof SoulShard)) {
            applySoul(amulet, SoulShard.getSoulUUID(soulShard), SoulShard.getSoulName(soulShard));
        }

    }


    public static HashMap<UUID, String> getAppliedSouls(ItemStack amulet) {
        HashMap<UUID, String> souls = new HashMap<>();

        if (amulet.getItem() instanceof SoulAmulet) {
            for (String s : amulet.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getAllKeys()) {

                souls.put(UUID.fromString(s), amulet.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getString(s));
            }
        }
        return souls;
    }

    public static void applySouls(HashMap<UUID, String> souls, ItemStack amulet) {
        if (amulet.getItem() instanceof SoulAmulet) {
            clearOldSouls(amulet);
            CompoundTag tag = amulet.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);

            for (UUID uuid : souls.keySet()) {
                tag.putString(uuid.toString(), souls.get(uuid));
            }
        }
    }

    public static void applySoul(ItemStack amulet, UUID uuid, String name) {
        if (amulet.getItem() instanceof SoulAmulet) {
            HashMap<UUID, String> souls = getAppliedSouls(amulet);
            souls.put(uuid, name);
            applySouls(souls, amulet);
        }
    }

    public static void applySoul(ItemStack amulet, ItemStack soulShard) {
        if (amulet.getItem() instanceof SoulAmulet && soulShard.getItem() instanceof SoulShard) {
            HashMap<UUID, String> souls = getAppliedSouls(amulet);
            souls.put(SoulShard.getSoulUUID(soulShard), SoulShard.getSoulName(soulShard));
            applySouls(souls, amulet);
        }
    }

    public static Map.Entry<UUID, String> getLastSoulAndRemoveFromList(ItemStack amulet) {
        HashMap<UUID, String> souls = getAppliedSouls(amulet);

        if (!(amulet.getItem() instanceof SoulAmulet)) return null;

        Map.Entry<UUID, String> toReturn = null;
        if (!souls.isEmpty()) {
            List<UUID> uuidsString = souls.keySet().stream().sorted(Collections.reverseOrder()).toList();
            toReturn = Map.entry(uuidsString.get(0), souls.get(uuidsString.get(0)));
            souls.remove(uuidsString.get(0));
            applySouls(souls, amulet);
        }

        return toReturn;
    }

    public static boolean amuletContainsUUID(ItemStack amulet, UUID uuid) {
        boolean toReturn = false;
        if (amulet.getItem() instanceof SoulAmulet) {
            toReturn = getAppliedSouls(amulet).containsKey(uuid);
        }
        return toReturn;
    }

    public static void clearOldSouls(ItemStack amulet) {
        if (amulet.getItem() instanceof SoulAmulet) {
            CompoundTag tag = amulet.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
            for (String s : tag.getAllKeys()) {
                tag.remove(s);
            }
        }
    }
}
