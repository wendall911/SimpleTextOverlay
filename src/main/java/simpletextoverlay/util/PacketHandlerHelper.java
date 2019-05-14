package simpletextoverlay.util;

import java.util.StringJoiner;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.network.message.ServerValues;
import simpletextoverlay.network.PacketHandler;
import simpletextoverlay.SimpleTextOverlay;

public class PacketHandlerHelper {

    public static void sendServerConfigValues(EntityPlayerMP player) {
        NBTTagCompound data = new NBTTagCompound();

        data.setString("type", "config");
        data.setLong("seed", player.world.getSeed());
        data.setBoolean("forceDebug", ConfigHandler.server.forceDebug);
        data.setString("blacklist", getBlacklistString());

        try {
            SimpleTextOverlay.logger.info("Sending server values to player.");
            PacketHandler.INSTANCE.sendTo(new ServerValues(data), player);
        } catch (Exception ex) {
            SimpleTextOverlay.logger.error("Failed to send server settings!", ex);
        }
    }

    public static String getBlacklistString() {
        StringJoiner bl = new StringJoiner(",");

        for (String s : ConfigHandler.server.blacklistTags) {
            bl.add(s);
        }

        return bl.toString();
    }

}
