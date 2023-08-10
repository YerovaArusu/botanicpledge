package yerova.botanicpledge.setup;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.tile.ModTiles;
import yerova.botanicpledge.common.blocks.block_entities.ManaBufferBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualPedestalBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.generating.ThunderLilyBLockEntity;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BotanicPledge.MOD_ID);
    public static final RegistryObject<BlockEntityType<ManaBufferBlockEntity>> MANA_BUFFER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("mana_buffer", () ->
                    BlockEntityType.Builder.of(ManaBufferBlockEntity::new, BlockInit.MANA_BUFFER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RitualCenterBlockEntity>> RITUAL_CENTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ritual_center", () ->
                    BlockEntityType.Builder.of(RitualCenterBlockEntity::new, BlockInit.RITUAL_CENTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RitualPedestalBlockEntity>> RITUAL_PEDESTAL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ritual_pedestal", () ->
                    BlockEntityType.Builder.of(RitualPedestalBlockEntity::new, BlockInit.RITUAL_PEDESTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ThunderLilyBLockEntity>> THUNDER_LILY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("thunder_lily", () ->
                    BlockEntityType.Builder.of(ThunderLilyBLockEntity::new, BlockInit.THUNDER_LILY.get()).build(null));


    public static void registerWandHudCaps(ModTiles.BECapConsumer<IWandHUD> consumer) {
        consumer.accept(be -> new ManaBufferBlockEntity.WandHud((ManaBufferBlockEntity) be), BlockEntityInit.MANA_BUFFER_BLOCK_ENTITY.get());
        consumer.accept(be -> new TileEntityGeneratingFlower.GeneratingWandHud<>((ThunderLilyBLockEntity) be), BlockEntityInit.THUNDER_LILY_BLOCK_ENTITY.get());
    }

}
