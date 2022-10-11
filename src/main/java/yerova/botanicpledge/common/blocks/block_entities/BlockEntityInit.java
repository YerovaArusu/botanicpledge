package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.setup.BotanicPledge;
import yerova.botanicpledge.common.blocks.BlockInit;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BotanicPledge.MOD_ID);

    public static final RegistryObject<BlockEntityType<CoreAltarBlockEntity>> CORE_ALTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("core_alter_block_entity", () ->
                    BlockEntityType.Builder.of(CoreAltarBlockEntity::new, BlockInit.CORE_ALTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ManaYggdralBufferBlockEntity>> MANA_YGGDRAL_BUFFER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("mana_yggdral_buffer_block_entity", () ->
                    BlockEntityType.Builder.of(ManaYggdralBufferBlockEntity::new, BlockInit.MANA_YGGDRAL_BUFFER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RitualCenterBlockEntity>> RITUAL_CENTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ritual_center_block_entity", () ->
                    BlockEntityType.Builder.of(RitualCenterBlockEntity::new, BlockInit.RITUAL_CENTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RitualPedestalBlockEntity>> RITUAL_PEDESTAL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ritual_pedestal_block_entity", () ->
                    BlockEntityType.Builder.of(RitualPedestalBlockEntity::new, BlockInit.RITUAL_PEDESTAL.get()).build(null));

}
