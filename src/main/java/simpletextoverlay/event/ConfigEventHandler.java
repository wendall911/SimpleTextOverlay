package simpletextoverlay.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Config.Type;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.client.gui.overlay.OverlayManager;
import simpletextoverlay.network.message.ClientValues;
import simpletextoverlay.network.PacketHandler;
import simpletextoverlay.reference.Reference;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.Alignment;

public class ConfigEventHandler {

    public static final ConfigEventHandler INSTANCE = new ConfigEventHandler();
    private static final OverlayManager overlayManager = OverlayManager.INSTANCE;

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            NBTTagCompound data = new NBTTagCompound();

            data.setString("type", "config");
            try {
                SimpleTextOverlay.logger.info("Sending config change notification to server (onConfigChanged)");
                PacketHandler.INSTANCE.sendToServer(new ClientValues(data));
            } catch (Exception ex) {
                SimpleTextOverlay.logger.error("Failed to send config change notification!", ex);
            }

            SimpleTextOverlay.logger.info("Config updated.");
            ConfigManager.sync(Reference.MODID, Type.INSTANCE);
            overlayManager.loadOverlayFile(ConfigHandler.client.general.defaultOverlayFile, false);
            applyConfigSettings();
        }
    }

    public Map getAlignment() {
        Map<String, Integer> alignmentMap = new HashMap<>();

        alignmentMap.put("topleftX", ConfigHandler.client.alignment.topleftX);
        alignmentMap.put("topleftY", ConfigHandler.client.alignment.topleftY);
        alignmentMap.put("topcenterX", ConfigHandler.client.alignment.topcenterX);
        alignmentMap.put("topcenterY", ConfigHandler.client.alignment.topcenterY);
        alignmentMap.put("toprightX", ConfigHandler.client.alignment.toprightX);
        alignmentMap.put("toprightY", ConfigHandler.client.alignment.toprightY);
        alignmentMap.put("middleleftX", ConfigHandler.client.alignment.middleleftX);
        alignmentMap.put("middleleftY", ConfigHandler.client.alignment.middleleftY);
        alignmentMap.put("middlecenterX", ConfigHandler.client.alignment.middlecenterX);
        alignmentMap.put("middlecenterY", ConfigHandler.client.alignment.middlecenterY);
        alignmentMap.put("middlerightX", ConfigHandler.client.alignment.middlerightX);
        alignmentMap.put("middlerightY", ConfigHandler.client.alignment.middlerightY);
        alignmentMap.put("bottomleftX", ConfigHandler.client.alignment.bottomleftX);
        alignmentMap.put("bottomleftY", ConfigHandler.client.alignment.bottomleftY);
        alignmentMap.put("bottomcenterX", ConfigHandler.client.alignment.bottomcenterX);
        alignmentMap.put("bottomcenterY", ConfigHandler.client.alignment.bottomcenterY);
        alignmentMap.put("bottomrightX", ConfigHandler.client.alignment.bottomrightX);
        alignmentMap.put("bottomrightY", ConfigHandler.client.alignment.bottomrightY);

        return alignmentMap;
    }

    public void applyConfigSettings() {
        Map<String, Integer>  alignmentMap = getAlignment();
        for (Alignment alignment : Alignment.values()) {
            String alignmentName = alignment.toString().toLowerCase(Locale.ENGLISH);
            String alignmentNameX = alignmentName + "X";
            String alignmentNameY = alignmentName + "Y";
            int x = alignmentMap.getOrDefault(alignmentNameX, alignment.defaultX);
            int y = alignmentMap.getOrDefault(alignmentNameY, alignment.defaultY);

            alignment.setX(x);
            alignment.setY(y);
        }

        overlayManager.setTagBlacklist(ConfigHandler.server.blacklistTags);
    }

}
