package yerova.botanicpledge.integration.expanded_combat;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.userofbricks.expanded_combat.api.material.Material;
import com.userofbricks.expanded_combat.api.material.MaterialBuilder;
import com.userofbricks.expanded_combat.api.registry.ECPlugin;
import com.userofbricks.expanded_combat.api.registry.IExpandedCombatPlugin;
import com.userofbricks.expanded_combat.api.registry.RegistrationHandler;
import com.userofbricks.expanded_combat.item.ECGauntletItem;
import com.userofbricks.expanded_combat.item.ECHammerWeaponItem;
import com.userofbricks.expanded_combat.item.ECKatanaItem;
import com.userofbricks.expanded_combat.item.ECWeaponItem;
import com.userofbricks.expanded_combat.plugins.VanillaECPlugin;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.resources.ResourceLocation;
import yerova.botanicpledge.config.BPConfig;
import yerova.botanicpledge.setup.BotanicPledge;


@ECPlugin
public class ExpandedCombatPlugin implements IExpandedCombatPlugin {

    public static final NonNullSupplier<Registrate> EC_REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(BotanicPledge.MOD_ID));


    public static Material YGGDRASILSTEEL;



    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BotanicPledge.MOD_ID, "expanded_combat");
    }

    @Override
    public void registerMaterials(RegistrationHandler registrationHandler) {
        BotanicPledge.CONFIG = AutoConfig.getConfigHolder(BPConfig.class).getConfig();

        YGGDRASILSTEEL = registrationHandler.registerMaterial(new MaterialBuilder(EC_REGISTRATE, "yggdrasilsteel", BotanicPledge.CONFIG.yggdrasilsteel)
                .gauntlet((Material) null, BPGauntletItem::new).build(false)
                .weaponBuilder(VanillaECPlugin.BATTLE_STAFF, null, ECWeaponItem.Dyeable::new).build()
                .weaponBuilder(VanillaECPlugin.BROAD_SWORD, null, ECWeaponItem.Dyeable::new).build()
                .weaponBuilder(VanillaECPlugin.CLAYMORE, null, ECWeaponItem.Dyeable::new).build()
                .weaponBuilder(VanillaECPlugin.CUTLASS, null, ECWeaponItem::new).build()
                .weaponBuilder(VanillaECPlugin.DAGGER, null, ECWeaponItem::new).build()
                .weaponBuilder(VanillaECPlugin.DANCERS_SWORD, null, ECWeaponItem.Dyeable::new).build()
                .weaponBuilder(VanillaECPlugin.FLAIL, null, ECWeaponItem::new).build()
                .weaponBuilder(VanillaECPlugin.GLAIVE, null, ECWeaponItem.Dyeable::new).build()
                .weaponBuilder(VanillaECPlugin.GREAT_HAMMER, null, (m, w, p) -> new ECHammerWeaponItem(m, p)).build()
                .weaponBuilder(VanillaECPlugin.KATANA, null, (m, w, p) -> new ECKatanaItem(m, p)).build()
                .weaponBuilder(VanillaECPlugin.MACE, null, ECWeaponItem::new).build()
                .weaponBuilder(VanillaECPlugin.SCYTHE, null, ECWeaponItem.HasPotion::new).build()
                .weaponBuilder(VanillaECPlugin.SICKLE, null, ECWeaponItem::new).build()
                .weaponBuilder(VanillaECPlugin.SPEAR, null, ECWeaponItem::new).build());
    }
}
