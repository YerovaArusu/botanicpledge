package yerova.botanicpledge.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;

import java.util.List;

public class EssenceOfWillItem extends Item {

    public EssenceOfWillItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent(
                "while holding 16 of these in the mainhand \n" +
                        " and holding a Core item in the off hand \n " +
                        "Shift Right Click to Rank Up the Core"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            if (pPlayer.getOffhandItem().getItem() instanceof DivineCoreItem && DivineCoreItem.getCoreRank(pPlayer.getOffhandItem()) < BPConstants.MAX_CORE_RANK &&
                    pPlayer.getMainHandItem().getItem() instanceof EssenceOfWillItem && pPlayer.getMainHandItem().getCount() >= 16) {
                DivineCoreItem.setCoreRank(pPlayer.getOffhandItem(), DivineCoreItem.getCoreRank(pPlayer.getOffhandItem()) + 1);
                if (PlayerUtils.removeItemFromInventory(pPlayer, pPlayer.getMainHandItem(), 16)) {
                    pPlayer.sendMessage(new TextComponent("Rank Upgraded"), pPlayer.getUUID());
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
