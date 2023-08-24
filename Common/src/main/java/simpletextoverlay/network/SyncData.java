package simpletextoverlay.network;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;

import simpletextoverlay.overlay.compass.DataManager;

public class SyncData {

    public byte[] bytes;

    public SyncData(FriendlyByteBuf buffer) {
        bytes = buffer.readByteArray();
    }

    public SyncData() {
        FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());

        DataManager.write(tmp);

        bytes = new byte[tmp.readableBytes()];

        tmp.readBytes(bytes, 0, bytes.length);
    }

}
