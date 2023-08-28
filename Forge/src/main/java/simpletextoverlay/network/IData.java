package simpletextoverlay.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.network.NetworkEvent;

public interface IData {

    void encode(FriendlyByteBuf buf);

    void process(Supplier<NetworkEvent.Context> ctx);

}
