package yerova.botanicpledge.common.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.items.relic.NineRealmGlove;
import yerova.botanicpledge.setup.BPItems;

public class EssenceItem extends Item {
    public EssenceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) return super.use(pLevel, pPlayer, pUsedHand);

        ItemStack inHand = pPlayer.getItemInHand(pUsedHand);

        if (Essence.isEssence(inHand)) {
            InteractionHand gauntletHand = pUsedHand == InteractionHand.OFF_HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            ItemStack gauntletStack = pPlayer.getItemInHand(gauntletHand);

            if (gauntletStack.is(BPItems.NINE_REALMS_GLOVE.get())) {
                int amount = 1;
                if (pPlayer.isShiftKeyDown()) {
                    amount = inHand.getCount();
                }

                NineRealmGlove.addEssence(gauntletStack, Essence.getEssence(inHand),amount);
                inHand.shrink(amount);
            }

        }


        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
