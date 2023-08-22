package simpletextoverlay.network;

import java.util.function.Supplier;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import simpletextoverlay.capabilities.CapabilityRegistry;
import simpletextoverlay.overlay.compass.DataManager;

public class SyncData implements IData {

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

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByteArray(bytes);
    }

    public void process(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            LocalPlayer player = Minecraft.getInstance().player;

            if (player != null) {
                ctx.get().enqueueWork(() -> player.getCapability(CapabilityRegistry.DATA_MANAGER_CAPABILITY).ifPresent(data -> {
                    DataManager.handleSync(player, bytes);
                }));
            }
        }
    }

}
