package yerova.botanicpledge.client.model;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.Map;
import java.util.function.Consumer;


public class ModelBakery {

    public static BakedModel asgardBlade;
    public static BakedModel vedrfolnirWings;
    public static BakedModel yggralSpreaderCore;
    public static BakedModel yggdralSpreaderScaffolding;

    public static final Material TERRA_SHIELD = new Material(Sheets.SHIELD_SHEET, new ResourceLocation(BotanicPledge.MOD_ID, "entity/shield/terra_shield"));
    public static final Material MANA_SHIELD = new Material(Sheets.SHIELD_SHEET, new ResourceLocation(BotanicPledge.MOD_ID, "entity/shield/mana_shield"));

    public static void onModelRegister(ResourceManager rm, Consumer<ResourceLocation> consumer) {
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "icon/asgard_blade"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "icon/vedrfolnir_wings"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "block/yggdral_spreader_core"));
        consumer.accept(new ResourceLocation(BotanicPledge.MOD_ID, "block/yggdral_spreader_scaffolding"));


    }

    public static void onModelBake(net.minecraft.client.resources.model.ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
        asgardBlade = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "icon/asgard_blade"));
        vedrfolnirWings = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "icon/vedrfolnir_wings"));
        yggralSpreaderCore = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "block/yggdral_spreader_core"));
        yggdralSpreaderScaffolding = map.get(new ResourceLocation(BotanicPledge.MOD_ID, "block/yggdral_spreader_scaffolding"));
    }

}
