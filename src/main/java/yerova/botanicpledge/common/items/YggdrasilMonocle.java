package yerova.botanicpledge.common.items;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import vazkii.botania.api.item.CosmeticAttachable;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.proxy.Proxy;
import yerova.botanicpledge.client.synched.ClientSyncedValues;
import yerova.botanicpledge.setup.BPItems;

import java.util.List;

public class YggdrasilMonocle extends ManaseerMonocleItem {
    public YggdrasilMonocle(Properties props) {
        super(props);
        Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
    }

    public static class Hud {
        public static void render(GuiGraphics gui, Player player) {
            Minecraft mc = Minecraft.getInstance();

            int x = mc.getWindow().getGuiScaledWidth() / 2 -51;
            int y = mc.getWindow().getGuiScaledHeight() / 6 *5;


            gui.drawString(mc.font, "Yggdrasil Aura", x +8, y - 10, 0x0000FF);
            HUDHandler.renderManaBar(gui,x,y, 0x0000FF, 0.75F, ClientSyncedValues.getYggdrasilPower(),15);

        }
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
        System.out.println("Testing");

    }
}
