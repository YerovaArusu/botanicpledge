package yerova.botanicpledge.common.blocks.block_entities.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.WispParticleData;
import yerova.botanicpledge.setup.BlockEntityInit;
import yerova.botanicpledge.common.utils.BPConstants;

public class ThunderLilyBLockEntity extends TileEntityGeneratingFlower {

    public static final int MAX_MANA = 64_000;
    public static final int COLOR_HEX = 0x0000CD;
    public static final AABB AREA = new AABB(-2,0,-2,2,1,2);
    private int burnTime = 0, cooldown = 0, cd = 0;

    public ThunderLilyBLockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.THUNDER_LILY_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (cd == 0) {
            int baseY = 64;
            if (getLevel().random.nextInt(400) == 1) {
                LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, getLevel());
                bolt.setPos(getEffectivePos().getX(), getEffectivePos().getY(), getEffectivePos().getZ());
                if(!getLevel().isClientSide)
                    getLevel().addFreshEntity(bolt);
                cd += getCooldown();
                if (cooldown == 0) {
                    burnTime += 1500;
                    if (getMana() < getMaxMana())
                        addMana(getMaxMana());
                    cooldown = getCooldown();
                }
            }
        }

        for (LightningBolt bolt : getLevel().getEntitiesOfClass(LightningBolt.class,
                new AABB(getEffectivePos().offset(AREA.minX -1, AREA.minY-1, AREA.minZ-1),
                        getEffectivePos().offset(AREA.maxX + 1, AREA.maxY + 1, AREA.maxZ + 1)))) {
            if (!bolt.isRemoved()) {
                if (cooldown == 0) {
                    burnTime += 1500;
                    if (getMana() < getMaxMana())
                        addMana(getMaxMana());
                    cooldown = getCooldown();
                    bolt.remove(Entity.RemovalReason.KILLED);
                    break;
                }
            }
        }

        if (cooldown > 0) {
            cooldown--;
            for (int i = 0; i < 3; i++) {
                WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
                level.addParticle(data, getEffectivePos().getX() + 0.5 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.5 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
            }
        }
        if (cd > 0)
            cd--;
        if (burnTime > 0)
            burnTime--;
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);
        cmp.putInt(BPConstants.FLOWER_TAG_BURN_TIME, burnTime);
        cmp.putInt(BPConstants.FLOWER_TAG_COOLDOWN, cooldown);
        cmp.putInt(BPConstants.FLOWER_TAG_CD, cd);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInt(BPConstants.FLOWER_TAG_BURN_TIME);
        cooldown = cmp.getInt(BPConstants.FLOWER_TAG_COOLDOWN);
        cd = cmp.getInt(BPConstants.FLOWER_TAG_CD);
    }

    @Override
    public int getMaxMana() {
        return MAX_MANA;
    }

    @Override
    public int getColor() {
        return COLOR_HEX;
    }

    @Nullable
    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Rectangle(getEffectivePos(), AREA);
    }

    public int getCooldown() {
        return 400;
    }



}
