package yerova.botanicpledge.common.entitites;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;
import yerova.botanicpledge.common.entitites.projectiles.EntityCorruptMagicMissile;
import yerova.botanicpledge.common.entitites.projectiles.ManaSlashEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, BotanicPledge.MOD_ID);

    public static final RegistryObject<EntityType<ManaSlashEntity>> MANA_SLASH = ENTITY.register("mana_slash",
            () -> EntityType.Builder.of(ManaSlashEntity::getRegistry, MobCategory.MISC)
                    .sized(6f, 1f)
                    .build(new ResourceLocation(BotanicPledge.MOD_ID, "mana_slash").toString()));

    public static final RegistryObject<EntityType<EntityCorruptMagicMissile>> CORRUPTED_MISSILE = ENTITY.register("corrupted_missile",
            () -> EntityType.Builder.of(EntityCorruptMagicMissile::getRegistry, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(new ResourceLocation(BotanicPledge.MOD_ID, "corrupted_missile").toString()));

    public static final RegistryObject<EntityType<MarinaEntity>> MARINA = ENTITY.register("marina",
            () -> EntityType.Builder.of(MarinaEntity::getRegistry, MobCategory.MONSTER)
                    .sized(1f, 2f)
                    .build(new ResourceLocation(BotanicPledge.MOD_ID, "marina").toString()));
}
