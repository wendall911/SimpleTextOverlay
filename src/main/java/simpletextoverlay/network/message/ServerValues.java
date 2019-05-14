package simpletextoverlay.network.message;

import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import simpletextoverlay.client.gui.overlay.OverlayManager;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.integrations.TagBloodMagic;
import simpletextoverlay.tag.Tag;

public class ServerValues implements IMessage, IMessageHandler<ServerValues, IMessage> {

    private NBTTagCompound data;

    public ServerValues() {}

    public ServerValues(NBTTagCompound data) {
        this.data = data.copy();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }

    @Override
    public IMessage onMessage(ServerValues message, MessageContext ctx) {
        OverlayManager overlayManager = OverlayManager.INSTANCE;
        NBTTagCompound mData = message.data;

        if (mData.hasKey("type") && mData.getString("type").equals("config")) {
            Tag.setSeed(mData.getLong("seed"));
            GameOverlayEventHandler.INSTANCE.forceDebug = mData.getBoolean("forceDebug");
            overlayManager.setTagBlacklist(mData.getString("blacklist").split(","));
            TagBloodMagic.setIsLoggedIn(true);
        }
        else if (mData.hasKey("type") && mData.getString("type").equals("bloodmagic")) {
            TagBloodMagic.setLp((int) mData.getLong("bmcurrentlp"));
            TagBloodMagic.setTier((int) mData.getLong("bmorbtier"));
        }

        return null;
    }

}
