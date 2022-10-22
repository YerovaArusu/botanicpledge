package yerova.botanicpledge.common.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface LeftClickable {

    void LeftClick(Level level, Player player, ItemStack stack);


}
