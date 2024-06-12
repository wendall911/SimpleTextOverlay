package simpletextoverlay.network;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import simpletextoverlay.overlay.compass.DataManager;

public class NeoForgeNetworkManager {

    private static final NeoForgeNetworkManager INSTANCE = new NeoForgeNetworkManager();

    public static NeoForgeNetworkManager getInstance() {
        return INSTANCE;
    }

    public void processSyncData(NeoForgeSyncData msgData, IPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> DataManager.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(msgData.getSyncData().bytes))));
    }

}
