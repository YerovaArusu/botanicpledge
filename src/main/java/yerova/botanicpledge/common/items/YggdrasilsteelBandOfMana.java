package yerova.botanicpledge.common.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.equipment.bauble.GreaterBandOfManaItem;

public class YggdrasilsteelBandOfMana extends GreaterBandOfManaItem implements CustomCreativeTabContents {

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

    @Override
    public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
        output.accept(this);

        ItemStack full = new ItemStack(this);
        setMana(full, MAX_MANA);
        output.accept(full);
    }
}
