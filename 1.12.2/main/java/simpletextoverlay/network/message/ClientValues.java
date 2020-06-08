package simpletextoverlay.network.message;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import simpletextoverlay.network.PacketHandler;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.PacketHandlerHelper;
import simpletextoverlay.util.ThaumcraftHelper;

import WayofTime.bloodmagic.util.helper.NetworkHelper;

public class ClientValues implements IMessage, IMessageHandler<ClientValues, IMessage> {

    private NBTTagCompound data;

    public ClientValues() {}

    public ClientValues(NBTTagCompound data) {
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
    public IMessage onMessage(ClientValues message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        NBTTagCompound mData = message.data;
        String type;

        if (mData.hasKey("type")) {
            type = mData.getString("type");
            player.getServerWorld().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if (type.equals("config")) {
                        PacketHandlerHelper.sendServerConfigValues(player);
                    }
                    else {
                        NBTTagCompound rData = new NBTTagCompound();
                        if (type.equals("bloodmagic")) {
                            rData.setString("type", "bloodmagic");
                            rData.setLong("bmcurrentlp", NetworkHelper.getSoulNetwork(player).getCurrentEssence());
                            rData.setLong("bmorbtier", NetworkHelper.getSoulNetwork(player).getOrbTier());
                        }
                        if (type.equals("thaumcraft")) {
                            rData.setDouble("localaura", ThaumcraftHelper.getVis(player.world, player.getPosition()));
                            rData.setLong("localaurabase", ThaumcraftHelper.getAuraBase(player.world, player.getPosition()));
                            rData.setDouble("localflux", ThaumcraftHelper.getFlux(player.world, player.getPosition()));
                        }

                        try {
                            PacketHandler.INSTANCE.sendTo(new ServerValues(rData), player);
                        } catch (Exception ex) {
                            SimpleTextOverlay.logger.error("Failed to send server data!", ex);
                        }
                    }
                }
            });
        }

        return null;
    }

}
