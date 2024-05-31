package yerova.botanicpledge.integration.curios;

import net.minecraft.world.entity.Entity;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.List;

public class ItemHelper {
    public static List<SlotResult> getDivineCoreCurio(Entity entity) {
        return getCurio(entity, "divine_core");
    }

    public static List<SlotResult> getCurio(Entity entity, String... identifiers) {
        if (entity.getCapability(CuriosCapability.INVENTORY).isPresent()) {
            return entity.getCapability(CuriosCapability.INVENTORY).resolve().get().findCurios(identifiers);
        } else return new ArrayList<>();
    }


}
