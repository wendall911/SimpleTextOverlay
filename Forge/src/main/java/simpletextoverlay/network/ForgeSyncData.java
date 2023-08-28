package simpletextoverlay.network;

import java.util.function.Supplier;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import simpletextoverlay.platform.Services;

public class ForgeSyncData implements IData {

    private SyncData syncData;

    public ForgeSyncData(FriendlyByteBuf buffer) {
        syncData = new SyncData(buffer.readByteArray());
    }

    public ForgeSyncData() {
        syncData = new SyncData();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByteArray(syncData.bytes);
    }

    @OnlyIn(Dist.CLIENT)
    public void process(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> Services.CAPABILITY_PLATFORM.getDataManagerCapability(Minecraft.getInstance().player).ifPresent(data -> {
                data.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(syncData.bytes)));
            }));
        }
    }

}
