package yerova.botanicpledge.common.blocks.block_entities.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.moddingx.libx.base.tile.BlockEntityBase;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.mana.ThrottledPacket;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.IAuraNode;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceTransportableImplementation;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPEssences;

import java.util.ArrayList;

public class EssenceExtractorBlockEntity extends EssenceCapableBlockEntityBase implements ManaReceiver, ThrottledPacket, Wandable {

    private static final String TAG_MANA = "mana";
    public static final int MAX_MANA = 10_000;
    private int mana;

    public static int MANA_CONSUMPTION = 100;


    public static final int EXTRACTION_COOLDOWN = 20;
    public static final int MAX_EXTRACTION_COOLDOWN = 80;
    public boolean isOnCoolDown = false;
    public static final String IS_ON_COOLDOWN_KEY = "is_on_cooldown";

    public static final String PREVIOUS_ESSENCE_KEY = "previous_essence";
    public Essence previousEssence;


    public static final int EXTRACTION_RANGE = 8;
    public static final int EXTRACTION_CHECKS_MAX = 10;


    public EssenceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(BPBlockEntities.ESSENCE_EXTRACTOR.get(),pos, state);

        transportable.getTransferTypeMap().clear();
        transportable.addIO(Direction.DOWN, EssenceTransportableImplementation.TransferType.OUT);

