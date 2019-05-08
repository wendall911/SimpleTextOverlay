package simpletextoverlay.network.message;

import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import simpletextoverlay.client.gui.overlay.OverlayManager;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.tag.Tag;

public class ServerValues implements IMessage, IMessageHandler<ServerValues, IMessage> {

    private long seed;
    private boolean forceDebug;
    private String tagBlacklist;

    public ServerValues() {
        seed = 0;
        forceDebug = false;
        tagBlacklist = "";
    }

    public ServerValues(long seed, boolean debug, String blacklist) {
        this.seed = seed;
        this.forceDebug = debug;
        this.tagBlacklist = blacklist;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound data = ByteBufUtils.readTag(buf);
        seed = data.getLong("seed");
        forceDebug = data.getBoolean("forceDebug");
        tagBlacklist = data.getString("blacklist");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound data = new NBTTagCompound();
        data.setLong("seed", this.seed);
        data.setBoolean("forceDebug", this.forceDebug);
        data.setString("blacklist", this.tagBlacklist);
        ByteBufUtils.writeTag(buf, data);
    }

    @Override
    public IMessage onMessage(ServerValues message, MessageContext ctx) {
        OverlayManager overlayManager = OverlayManager.INSTANCE;

        Tag.setSeed(message.seed);
        GameOverlayEventHandler.INSTANCE.forceDebug = message.forceDebug;
        overlayManager.setTagBlacklist(message.tagBlacklist.split(","));

        return null;
    }

}
