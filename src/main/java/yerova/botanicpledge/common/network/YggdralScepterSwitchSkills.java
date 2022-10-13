package yerova.botanicpledge.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.common.items.YggdralScepter;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.function.Supplier;

public class YggdralScepterSwitchSkills {

    private int operation = -1;

    public YggdralScepterSwitchSkills(int operation) {
        this.operation = operation;
    }

    public YggdralScepterSwitchSkills(FriendlyByteBuf buffer) {
        operation = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.operation);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {

        NetworkEvent.Context ctx = contextSupplier.get();
        ServerPlayer player = ctx.getSender();
        ctx.enqueueWork(() -> {
            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() instanceof YggdralScepter) {
                CompoundTag tag = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");
                int setValue = tag.getInt("set_skill");
                int tmp = setValue;
                if (operation == 0 && setValue > 0) {
                    setValue -= 1;
                } else if (operation == 0) {
                    setValue = 4;
                } else if (operation == 1 && setValue < 4) {
                    setValue += 1;
                } else if (operation == 1) {
                    setValue = 4;
                }
                if (setValue != tmp) {
                    tag.putInt("set_skill", setValue);
                    player.displayClientMessage(new TextComponent("Switched to" + setValue + ". skill"), true);
                }


            }

        });
        return ctx.getPacketHandled();
    }


}
