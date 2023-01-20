package yerova.botanicpledge.common.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerUtils {
    public static boolean removeItemFromInventory(Player player, ItemStack stack, int amount){
        if(!player.getInventory().contains(stack)) return false;
        if(stack.getCount()<amount) return false;

        if(stack.getCount() == amount) {
            player.getInventory().removeItem(stack);
            return true;
        }

        if(stack.getCount() > amount) {
            for (ItemStack s : player.getInventory().items) {
                if (s.equals(stack, true)) {
                    s.setCount(s.getCount() - amount);
                    return true;
                }
            }
        }
        return false;
    }
}
