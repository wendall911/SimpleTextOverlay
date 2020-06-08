package simpletextoverlay.util;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.config.SyncedConfig;
import simpletextoverlay.config.OverlayOption;
import simpletextoverlay.handler.message.ConfigUpdate;
import simpletextoverlay.handler.PacketHandler;

import net.minecraft.entity.player.ServerPlayerEntity;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.fml.network.NetworkDirection;

public class PacketHandlerHelper {

    public static void sendServerConfigValues(ServerPlayerEntity player) {
        CompoundNBT data = new CompoundNBT();

        SimpleTextOverlay.logger.info("Sending server config values to player.");

        data.putString(OverlayOption.BLACKLIST_TAGS.getName(),
                String.valueOf(SyncedConfig.getValue(OverlayOption.BLACKLIST_TAGS)));
        data.putString(OverlayOption.FORCE_DEBUG.getName(),
                String.valueOf(SyncedConfig.getBooleanValue(OverlayOption.FORCE_DEBUG)));

        PacketHandler.INSTANCE.sendTo(new ConfigUpdate(data), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

}

