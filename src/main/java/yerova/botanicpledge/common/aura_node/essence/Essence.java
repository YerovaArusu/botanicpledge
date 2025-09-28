package yerova.botanicpledge.common.aura_node.essence;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yerova.botanicpledge.setup.BPEssences;

import java.util.*;
import java.util.stream.Collectors;

import static yerova.botanicpledge.setup.BPEssences.ESSENCES;

/**
 * @param color 0xRRGGBB
 */
public record Essence(Item itemBase, int color) {
    private static final Random RANDOM = new Random();

    public static boolean isEssence(ItemStack stack) {
        return getRegisteredEssences().stream().anyMatch(essence -> essence.itemBase().equals(stack.getItem()));
    }


    public static Essence getRandomEssence() {
        List<Essence> essenceList = getRegisteredEssences().stream().filter(essence -> !essence.equals(BPEssences.EMPTY_ESSENCE.get())).toList();
        if (essenceList.isEmpty()) {
            return null;
        }
        return essenceList.get(RANDOM.nextInt(essenceList.size()));
    }

    public static Essence getRandomEssence(Essence... essencesToExclude) {
        Set<Essence> toExclude = Arrays.stream(essencesToExclude).collect(Collectors.toSet());
        toExclude.add(BPEssences.EMPTY_ESSENCE.get());

        List<Essence> essenceList = getRegisteredEssences().stream().filter(essence -> !toExclude.contains(essence)).toList();
        if (essenceList.isEmpty()) {
            return null;
        }
        return essenceList.get(RANDOM.nextInt(essenceList.size()));
    }

    public static Essence getEssence(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return BPEssences.EMPTY_ESSENCE.get();
        Item targetItem = stack.getItem();
        return getEssence(targetItem);
    }


    public static Essence getEssence(Item item) {
        Essence e = getRegisteredEssences().stream()
                .filter(essence -> essence.itemBase() == item) // use == for identity comparison
                .findFirst()
                .orElse(BPEssences.EMPTY_ESSENCE.get());

        return e;
    }

    public static List<Essence> getRegisteredEssences() {
        return ESSENCES.getEntries().stream().map(RegistryObject::get).toList();
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemBase);
        if (itemId != null) {
            tag.putString("Item", itemId.toString());
        }
        tag.putInt("Color", color);
        return tag;
    }

    public static Essence fromNBT(CompoundTag tag) {
        if (!tag.contains("Item") || !tag.contains("Color")) {
            return BPEssences.EMPTY_ESSENCE.get();
        }

        ResourceLocation itemId = new ResourceLocation(tag.getString("Item"));
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        int color = tag.getInt("Color");

        if (item == null) {
            return BPEssences.EMPTY_ESSENCE.get();
        }
        return getEssence(item);
    }

    @Override
    public @NotNull String toString() {
        return "Essence{" +
                "itemBase=" + itemBase +
                ", color=" + color +
                '}';
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Essence other)) return false;
        return this.color == other.color &&
                this.itemBase.equals(other.itemBase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemBase, color);
    }

}
