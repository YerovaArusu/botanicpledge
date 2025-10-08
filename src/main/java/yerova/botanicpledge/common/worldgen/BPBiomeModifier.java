package yerova.botanicpledge.common.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import yerova.botanicpledge.setup.BotanicPledge;

public class BPBiomeModifier {

    public static final ResourceKey<BiomeModifier> ADD_YGGDRASIL_TREE = registerKey("add_yggdrasil_tree");
    public static final ResourceKey<BiomeModifier> ADD_AURA_NODE = registerKey("add_aura_node");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);


        context.register(ADD_YGGDRASIL_TREE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_MOUNTAIN),
                HolderSet.direct(placedFeatures.getOrThrow(BPPlacedFeatures.YGGDRASIL_TREE)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_AURA_NODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(BPPlacedFeatures.AURA_NODE)),
                GenerationStep.Decoration.VEGETAL_DECORATION // oder VEGETAL_DECORATION
        ));



    }


    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(BotanicPledge.MOD_ID, name));
    }
}
