package yerova.botanicpledge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import yerova.botanicpledge.common.worldgen.BPBiomeModifier;
import yerova.botanicpledge.common.worldgen.BPPlacedFeatures;
import yerova.botanicpledge.common.worldgen.ConfiguredBPFeatures;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ConfiguredBPFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, BPPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BPBiomeModifier::bootstrap);

    public WorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(BotanicPledge.MOD_ID));
    }
}
