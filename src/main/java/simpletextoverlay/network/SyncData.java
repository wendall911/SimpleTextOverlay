package simpletextoverlay.network;

import java.util.function.Supplier;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import simpletextoverlay.client.DataHandler;
import simpletextoverlay.overlay.compass.DataManager;

public class SyncData implements IData {

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

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByteArray(bytes);
    }

    @Override
    public void process(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> DataHandler.handleSync(bytes));
        }
    }

}
