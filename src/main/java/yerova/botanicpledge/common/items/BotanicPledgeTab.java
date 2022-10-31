package yerova.botanicpledge.common.items;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BotanicPledgeTab extends CreativeModeTab {

    public static final BotanicPledgeTab BOTANIC_PLEDGE_TAB = new BotanicPledgeTab("botanicpledge");

    public BotanicPledgeTab(String label) {
        super(label);
        hideTitle();
        setBackgroundSuffix(label + ".png");
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemInit.MARIAS_CORE.get());
    }
}
