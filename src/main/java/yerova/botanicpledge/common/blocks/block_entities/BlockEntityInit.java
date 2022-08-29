package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.blocks.BlockInit;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BotanicPledge.MOD_ID);

    public static final RegistryObject<BlockEntityType<CoreAltarBlockEntity>> CORE_ALTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("core_alter_block_entity", () ->
                    BlockEntityType.Builder.of(CoreAltarBlockEntity::new, BlockInit.CORE_ALTAR.get()).build(null));


}
