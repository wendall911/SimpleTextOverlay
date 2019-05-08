package simpletextoverlay.network.message;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import simpletextoverlay.util.PacketHandlerHelper;

public class ClientValues implements IMessage, IMessageHandler<ClientValues, IMessage> {

    public ClientValues() {
    }

    public ClientValues(boolean forceDebug) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(ClientValues message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        PacketHandlerHelper.sendServerValues(player);

        return null;
    }

}
