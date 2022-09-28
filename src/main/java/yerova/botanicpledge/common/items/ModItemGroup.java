package yerova.botanicpledge.common.items;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {
    public static final CreativeModeTab BOTANIC_PLEDGE_TAB = new CreativeModeTab("botanicpledge") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.YGGDRAL_PROTECTOR.get());
        }
    };
}
