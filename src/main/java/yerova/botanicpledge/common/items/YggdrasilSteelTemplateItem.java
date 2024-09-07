package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class YggdrasilSteelTemplateItem extends SmithingTemplateItem {
    public YggdrasilSteelTemplateItem() {
        super(Component.translatable("tooltip.botanicpledge.yggdrasilsteel_template.applies_to").withStyle(ChatFormatting.BLUE),
                Component.translatable("item.botanicpledge.yggdrasilsteel_ingot"),
                Component.translatable("tooltip.botanicpledge.yggdrasilsteel_template.upgrade").withStyle(ChatFormatting.BLUE),
                Component.translatable("tooltip.botanicpledge.yggdrasilsteel_template.slot_description", Component.translatable("tooltip.botanicpledge.yggdrasilsteel_template.slot_base")),
                Component.translatable("tooltip.botanicpledge.yggdrasilsteel_template.slot_description", Component.translatable("item.botanicpledge.yggdrasilsteel_ingot")),
                List.of(EMPTY_SLOT_SWORD, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_AXE, EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS),
                List.of(EMPTY_SLOT_INGOT)
        );
    }
}