        capacitor.setMaxCapacity(10);

    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, EssenceExtractorBlockEntity entity) {
        entity.essenceTick(level, blockPos, blockState, entity);
        int color = 0x08e8de;

        float r = (color >> 16 & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;

        spawnAbsorbingSphere(level, entity.getBlockPos().getCenter().add(0,0.125,0),2, 0.4f, 0.1f,r,g,b);



        if (level.getGameTime() % EXTRACTION_COOLDOWN == 0) {
            if (entity.isOnCoolDown) {
                entity.isOnCoolDown = false;
            }
        }

        // Nur extrahieren, wenn kein Cooldown aktiv ist
        if (!entity.isOnCoolDown) {

            for (int i = 0; i < EXTRACTION_CHECKS_MAX; i++) {

                BlockEntity potentialBE = level.getBlockEntity(AuraNodeBlockEntity.getRandomSurfaceBlock(level, blockPos,EXTRACTION_RANGE));
                if (potentialBE instanceof IAuraNode node) {
                    AuraImplementation imp = node.getImplementation();

                    if (imp == null) continue;
                    if (imp.getBaseEssence() == null || imp.getBaseEssence().equals(BPEssences.EMPTY_ESSENCE.get()) || imp.hasEssence(BPEssences.EMPTY_ESSENCE.get())) continue;

                    if (entity.previousEssence == null
                            || entity.previousEssence.equals(BPEssences.EMPTY_ESSENCE.get())
                            || !imp.hasEssence(entity.previousEssence)) {
                        entity.previousEssence = getRandomAuraNodeEssence(imp, level.random);
                        potentialBE.setChanged();
                        level.sendBlockUpdated(blockPos, potentialBE.getBlockState(), potentialBE.getBlockState(), 3);
                    }

                    if (entity.previousEssence == null || entity.previousEssence.equals(BPEssences.EMPTY_ESSENCE.get())) continue;

                    if(entity.mana <= MANA_CONSUMPTION) return;


                    if(imp.removeEssence(entity.previousEssence, 1) && entity.capacitor.addEssence(entity.previousEssence, 1)) {
                        entity.mana -= MANA_CONSUMPTION;
                        if (level.isClientSide) renderEssenceBurst(level, Vec3.atCenterOf(potentialBE.getBlockPos()), entity.getBlockPos().getCenter().add(0,0.125,0), entity.previousEssence.color() );
                        entity.isOnCoolDown = true;
                    }

                    potentialBE.setChanged();
                    level.sendBlockUpdated(potentialBE.getBlockPos(), potentialBE.getBlockState(),potentialBE.getBlockState(),3);
                }
            }
        }

        BlockState state = level.getBlockState(entity.worldPosition);
        level.sendBlockUpdated(entity.worldPosition, state, state, 3);
        entity.setChanged();

    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        mana = compound.getInt(TAG_MANA);
        isOnCoolDown = compound.getBoolean(IS_ON_COOLDOWN_KEY);
        previousEssence = Essence.fromNBT(compound.getCompound(PREVIOUS_ESSENCE_KEY));

    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt(TAG_MANA, mana);
        tag.putBoolean(IS_ON_COOLDOWN_KEY, isOnCoolDown);
        if (previousEssence != null) {
            tag.put(PREVIOUS_ESSENCE_KEY, previousEssence.toNBT());
        }
    }

    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public Level getManaReceiverLevel() {
        return level;
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return worldPosition;
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public boolean isFull() {
        return getCurrentMana() >= MAX_MANA;
    }

    @Override
    public void receiveMana(int mana) {
        int old = this.mana;
        this.mana = Math.max(0, Math.min(getCurrentMana() + mana, MAX_MANA));
        if (old != this.mana) {
            markDispatchable();
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    @Override
    public void markDispatchable() {

    }

    @Override
    @javax.annotation.Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag() == null ? new CompoundTag() : pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public boolean updateBlock() {
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            setChanged();
            return true;
        }
        return false;
    }

    public static Essence getRandomAuraNodeEssence(AuraImplementation node, RandomSource random) {
        ArrayList<Essence> essences = new ArrayList<>(node.essenceList.getEssenceTypes().stream().filter(e -> e.equals(BPEssences.EMPTY_ESSENCE.get())).toList());
        essences.add(node.getBaseEssence());

        return essences.get(random.nextInt(essences.size()));

    }

    public static void renderEssenceBurst(Level world, Vec3 start, Vec3 end, int color) {
        if (world == null || !world.isClientSide()) return;
        if (start == null || end == null) return;


        Vec3 diff = end.subtract(start);
        double length = diff.length();
        if (length < 0.01) return;

        Vec3 dir = diff.normalize();

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        int count = (int) (length * 4);
        for (int i = 0; i < count; i++) {
            double t = (i + world.random.nextDouble()) / count;
            Vec3 pos = start.add(dir.scale(length * t));

            // Welleffekt
            double wave = Math.sin(t * Math.PI * 6 + world.getGameTime() * 0.3) * 0.15;
            Vec3 side = new Vec3(dir.z, 0, -dir.x).normalize().scale(wave);
            pos = pos.add(side);

            // leichte Zufallsabweichung
            if (world.random.nextFloat() < 0.3) {
                double rc = 0.1;
                pos = pos.add(
                        (world.random.nextDouble() - 0.5) * rc,
                        (world.random.nextDouble() - 0.5) * rc,
                        (world.random.nextDouble() - 0.5) * rc
                );
            }

            Vec3 motion = end.subtract(pos).scale(0.05 + 0.03 * t);

            float progress = (float)t;
            float r2 = r * (1.0f - 0.3f * progress);
            float g2 = g * (1.0f - 0.3f * progress);
            float b2 = b * (1.0f - 0.3f * progress);

            float pulse = (float)(0.9 + 0.2 * Math.sin(world.getGameTime() * 0.2 + t * 12));
            float size = 0.08F + 0.12F * pulse;

            WispParticleData data = WispParticleData.wisp(size, r2, g2, b2).withNoClip(true);
            world.addAlwaysVisibleParticle(data, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        }
    }

    public static void spawnAbsorbingSphere(Level level, Vec3 center, int amount, float radius, float size, float r, float g, float b) {
        if (!level.isClientSide()) return;

        RandomSource random = level.getRandom();

        for (int i = 0; i < amount; i++) {
            // Zufällige Position auf einer Kugeloberfläche
            double theta = random.nextDouble() * 2 * Math.PI;  // Winkel um Y
            double phi = Math.acos(2 * random.nextDouble() - 1); // Winkel von Z-Achse
            double x = center.x + radius * Math.sin(phi) * Math.cos(theta);
            double y = center.y + radius * Math.cos(phi);
            double z = center.z + radius * Math.sin(phi) * Math.sin(theta);

            Vec3 start = new Vec3(x, y, z);

            // Bewegungsrichtung (zum Zentrum)
            Vec3 motion = center.subtract(start).normalize().scale(0.05 + random.nextDouble() * 0.05);

            // Partikel erstellen
            WispParticleData data = WispParticleData.wisp(size, r, g, b,0.5f).withNoClip(true);

            level.addParticle(
                    data,
                    start.x, start.y, start.z,  // Startposition
                    motion.x, motion.y, motion.z // Richtung ins Zentrum
            );
        }
    }




}
