package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.blocks.block_entities.YggdrasilPylonBlockEntity;
import yerova.botanicpledge.setup.BPItems;

import java.util.List;
import java.util.Optional;

public class YggdrasilHeart extends Item {
    public YggdrasilHeart(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        var level = pContext.getLevel();
        var pos = pContext.getClickedPos();
        var itemStack = pContext.getItemInHand();

        if (level.getBlockEntity(pos) instanceof YggdrasilPylonBlockEntity pylon) {
            if (!pylon.canGenerateMana() && itemStack.is(BPItems.YGGDRASIL_HEART.get())) {
                pylon.setCanGenerateMana(true);
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.botanicpledge.yggdrasil_heart.desc").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
