package simpletextoverlay.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import simpletextoverlay.SimpleTextOverlay;

public class NeoForgeSyncData implements CustomPacketPayload {

    private SyncData syncData;

    public static final ResourceLocation ID = new ResourceLocation(SimpleTextOverlay.MODID, "sync_data");

    public NeoForgeSyncData(FriendlyByteBuf buffer) {
        syncData = new SyncData(buffer.readByteArray());
    }

    public NeoForgeSyncData(ServerPlayer player) {
        syncData = new SyncData(player);
    }

    public SyncData getSyncData() {
        return syncData;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeByteArray(syncData.bytes);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
