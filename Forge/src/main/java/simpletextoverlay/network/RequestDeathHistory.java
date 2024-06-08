package simpletextoverlay.network;

import java.util.List;
import java.util.function.Supplier;

import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.event.network.CustomPayloadEvent;

import net.minecraftforge.network.PacketDistributor;

public class RequestDeathHistory implements IData {

    public RequestDeathHistory() {}

    public RequestDeathHistory(FriendlyByteBuf buf) {}
    
    public static void init(int idx) {
        ForgeNetworkManager.registerMessage(idx, RequestDeathHistory.class, RequestDeathHistory::new, null);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public void process(Supplier<CustomPayloadEvent.Context> ctx) {
        ServerPlayer sp = ctx.get().getSender();

        if (sp != null) {
            ctx.get().enqueueWork(() -> sendDeathHistory(sp));
        }
    }

    @Override
    public void handle() {}

    public static void sendDeathHistory(ServerPlayer sp) {
        List<Death> deaths = DeathManager.getDeaths((ServerLevel) sp.level(), sp.getUUID());

        if (deaths != null) {
            ForgeNetworkManager.INSTANCE.send(
                new OpenHistory(deaths),
                PacketDistributor.PLAYER.with(sp)
            );
        }
    }

}
