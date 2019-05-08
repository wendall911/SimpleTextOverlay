package simpletextoverlay.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import simpletextoverlay.network.message.ClientValues;
import simpletextoverlay.network.message.ServerValues;
import simpletextoverlay.reference.Reference;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    public static void init() {
        INSTANCE.registerMessage(ServerValues.class, ServerValues.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(ClientValues.class, ClientValues.class, 1, Side.SERVER);
    }

}
