package yerova.botanicpledge.common.aura_node;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuraNodeSavedData extends SavedData {

    private final Map<AuraNodeType, Set<BlockPos>> nodePositions = new EnumMap<>(AuraNodeType.class);

    public AuraNodeSavedData() {
        // Stelle sicher, dass jede NodeType immer einen Eintrag hat
        for (AuraNodeType type : AuraNodeType.values()) {
            nodePositions.put(type, new HashSet<>());
        }
    }

    public static AuraNodeSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AuraNodeSavedData::load,
                AuraNodeSavedData::new,
                "aura_node_data"
        );
    }

    public static AuraNodeSavedData load(CompoundTag tag) {
        AuraNodeSavedData data = new AuraNodeSavedData();
        CompoundTag nodesTag = tag.getCompound("Nodes");

        for (String key : nodesTag.getAllKeys()) {
            try {
                AuraNodeType type = AuraNodeType.valueOf(key);
                ListTag list = nodesTag.getList(key, Tag.TAG_COMPOUND);
                Set<BlockPos> set = data.nodePositions.computeIfAbsent(type, t -> new HashSet<>());

                for (int i = 0; i < list.size(); i++) {
                    set.add(NbtUtils.readBlockPos(list.getCompound(i)));
                }
            } catch (IllegalArgumentException e) {
                BotanicPledge.LOGGER.error("Invalid Node Type '{}' skipped when loading", key);
            }
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag nodesTag = new CompoundTag();

        for (var entry : nodePositions.entrySet()) {
            ListTag list = new ListTag();
            for (BlockPos pos : entry.getValue()) {
                list.add(NbtUtils.writeBlockPos(pos));
            }
            nodesTag.put(entry.getKey().name(), list);
        }

        tag.put("Nodes", nodesTag);
        return tag;
    }

    private Set<BlockPos> ensureSet(AuraNodeType type) {
        return nodePositions.computeIfAbsent(type, t -> new HashSet<>());
    }

    public void add(AuraNodeType type, BlockPos pos) {
        if (type == null || pos == null) return;
        ensureSet(type).add(pos);
        setDirty();
    }

    public void remove(AuraNodeType type, BlockPos pos) {
        if (type == null || pos == null) return;
        Set<BlockPos> set = nodePositions.get(type);
        if (set != null) {
            set.remove(pos);
            setDirty();
        }
    }

    public Set<BlockPos> getNodes(AuraNodeType type) {
        return ensureSet(type);
    }

    public Map<AuraNodeType, Set<BlockPos>> getAllNodes() {
        return nodePositions;
    }

    public boolean blockIsNearType(BlockPos pos, AuraNodeType type, int range) {
        Set<BlockPos> set = getNodes(type);
        if (set.isEmpty()) return false;

        double rangeSq = range * range;
        return set.stream().anyMatch(nPos -> nPos.distToCenterSqr(pos.getCenter()) < rangeSq);
    }
}
