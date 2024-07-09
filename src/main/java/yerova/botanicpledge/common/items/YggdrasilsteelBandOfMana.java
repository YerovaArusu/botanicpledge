package yerova.botanicpledge.common.items;

import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.GreaterBandOfManaItem;

public class YggdrasilsteelBandOfMana extends GreaterBandOfManaItem {

    public static final int MAX_MANA = 8_000_000;


    public YggdrasilsteelBandOfMana(Properties props) {
        super(props);
    }

    public static class YggdrasilsteelManaItemImpl extends ManaItemImpl {
        public YggdrasilsteelManaItemImpl(ItemStack stack) {
            super(stack);
        }

        @Override
        public int getMaxMana() {
            return MAX_MANA * stack.getCount();
        }
    }


}
