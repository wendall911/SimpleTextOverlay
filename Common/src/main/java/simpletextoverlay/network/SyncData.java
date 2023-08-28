package simpletextoverlay.network;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;

import simpletextoverlay.overlay.compass.DataManager;

public class SyncData {

    public byte[] bytes;

    public SyncData(byte[] bytes) {
        this.bytes = bytes;
    }

    public SyncData() {
        FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());

        DataManager.write(tmp);

        bytes = new byte[tmp.readableBytes()];

        tmp.readBytes(bytes, 0, bytes.length);
    }

}
