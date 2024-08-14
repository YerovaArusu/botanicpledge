package yerova.botanicpledge.setup;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.entitites.projectiles.AsgardBladeEntity;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocusEntity;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;

public class BPEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BotanicPledge.MOD_ID);

    public static final RegistryObject<EntityType<YggdFocusEntity>> YGGD_FOCUS = ENTITY.register("yggd_focus",
            () -> EntityType.Builder.of(YggdFocusEntity::getRegistry, MobCategory.MISC)
                    .sized(1f, 1f)
                    .setUpdateInterval(10)
                    .setTrackingRange(64)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(""));

    public static final RegistryObject<EntityType<YggdrafoliumEntity>> YGGDRAFOLIUM = ENTITY.register("yggdrafolium",
            () -> EntityType.Builder.of(YggdrafoliumEntity::getRegistry, MobCategory.MISC)
                    .sized(1f, 1f)
                    .setUpdateInterval(10)
                    .setTrackingRange(64)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(""));

    public static final RegistryObject<EntityType<AsgardBladeEntity>> ASGARD_BLADE = ENTITY.register("yggdrasil_warden",
            () -> EntityType.Builder.of(AsgardBladeEntity::getRegistry, MobCategory.MISC)
                    .sized(1f, 1f)
                    .setUpdateInterval(1)
                    .setTrackingRange(64)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(""));


    public static final RegistryObject<EntityType<YggdrasilGuardian>> YGGDRASIL_GUARDIAN = ENTITY.register("yggdrasil_guardian",
            () -> EntityType.Builder.of(YggdrasilGuardian::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .setUpdateInterval(1)
                    .setTrackingRange(64)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("yggdrasil_guardian"));

}
