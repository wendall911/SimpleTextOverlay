package simpletextoverlay.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import simpletextoverlay.SimpleTextOverlay;

import java.util.function.Function;

public class NetworkManager {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(SimpleTextOverlay.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;

        registerMessage(++id, SyncData.class, SyncData::new);

        if (ModList.get().isLoaded("corpse")) {
            RequestDeathHistory.init(++id);
            OpenHistory.init(++id);
            SetDeathLocation.init(++id);
        }
    }

    public static <T extends IData> void registerMessage(int idx, Class<T> type, Function<FriendlyByteBuf, T> decoder) {
        INSTANCE.registerMessage(idx, type, IData::encode, decoder, (msg, ctx) -> {
            msg.process(ctx);
            ctx.get().setPacketHandled(true);
        });
    }

}
