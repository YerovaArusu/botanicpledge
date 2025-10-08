package yerova.botanicpledge.setup;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.worldgen.FloatingNodeFeature;


public class BPFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, BotanicPledge.MOD_ID);

    public static final RegistryObject<Feature<SimpleBlockConfiguration>> FLOATING_NODE =
            FEATURES.register("floating_node", () -> new FloatingNodeFeature(SimpleBlockConfiguration.CODEC));
}
