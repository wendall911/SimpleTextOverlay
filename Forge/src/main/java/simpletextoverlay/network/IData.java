package simpletextoverlay.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.event.network.CustomPayloadEvent;

public interface IData {

    void encode(FriendlyByteBuf buf);

    void process(Supplier<CustomPayloadEvent.Context> ctx);

    void handle();
}
