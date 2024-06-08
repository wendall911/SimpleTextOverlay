package simpletextoverlay.network;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import simpletextoverlay.overlay.compass.DataManager;

public class SyncData {

    public byte[] bytes;

    public SyncData(byte[] bytes) {
        this.bytes = bytes;
    }

    public SyncData(ServerPlayer sp) {
        FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());

        DataManager.write(sp.getUUID(), tmp);

        bytes = new byte[tmp.readableBytes()];

        tmp.readBytes(bytes, 0, bytes.length);
    }

}
