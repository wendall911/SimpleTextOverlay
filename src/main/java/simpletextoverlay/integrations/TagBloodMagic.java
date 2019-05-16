package simpletextoverlay.integrations;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

import simpletextoverlay.network.message.ClientValues;
import simpletextoverlay.network.PacketHandler;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.tag.Tag;

import WayofTime.bloodmagic.util.helper.NetworkHelper;

public abstract class TagBloodMagic extends Tag {

    @Override
    public String getCategory() {
        return "bloodmagic";
    }

    @Override
    public String getLocalizedCategory() {
        return I18n.format("itemGroup.bloodmagic.creativeTab");
    }

    private static boolean isLoggedIn = false;
    private static NBTTagCompound data = new NBTTagCompound();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void init() {
        data.setLong("bmcurrentlp", 0);
        data.setLong("bmorbtier", 0);
    }

    public static void setLp(int lp) {
        data.setLong("bmcurrentlp", lp);
    }

    public static void setTier(int tier) {
        data.setLong("bmorbtier", tier);
    }

    protected static long updateTime = System.currentTimeMillis();

    protected void sendDataRequest() {
        if ((System.currentTimeMillis() - updateTime) > 500) {
            updateTime = System.currentTimeMillis();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    NBTTagCompound requestData = new NBTTagCompound();
                    requestData.setString("type", getCategory());
                    try {
                        PacketHandler.INSTANCE.sendToServer(new ClientValues(requestData));
                    } catch (Exception ex) {
                        SimpleTextOverlay.logger.error("Failed to send server request for bloodmagic!", ex);
                    }
                }
            });
        }
    }

    public static class CurrentEssence extends TagBloodMagic {
        @Override
        public String getValue() {
            if (isLoggedIn) {
                sendDataRequest();
            }
            else {
                setLp(NetworkHelper.getSoulNetwork(player).getCurrentEssence());
            }
            return String.valueOf(data.getLong("bmcurrentlp"));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("tooltip.bloodmagic.sigil.divination.currentEssence", 0).replace(": 0", "");
        }
    }

    public static class OrbTier extends TagBloodMagic {
        @Override
        public String getValue() {
            if (isLoggedIn) {
                sendDataRequest();
            }
            else {
                setTier(NetworkHelper.getSoulNetwork(player).getOrbTier());
            }
            return String.valueOf(data.getLong("bmorbtier"));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("tooltip.bloodmagic.sigil.divination.currentAltarTier", 0).replace(": 0", "");
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new TagBloodMagic.CurrentEssence().setName("bmcurrentlp"));
        TagRegistry.INSTANCE.register(new TagBloodMagic.OrbTier().setName("bmorbtier"));
    }

}
