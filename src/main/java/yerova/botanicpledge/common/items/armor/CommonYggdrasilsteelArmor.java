package yerova.botanicpledge.common.items.armor;


import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.handler.PixieHandler;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelArmorItem;
import yerova.botanicpledge.setup.BPItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
public class CommonYggdrasilsteelArmor {

    private static final List<UUID> ARMOR_ATTRIBUTE_SLOT_UIDS = List.of(
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
    );

    public static Multimap<Attribute, AttributeModifier> applyModifiers(TerrasteelArmorItem item, Multimap<Attribute, AttributeModifier> map, @Nullable EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> ret = LinkedHashMultimap.create(map);

        ret.removeAll(Attributes.ARMOR);
        ret.removeAll(Attributes.ARMOR_TOUGHNESS);
        ret.removeAll(Attributes.KNOCKBACK_RESISTANCE);
        if (slot == item.getType().getSlot()) {
            ret.put(Attributes.ARMOR, new AttributeModifier(ARMOR_ATTRIBUTE_SLOT_UIDS.get(slot.getIndex()), "Armor modifier", item.getDefense(), AttributeModifier.Operation.ADDITION));
            ret.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_ATTRIBUTE_SLOT_UIDS.get(slot.getIndex()), "Armor toughness", item.getToughness(), AttributeModifier.Operation.ADDITION));
            ret.put(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Armor modifier", 0.25));


            @SuppressWarnings("ConstantConditions")
            UUID uuid = new UUID(ForgeRegistries.ITEMS.getKey(item).hashCode() + slot.name().hashCode(), 0L);
            if (item == BPItems.YGGDRASIL_HELMET.get()) {
                Attribute reachDistance = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "reach_distance"));
                if (reachDistance != null)
                    ret.put(reachDistance, new AttributeModifier(uuid, "Yggdrasilsteel modifier " + item.type, 8.0f, AttributeModifier.Operation.ADDITION));
            } else if (item == BPItems.YGGDRASIL_CHESTPLATE.get()) {
                ret.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Yggdrasilsteel modifier " + item.type, 0.8f, AttributeModifier.Operation.ADDITION));
            } else if (item == BPItems.YGGDRASIL_LEGGINGS.get()) {
                ret.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Yggdrasilsteel modifier " + item.type, 0.3f, AttributeModifier.Operation.ADDITION));
                Attribute swimSpeed = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "swim_speed"));
                if (swimSpeed != null) {
                    @SuppressWarnings("ConstantConditions")
                    UUID uuid2 = new UUID(ForgeRegistries.ITEMS.getKey(item).hashCode() + slot.name().hashCode(), 1L);
                    ret.put(swimSpeed, new AttributeModifier(uuid2, "Yggdrasilsteel modifier swim " + item.type, 0.3f, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        return ret;
    }

    public static void addArmorSetDescription(ItemStack stack, List<Component> list) {
        if (stack.getItem() == BPItems.YGGDRASIL_HELMET.get()) {
            list.add(Component.translatable("item.botanicpledge.yggdrasilsteel_helmet.description").withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == BPItems.YGGDRASIL_CHESTPLATE.get()) {
            list.add(Component.translatable("item.botanicpledge.yggdrasilsteel_chestplate.description").withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == BPItems.YGGDRASIL_LEGGINGS.get()) {
            list.add(Component.translatable("item.botanicpledge.yggdrasilsteel_leggings.description").withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == BPItems.YGGDRASIL_BOOTS.get()) {
            list.add(Component.translatable("item.botanicpledge.yggdrasilsteel_boots.description").withStyle(ChatFormatting.GOLD));
        }
    }
    public static boolean hasArmorSet(Player player) {
        return hasArmorSetItem(player, EquipmentSlot.HEAD) && hasArmorSetItem(player, EquipmentSlot.CHEST) && hasArmorSetItem(player, EquipmentSlot.LEGS) && hasArmorSetItem(player, EquipmentSlot.FEET);
    }

    public static boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
        if (player == null) {
            return false;
        } else {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) {
                return false;
            } else {
                return switch (slot) {
                    case HEAD -> stack.getItem() == BPItems.YGGDRASIL_HELMET.get();
                    case CHEST -> stack.getItem() == BPItems.YGGDRASIL_CHESTPLATE.get();
                    case LEGS -> stack.getItem() == BPItems.YGGDRASIL_LEGGINGS.get();
                    case FEET -> stack.getItem() == BPItems.YGGDRASIL_BOOTS.get();
                    default -> false;
                };
            }
        }
    }

    public static int getDefense(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 5;
            case CHESTPLATE -> 13;
            case LEGGINGS -> 7;
            case BOOTS -> 5;
        };
    }

    public static float getToughness(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 3;
            case CHESTPLATE -> 10;
            case BOOTS -> 3;
            case LEGGINGS -> 4;
        };
    }
}
