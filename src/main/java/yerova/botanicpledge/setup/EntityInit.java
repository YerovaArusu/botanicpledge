package yerova.botanicpledge.setup;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocus;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, BotanicPledge.MOD_ID);

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
