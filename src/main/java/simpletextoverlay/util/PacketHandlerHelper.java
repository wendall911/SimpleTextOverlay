package simpletextoverlay.util;

import java.util.StringJoiner;

import net.minecraft.entity.player.EntityPlayerMP;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.network.message.ServerValues;
import simpletextoverlay.network.PacketHandler;
import simpletextoverlay.SimpleTextOverlay;

public class PacketHandlerHelper {

    public static void sendServerValues(EntityPlayerMP player) {
        long seed = player.world.getSeed();
        boolean forceDebug = ConfigHandler.server.forceDebug;
        String blacklist = getBlacklistString();
        try {
            SimpleTextOverlay.logger.info("Sending server values to player. (onPlayerLogin)");
            PacketHandler.INSTANCE.sendTo(new ServerValues(seed, forceDebug, blacklist), player);
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
