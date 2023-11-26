package yerova.botanicpledge.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yerova.botanicpledge.common.enchantments.ManaEdgeEnchantment;
import yerova.botanicpledge.setup.BPAttributes;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow public abstract void setTag(@org.jetbrains.annotations.Nullable CompoundTag p_41752_);

    @Shadow @Nullable public abstract CompoundTag getTag();

    @Inject(at = @At(value = "HEAD"), method = "getTooltipLines", cancellable = true)
    private void onGetTooltipLines(@Nullable Player pPlayer, TooltipFlag pIsAdvanced, CallbackInfoReturnable<List<Component>> cir) {

        ItemStack stack = (ItemStack) ((Object) this);

        List<Component> list = Lists.newArrayList();
        MutableComponent mutablecomponent = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().getStyleModifier());
        if (stack.hasCustomHoverName()) {
            mutablecomponent.withStyle(ChatFormatting.ITALIC);
        }

        list.add(mutablecomponent);
        if (!pIsAdvanced.isAdvanced() && !stack.hasCustomHoverName() && stack.is(Items.FILLED_MAP)) {
            Integer integer = MapItem.getMapId(stack);
            if (integer != null) {
                list.add(Component.literal("#" + integer).withStyle(ChatFormatting.GRAY));
            }
        }

        int j = stack.getHideFlags();
        if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.ADDITIONAL)) {
            stack.getItem().appendHoverText(stack, pPlayer == null ? null : pPlayer.level(), list, pIsAdvanced);
        }

        if (stack.hasTag()) {
            if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.UPGRADES) && pPlayer != null) {
                ArmorTrim.appendUpgradeHoverText(stack, pPlayer.level().registryAccess(), list);
            }

            if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.ENCHANTMENTS)) {
                ItemStack.appendEnchantmentNames(list, stack.getEnchantmentTags());
            }

            if (stack.getTag().contains("display", 10)) {
                CompoundTag compoundtag = stack.getTag().getCompound("display");
                if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.DYE) && compoundtag.contains("color", 99)) {
                    if (pIsAdvanced.isAdvanced()) {
                        list.add(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", compoundtag.getInt("color"))).withStyle(ChatFormatting.GRAY));
                    } else {
                        list.add(Component.translatable("item.dyed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    }
                }

                if (compoundtag.getTagType("Lore") == 9) {
                    ListTag listtag = compoundtag.getList("Lore", 8);

                    for (int i = 0; i < listtag.size(); ++i) {
                        String s = listtag.getString(i);

                        try {
                            MutableComponent mutablecomponent1 = Component.Serializer.fromJson(s);
                            if (mutablecomponent1 != null) {
                                list.add(ComponentUtils.mergeStyles(mutablecomponent1, ItemStack.LORE_STYLE));
                            }
                        } catch (Exception exception) {
                            compoundtag.remove("Lore");
                        }
                    }
                }
            }
        }

        if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.MODIFIERS)) {
            for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                Multimap<Attribute, AttributeModifier> multimap = stack.getAttributeModifiers(equipmentslot);
                if (!multimap.isEmpty()) {
                    list.add(CommonComponents.EMPTY);
                    list.add(Component.translatable("item.modifiers." + equipmentslot.getName()).withStyle(ChatFormatting.GRAY));

                    for (Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        double d0 = attributemodifier.getAmount();
                        boolean flag = false;
                        if (pPlayer != null) {
                            if (attributemodifier.getId() == Item.BASE_ATTACK_DAMAGE_UUID) {
                                d0 += pPlayer.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                                d0 += (double) EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
                                flag = true;
                            } else if (attributemodifier.getId() == Item.BASE_ATTACK_SPEED_UUID) {
                                d0 += pPlayer.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                                flag = true;
                            } else if (attributemodifier.getId() == BPAttributes.BASE_MANA_DAMAGE_UUID) {
                                d0 += pPlayer.getAttributeBaseValue(BPAttributes.MANA_DAMAGE.get());
                                d0 += ManaEdgeEnchantment.getManaDamageBonus(stack);
                                flag = true;
                            }
                        }

                        double d1;
                        if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                            if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                                d1 = d0 * 10.0D;
                            } else {
                                d1 = d0;
                            }
                        } else {
                            d1 = d0 * 100.0D;
                        }

                        if (flag) {
                            if (attributemodifier.getId() == BPAttributes.BASE_MANA_DAMAGE_UUID) {
                                list.add(CommonComponents.space().append(Component.translatable("attribute.modifier.equals." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId()))).withStyle(ChatFormatting.DARK_PURPLE));
                            } else {
                                list.add(CommonComponents.space().append(Component.translatable("attribute.modifier.equals." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
                            }
                        } else if (d0 > 0.0D) {
                            list.add(Component.translatable("attribute.modifier.plus." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                        } else if (d0 < 0.0D) {
                            d1 *= -1.0D;
                            list.add(Component.translatable("attribute.modifier.take." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.RED));
                        }
                    }
                }
            }
        }

        if (stack.hasTag()) {
            if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.UNBREAKABLE) && stack.getTag().getBoolean("Unbreakable")) {
                list.add(Component.translatable("item.unbreakable").withStyle(ChatFormatting.BLUE));
            }

            if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.CAN_DESTROY) && stack.getTag().contains("CanDestroy", 9)) {
                ListTag listtag1 = stack.getTag().getList("CanDestroy", 8);
                if (!listtag1.isEmpty()) {
                    list.add(CommonComponents.EMPTY);
                    list.add(Component.translatable("item.canBreak").withStyle(ChatFormatting.GRAY));

                    for(int k = 0; k < listtag1.size(); ++k) {
                        list.addAll(ItemStack.expandBlockState(listtag1.getString(k)));
                    }
                }
            }

            if (ItemStack.shouldShowInTooltip(j, ItemStack.TooltipPart.CAN_PLACE) && stack.getTag().contains("CanPlaceOn", 9)) {
                ListTag listtag2 = stack.getTag().getList("CanPlaceOn", 8);
                if (!listtag2.isEmpty()) {
                    list.add(CommonComponents.EMPTY);
                    list.add(Component.translatable("item.canPlace").withStyle(ChatFormatting.GRAY));

                    for(int l = 0; l < listtag2.size(); ++l) {
                        list.addAll(ItemStack.expandBlockState(listtag2.getString(l)));
                    }
                }
            }
        }

        if (pIsAdvanced.isAdvanced()) {
            if (stack.isDamaged()) {
                list.add(Component.translatable("item.durability", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage()));
            }

            list.add(Component.literal(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
            if (stack.hasTag()) {
                list.add(Component.translatable("item.nbt_tags", this.getTag().getAllKeys().size()).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        if (pPlayer != null && !stack.getItem().isEnabled(pPlayer.level().enabledFeatures())) {
            list.add(ItemStack.DISABLED_ITEM_TOOLTIP);
        }

        net.minecraftforge.event.ForgeEventFactory.onItemTooltip(stack, pPlayer, list, pIsAdvanced);
        cir.setReturnValue(list);


    }
}
