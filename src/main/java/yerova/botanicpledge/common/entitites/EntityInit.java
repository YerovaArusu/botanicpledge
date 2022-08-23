package yerova.botanicpledge.common.entitites;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.entitites.projectiles.ManaSlashEntity;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, BotanicPledge.MOD_ID);

    public static final RegistryObject<EntityType<ManaSlashEntity>> MANA_SLASH = ENTITY.register("mana_slash",
            () -> EntityType.Builder.of(ManaSlashEntity::getRegistry, MobCategory.MISC)
                    .sized(1f, 2f)
                    .build(new ResourceLocation(BotanicPledge.MOD_ID, "mana_slash").toString()));

}
