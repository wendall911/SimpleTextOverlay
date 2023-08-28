package simpletextoverlay.network;

import java.util.List;
import java.util.function.Supplier;

import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.network.NetworkEvent;

import net.minecraftforge.network.PacketDistributor;

public class RequestDeathHistory implements IData {

    public RequestDeathHistory() {}

    public RequestDeathHistory(FriendlyByteBuf buf) {}
    
    public static void init(int idx) {
        NetworkManager.registerMessage(idx, RequestDeathHistory.class, RequestDeathHistory::new);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public void process(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer sp = ctx.get().getSender();

        if (sp != null) {
            ctx.get().enqueueWork(() -> sendDeathHistory(sp));
        }
    }

    public static void sendDeathHistory(ServerPlayer sp) {
        List<Death> deaths = DeathManager.getDeaths(sp.getLevel(), sp.getUUID());

        if (deaths != null) {
            NetworkManager.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> sp),
                    new OpenHistory(deaths)
            );
        }
    }

}
