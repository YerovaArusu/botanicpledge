package yerova.botanicpledge.common.entitites;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;
import yerova.botanicpledge.common.entitites.projectiles.EntityCorruptMagicMissile;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocus;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, BotanicPledge.MOD_ID);

    public static final RegistryObject<EntityType<EntityCorruptMagicMissile>> CORRUPTED_MISSILE = ENTITY.register("corrupted_missile",
            () -> EntityType.Builder.of(EntityCorruptMagicMissile::getRegistry, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(new ResourceLocation(BotanicPledge.MOD_ID, "corrupted_missile").toString()));

    public static final RegistryObject<EntityType<MarinaEntity>> MARINA = ENTITY.register("marina",
            () -> EntityType.Builder.of(MarinaEntity::getRegistry, MobCategory.MONSTER)
                    .sized(1f, 2f)
                    .build(new ResourceLocation(BotanicPledge.MOD_ID, "marina").toString()));

    public static final RegistryObject<EntityType<YggdFocus>> YGGD_FOCUS = ENTITY.register("yggd_focus",
            () -> EntityType.Builder.of(YggdFocus::getRegistry, MobCategory.MISC)
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

}
