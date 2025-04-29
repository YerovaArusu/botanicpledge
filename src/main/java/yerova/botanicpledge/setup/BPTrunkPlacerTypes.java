package yerova.botanicpledge.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.worldgen.placer.YggdrasilTrunkPlacer;

public class BPTrunkPlacerTypes {

    public static final DeferredRegister<TrunkPlacerType<?>> PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, BotanicPledge.MOD_ID);

    public static final RegistryObject<TrunkPlacerType<YggdrasilTrunkPlacer>> YGGDRASIL_TRUNK_PLACER = PLACER_TYPES.register("yggdrasil_trunk_placer", () -> new TrunkPlacerType<>(YggdrasilTrunkPlacer.CODEC));
}
