package yerova.botanicpledge.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import yerova.botanicpledge.common.blocks.block_entities.*;
import yerova.botanicpledge.common.blocks.block_entities.generating.ThunderLilyBLockEntity;

public class BPBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BotanicPledge.MOD_ID);

    public static final RegistryObject<BlockEntityType<YggdralSpreaderBlockEntity>> YGGDRAL_SPREADER =
            BLOCK_ENTITIES.register("yggdral_spreader", () ->
                    BlockEntityType.Builder.of(YggdralSpreaderBlockEntity::new, BPBlocks.YGGDRAL_SPREADER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ManaBufferBlockEntity>> MANA_BUFFER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("mana_buffer", () ->
                    BlockEntityType.Builder.of(ManaBufferBlockEntity::new, BPBlocks.MANA_BUFFER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RitualCenterBlockEntity>> RITUAL_CENTER =
            BLOCK_ENTITIES.register("ritual_center", () ->
                    BlockEntityType.Builder.of(RitualCenterBlockEntity::new, BPBlocks.RITUAL_CENTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RitualPedestalBlockEntity>> RITUAL_PEDESTAL =
            BLOCK_ENTITIES.register("ritual_pedestal", () ->
                    BlockEntityType.Builder.of(RitualPedestalBlockEntity::new, BPBlocks.RITUAL_PEDESTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ModificationAltarBlockEntity>> MODIFICATION_TABLE =
            BLOCK_ENTITIES.register("modification_table", () ->
                    BlockEntityType.Builder.of(ModificationAltarBlockEntity::new, BPBlocks.MODIFICATION_TABLE.get()).build(null));


    public static final RegistryObject<BlockEntityType<ThunderLilyBLockEntity>> THUNDER_LILY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("thunder_lily", () ->
                    BlockEntityType.Builder.of(ThunderLilyBLockEntity::new, BPBlocks.THUNDER_LILY.get()).build(null));

    public static final RegistryObject<BlockEntityType<YggdrasilPylonBlockEntity>> YGGDRASIL_PYLON =
            BLOCK_ENTITIES.register("yggdrasil_pylon",
                    () -> BlockEntityType.Builder.of(YggdrasilPylonBlockEntity::new, BPBlocks.YGGDRASIL_PYLON.get()).build(null));


    public static void registerWandHudCaps(BotaniaBlockEntities.BECapConsumer<WandHUD> consumer) {
        consumer.accept(be -> new ManaBufferBlockEntity.WandHud((ManaBufferBlockEntity) be), BPBlockEntities.MANA_BUFFER_BLOCK_ENTITY.get());
        consumer.accept(be -> new YggdralSpreaderBlockEntity.WandHud((YggdralSpreaderBlockEntity) be), BPBlockEntities.YGGDRAL_SPREADER.get());
        consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((ThunderLilyBLockEntity) be), BPBlockEntities.THUNDER_LILY_BLOCK_ENTITY.get());
    }

}
