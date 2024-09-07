package yerova.botanicpledge.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.entity.GaiaGuardianEntity;
import yerova.botanicpledge.setup.BotanicPledge;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mixin(GaiaGuardianEntity.class)
public class MixinGaiaGuardianEntity {

    @Shadow
    private int mobSpawnTicks;

    @Shadow
    private boolean hardMode;


    //Just a slightly modified Loot Table for Gaia Guardian
    @Inject(at = @At(value = "RETURN"), method = "getDefaultLootTable", cancellable = true)
    private void onGetDefaultLootTable(CallbackInfoReturnable<ResourceLocation> cir) {

        if (mobSpawnTicks > 0) {
            cir.setReturnValue(BuiltInLootTables.EMPTY);
        }
        cir.setReturnValue(hardMode ? new ResourceLocation(BotanicPledge.MOD_ID, "gaia_guardian_2") : prefix("gaia_guardian"));
    }
}
