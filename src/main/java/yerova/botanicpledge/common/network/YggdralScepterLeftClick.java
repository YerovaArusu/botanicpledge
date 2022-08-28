package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.common.entitites.projectiles.ManaSlashEntity;

import java.util.function.Supplier;

public class YggdralScepterLeftClick {

    public YggdralScepterLeftClick() {

    }

    public YggdralScepterLeftClick(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx =contextSupplier.get();
        ServerPlayer player = ctx.getSender();
        ctx.enqueueWork(() -> {
            ManaSlashEntity slash = new ManaSlashEntity(player, player.getLevel());
            slash.setBoundingBox(new AABB(40D, 40D, 40D, 40D, 40D, 40D));
            slash.setPos(player.position().x, player.position().y +1, player.position().z);
            slash.setNoGravity(true);
            slash.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2, 0);
            player.getLevel().addFreshEntity(slash);
        });
        return ctx.getPacketHandled();
    }


}
