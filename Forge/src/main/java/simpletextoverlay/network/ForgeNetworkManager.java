package simpletextoverlay.network;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.*;

import simpletextoverlay.SimpleTextOverlay;

public class ForgeNetworkManager {

    private static final int PROTOCOL_VERSION = 1;
    public static final SimpleChannel INSTANCE = ChannelBuilder
        .named(new ResourceLocation(SimpleTextOverlay.MODID, "main"))
        .acceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
        .networkProtocolVersion(PROTOCOL_VERSION)
        .simpleChannel();

    public static void init() {
        int id = 0;

        registerMessage(++id, ForgeSyncData.class, ForgeSyncData::new, null);

        if (ModList.get().isLoaded("corpse")) {
            RequestDeathHistory.init(++id);
            OpenHistory.init(++id);
            SetDeathLocation.init(++id);
        }

    }

    static <MSG extends IData> void registerMessage(int idx, Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder, @SuppressWarnings("SameParameterValue") @Nullable NetworkDirection direction) {
        registerMessage(idx, type, decoder, (messageCopy, contextSupplier) -> addHandling(contextSupplier, messageCopy::handle), direction);
    }

    public static <MSG extends IData> void registerMessage(int idx, Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, CustomPayloadEvent.Context> messageConsumer, @Nullable NetworkDirection direction) {
        INSTANCE.messageBuilder(type, idx, direction)
            .encoder(IData::encode)
            .decoder(decoder)
            .consumerNetworkThread(messageConsumer)
            .add();
    }

    private static void addHandling(final CustomPayloadEvent.Context context, final Runnable enqueuedWork) {
        context.enqueueWork(enqueuedWork);
        context.setPacketHandled(true);
    }

}
