package yerova.botanicpledge.common.items;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import yerova.botanicpledge.setup.BPItems;

public class BotanicPledgeTab extends CreativeModeTab {
    public static final BotanicPledgeTab BOTANIC_PLEDGE_TAB = new BotanicPledgeTab("botanicpledge");

    public BotanicPledgeTab(String label) {
        super(builder().backgroundSuffix(label + ".png").title(Component.literal(label)));
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    @Override
    public ItemStack getIconItem() {
        return new ItemStack(BPItems.MARIAS_CORE.get());
    }


}
