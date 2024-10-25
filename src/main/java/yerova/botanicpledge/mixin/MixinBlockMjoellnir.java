package yerova.botanicpledge.mixin;

import mythicbotany.config.MythicConfig;
import mythicbotany.mjoellnir.BlockMjoellnir;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.item.relic.RingOfThorItem;
import yerova.botanicpledge.common.items.relic.RingOfAesir;

@Mixin(BlockMjoellnir.class)
public class MixinBlockMjoellnir {

    @Unique
    private static final boolean IS_MYTHIC_BOTANY_LOADED = ModList.get().isLoaded("mythicbotany");

    @Inject(at = @At(value = "RETURN"), method = "canHold", cancellable = true, remap = false)
    private static void canHold(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!IS_MYTHIC_BOTANY_LOADED) {
            cir.cancel(); // Skip if Mythic Botany is not present
            return;
        }


        cir.setReturnValue(player.isCreative() || player.isSpectator()
                || MythicConfig.mjoellnir.requirement.test(player) || !RingOfAesir.getAesirRing(player).isEmpty()
                || !RingOfThorItem.getThorRing(player).isEmpty() && MythicConfig.mjoellnir.requirement_thor.test(player));
    }
}
