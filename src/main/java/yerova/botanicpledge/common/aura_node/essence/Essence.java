package yerova.botanicpledge.common.aura_node.essence;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Essence {


    private final Item itemBase;

    private final int color; // 0xRRGGBB

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


}
