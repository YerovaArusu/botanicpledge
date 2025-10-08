package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import vazkii.botania.common.proxy.Proxy;
import yerova.botanicpledge.setup.BPItems;

import java.util.List;

public class YggdrasilMonocle extends ManaseerMonocleItem {
    public YggdrasilMonocle(Properties props) {
        super(props);
        Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
    }

    public static boolean hasMonocle(LivingEntity living) {
        return !EquipmentHandler.findOrEmpty(stack -> {
            if (!stack.isEmpty() && stack.is(BPItems.YGGDRASIL_MONOCLE.get()))
                return true;
            return false;
        }, living).isEmpty();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        tooltip.add(Component.translatable("item.botanicpledge.yggdrasil_monocle.desc").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));

    }
}
