package yerova.botanicpledge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.item.relic.RingOfLokiItem;
import yerova.botanicpledge.common.items.relic.RingOfAesir;

@Mixin(BotaniaAPIImpl.class)
public class MixinBotaniaApi {

    @Inject(at = @At(value = "HEAD"), method = "breakOnAllCursors", remap = false)
    public void breakOnAllCursors(Player player, ItemStack stack, BlockPos pos, Direction side, CallbackInfo ci) {
        if (!RingOfAesir.getAesirRing(player).isEmpty()) {
            RingOfAesir.breakOnAllCursors(player, stack, pos, side);
        } else {
            RingOfLokiItem.breakOnAllCursors(player, stack, pos, side);
        }
    }

}
