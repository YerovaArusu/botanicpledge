package yerova.botanicpledge.common.aura_node.essence;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.setup.BPEssences;

import java.util.List;
import java.util.Random;

import static yerova.botanicpledge.setup.BPEssences.ESSENCES;

public class Essence {
    private final Item itemBase;
    private final int color; // 0xRRGGBB
    private static final Random RANDOM = new Random();

    public Essence(Item itemBase, int color) {
        this.itemBase = itemBase;
        this.color = color;
    }

    public Item getItemBase() {
        return itemBase;
    }

    public int getColor() {
        return color;
    }

    public static boolean isEssence(ItemStack stack) {
        return getRegisteredEssences().stream().anyMatch(essence -> essence.getItemBase().equals(stack.getItem()));
    }


    public static Essence getRandomEssence() {
        List<Essence> essenceList = getRegisteredEssences();
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
                .filter(essence -> essence.getItemBase() == item) // use == for identity comparison
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
    public String toString() {
        return "Essence{" +
                "itemBase=" + itemBase +
                ", color=" + color +
                '}';
    }
}
