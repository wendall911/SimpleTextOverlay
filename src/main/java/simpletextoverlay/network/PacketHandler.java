package simpletextoverlay.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import simpletextoverlay.network.message.MessageSeed;
import simpletextoverlay.reference.Reference;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    public static void initClient() {
        INSTANCE.registerMessage(MessageSeed.class, MessageSeed.class, 0, Side.CLIENT);
    }

    @SideOnly(Side.SERVER)
    public static void initServer() {
        INSTANCE.registerMessage(MessageSeed.class, MessageSeed.class, 0, Side.SERVER);
    }
}
