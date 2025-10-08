package yerova.botanicpledge.common.blocks.block_entities.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.block_entity.mana.ThrottledPacket;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceTransportableImplementation;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static yerova.botanicpledge.common.blocks.block_entities.essence.EssenceExtractorBlockEntity.renderEssenceBurst;

public class EssenceCondenserBlockEntity extends EssenceCapableBlockEntityBase implements Container, ManaReceiver, ThrottledPacket, Wandable {
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    public ItemStack heldStack = ItemStack.EMPTY;
    public ItemEntity entity;

    private static final String TAG_MANA = "mana";
    public static final int MAX_MANA = 10_000;
    private int mana;

    public static int CONDENSER_MANA_CONSUMPTION = 1000;

    public int currentCondensingCooldown = 0;
    public static final int CONDENSING_COOLDOWN = 60;
    public static final String CONDENSING_COOLDOWN_TAG = "condensing_cooldown";

    public Map<Direction, Boolean> connections = new HashMap<>();

    public EssenceCondenserBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BPBlockEntities.ESSENCE_CONDENSER.get(), pPos, pBlockState);

        transportable.getTransferTypeMap().clear();

        capacitor.setMaxCapacity(10);

        transportable.addIO(Direction.NORTH, EssenceTransportableImplementation.TransferType.IN);
        transportable.addIO(Direction.SOUTH, EssenceTransportableImplementation.TransferType.IN);
        transportable.addIO(Direction.WEST, EssenceTransportableImplementation.TransferType.IN);
        transportable.addIO(Direction.EAST, EssenceTransportableImplementation.TransferType.IN);

        connections.put(Direction.NORTH, false);
        connections.put(Direction.SOUTH, false);
        connections.put(Direction.WEST, false);
        connections.put(Direction.EAST, false);

    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, EssenceCondenserBlockEntity entity) {
        entity.essenceTick(level,blockPos,blockState,entity);
        if (entity.mana > 0) {
            renderEssenceBurst(level,entity.getBlockPos().getCenter().add(0,0.5,0),entity.getBlockPos().getCenter().add(0,-0.5,0), 0x08e8de);
        }


        entity.connections.replaceAll((d, v) -> false);

        for (Direction direction : entity.connections.keySet()) {
            BlockPos connectorPos = blockPos.relative(direction);

            if (level.getBlockEntity(connectorPos) instanceof EssenceTransporterBlockEntity transporterBlockEntity) {
                if (connectorPos.relative(transporterBlockEntity.getFacing().getOpposite()).equals(blockPos)) {
                    entity.connections.put(direction, true);
                }
            }
        }

        if (entity.currentCondensingCooldown == 0) {
            Essence essence = entity.capacitor.getFirstEssence();
            if (essence != null && entity.heldStack.getCount() < entity.getContainerSize()) {
                if (essence.itemBase().equals(entity.heldStack.getItem())) {
                    if (entity.capacitor.removeEssence(essence, 1) && entity.getCurrentMana() >= CONDENSER_MANA_CONSUMPTION) {
                        entity.receiveMana(-CONDENSER_MANA_CONSUMPTION);
                        entity.heldStack.grow(1);
                        entity.currentCondensingCooldown = CONDENSING_COOLDOWN;
                    }

                } else if (entity.heldStack.isEmpty()) {
                    if (entity.capacitor.removeEssence(essence, 1) && entity.getCurrentMana() >= CONDENSER_MANA_CONSUMPTION) {
                        entity.receiveMana(-CONDENSER_MANA_CONSUMPTION);
                        entity.heldStack = new ItemStack(essence.itemBase(), 1);
                        entity.currentCondensingCooldown = CONDENSING_COOLDOWN;
                    }
                }

            }

        } else if (entity.currentCondensingCooldown > 0) {
            entity.currentCondensingCooldown--;
        }

    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        mana = compound.getInt(TAG_MANA);

        heldStack = compound.contains("itemStack") ? ItemStack.of((CompoundTag) compound.get("itemStack")) : ItemStack.EMPTY;
        currentCondensingCooldown = compound.getInt(CONDENSING_COOLDOWN_TAG);

        connections.clear();
        if (compound.contains("Connections")) {
            CompoundTag connTag = compound.getCompound("Connections");
            for (Direction dir : Direction.values()) {
                if (connTag.contains(dir.getName())) {
                    connections.put(dir, connTag.getBoolean(dir.getName()));
                } else {
                    connections.put(dir, false);
                }
            }
        } else {
            // Falls noch nichts gespeichert war → Default false
            for (Direction dir : Direction.values()) {
                connections.put(dir, false);
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt(TAG_MANA, mana);
        tag.putInt(CONDENSING_COOLDOWN_TAG, currentCondensingCooldown);

        if (heldStack != null) {
            CompoundTag reagentTag = new CompoundTag();
            heldStack.save(reagentTag);
            tag.put("itemStack", reagentTag);
        }

        CompoundTag connTag = new CompoundTag();
        for (Map.Entry<Direction, Boolean> entry : connections.entrySet()) {
            connTag.putBoolean(entry.getKey().getName(), entry.getValue());
        }
        tag.put("Connections", connTag);
    }

    @Override
    @Nullable
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

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return heldStack == null || heldStack.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return heldStack == null ? ItemStack.EMPTY : heldStack;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack toReturn = getItem(0).copy().split(count);
        heldStack.setCount(0);
        updateBlock();
        return toReturn;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return heldStack;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack s) {
        return heldStack == null || heldStack.isEmpty();
    }

    @Override
    public void setItem(int index, ItemStack s) {
        heldStack = s;
        updateBlock();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }


    @Override
    public void clearContent() {
        this.heldStack = ItemStack.EMPTY;
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        itemHandler.invalidate();
        super.invalidateCaps();
    }

    public ItemStack getHeldStack() {
        return heldStack;
    }

    public void setHeldStack(ItemStack heldStack) {
        this.heldStack = heldStack;

        updateBlock();
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

    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        if (player == null || player.isShiftKeyDown()) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }
        return true;
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
}
