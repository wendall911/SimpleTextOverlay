package simpletextoverlay.network;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.CorpseHelper;

public class NeoForgeNetworkManager {

    private static final NeoForgeNetworkManager INSTANCE = new NeoForgeNetworkManager();

    public static NeoForgeNetworkManager getInstance() {
        return INSTANCE;
    }

    public void processSyncData(NeoForgeSyncData msgData, IPayloadContext ctx) {
        if (ctx.player().isPresent()) {
            Player player = ctx.player().get();
            ctx.workHandler().submitAsync(() -> Services.CAPABILITY_PLATFORM.getDataManagerCapability(player).ifPresent(data -> {
                data.read(player, new FriendlyByteBuf(Unpooled.wrappedBuffer(msgData.getSyncData().bytes)));
            }));
        }
        else {
            /*
             * This is really, really stupid. So far NeoForge stuff has been great, this is just garbage. If I send
             * ServerPlayer, it shouldn't be optional! Not sure who made that poor choice, but it happened. Now I'm stuck
             * dubugging this shit becuase it is poorly implemented. Faster != having to send the data twice because you
             * can't give a correct context the first time.
             */
            ctx.workHandler().submitAsync(() -> PacketDistributor.SERVER.noArg().send(new RequestSyncData()));
        };
    }

    public void processRequestSyncData(RequestSyncData msgData, IPayloadContext ctx) {
        ctx.player().ifPresent(player -> ctx.workHandler().submitAsync(() -> RequestSyncData.sync(player)));
    }

    public void processOpenHistory(OpenHistory msgData, IPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> CorpseHelper.openDeathHistoryScreen(msgData.getDeaths()));
    }

    public void processRequestDeathHistory(RequestDeathHistory msgData, IPayloadContext ctx) {
        ctx.player().ifPresent(player -> ctx.workHandler().submitAsync(() -> RequestDeathHistory.sync(player)));
    }

    public void processSetDeathLocation(SetDeathLocation msgData, IPayloadContext ctx) {
        ctx.player().ifPresent(player -> ctx.workHandler().submitAsync(() -> SetDeathLocation.sync(player, msgData.getDeath())));
    }

}
