package simpletextoverlay.network;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import simpletextoverlay.SimpleTextOverlay;

public class NetworkManager {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(SimpleTextOverlay.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void setup() {
        INSTANCE.messageBuilder(ForgeSyncData.class, 0, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ForgeSyncData::encode)
            .decoder(ForgeSyncData::new)
            .consumer(ForgeSyncData::handle).add();
    }

}
