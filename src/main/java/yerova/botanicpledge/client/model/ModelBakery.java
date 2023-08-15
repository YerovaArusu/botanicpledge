package yerova.botanicpledge.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.Map;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModelBakery {

    public static BakedModel asgardBlade;
    public static BakedModel mariaWings;
    public static BakedModel marinaWings;
    public static BakedModel yggralSpreaderCore;
    public static BakedModel yggdralSpreaderScaffolding;

    public static void onModelRegister(ResourceManager rm, Consumer<ResourceLocation> consumer) {
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "icon/asgard_blade"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "icon/maria_wings"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "icon/marina_wings"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "block/yggdral_spreader_core"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "block/yggdral_spreader_scaffolding"));

    }

    public static void onModelBake(net.minecraft.client.resources.model.ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
        asgardBlade = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "icon/asgard_blade"));
        mariaWings = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "icon/maria_wings"));
        marinaWings = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "icon/marina_wings"));
        yggralSpreaderCore = map.get(new ResourceLocation(BotanicPledge.MOD_ID,"block/yggdral_spreader_core"));
        yggdralSpreaderScaffolding = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "block/yggdral_spreader_scaffolding"));
    }

}
