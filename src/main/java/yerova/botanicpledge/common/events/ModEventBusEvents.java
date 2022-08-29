package yerova.botanicpledge.common.events;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.entitites.EntityInit;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(EntityInit.MARINA.get(), MarinaEntity.setAttributes());
    }
}
