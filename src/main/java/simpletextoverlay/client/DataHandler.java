package simpletextoverlay.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import simpletextoverlay.overlay.compass.DataManager;

public class DataHandler {

    public static void handleSync(byte[] bytes) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null) {
            DataManager.handleSync(player, bytes);
        }
    }

}
