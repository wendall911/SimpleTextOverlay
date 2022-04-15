package simpletextoverlay.network;

import java.util.function.Supplier;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.network.NetworkEvent;

import simpletextoverlay.client.DataHandler;
import simpletextoverlay.overlay.compass.DataManager;

public class SyncData {

    public byte[] bytes;

    public SyncData(FriendlyByteBuf buffer) {
        bytes = buffer.readByteArray();
    }

    public SyncData(DataManager data) {
        FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());

        data.write(tmp);

        bytes = new byte[tmp.readableBytes()];

        tmp.readBytes(bytes, 0, bytes.length);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByteArray(bytes);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DataHandler.handleSync(bytes));

        return true;
    }

}
