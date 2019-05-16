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

import simpletextoverlay.util.ThaumcraftHelper;

public abstract class TagThaumcraft extends Tag {

    @Override
    public String getCategory() {
        return "thaumcraft";
    }

    @Override
    public String getLocalizedCategory() {
        return I18n.format("key.categories.thaumcraft");
    }

    private static NBTTagCompound data = new NBTTagCompound();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void init() {
        data.setDouble("localaura", 0.0);
        data.setLong("localaurabase", 0);
        data.setDouble("localflux", 0.0);
    }

    public static void setAura(double aura) {
        data.setDouble("localaura", aura);
    }

    public static void setAuraBase(int aura) {
        data.setLong("localaurabase", aura);
    }

    public static void setFlux(double flux) {
        data.setDouble("localflux", flux);
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
                        SimpleTextOverlay.logger.error("Failed to send server request for thuamcraft!", ex);
                    }
                }
            });
        }
    }

    public static class Aura extends TagThaumcraft {
        @Override
        public String getValue() {
            if (isLoggedIn) {
                sendDataRequest();
            }
            else {
                setAura(ThaumcraftHelper.getVis(player.world, player.getPosition()));
            }
            return String.format("%.2f", data.getDouble("localaura"));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("tc.aspect.auram");
        }
    }

    public static class AuraBase extends TagThaumcraft {
        @Override
        public String getValue() {
            if (isLoggedIn) {
                sendDataRequest();
            }
            else {
                setAuraBase(ThaumcraftHelper.getAuraBase(player.world, player.getPosition()));
            }
            return String.valueOf(data.getLong("localaurabase"));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("tc.aspect.auram") + " base.";
        }
    }

    public static class Flux extends TagThaumcraft {
        @Override
        public String getValue() {
            if (isLoggedIn) {
                sendDataRequest();
            }
            else {
                setFlux(ThaumcraftHelper.getFlux(player.world, player.getPosition()));
            }
            return String.format("%.2f", data.getDouble("localflux"));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("thaumcraft.FLUX.name");
        }
    }

    public static class WarpNorm extends TagThaumcraft {
        @Override
        public String getValue() {
            return String.valueOf(ThaumcraftHelper.getWarpNorm(player));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("simpletextoverlay.tag.warpnorm.desc");
        }
    }

    public static class WarpPerm extends TagThaumcraft {
        @Override
        public String getValue() {
            return String.valueOf(ThaumcraftHelper.getWarpPerm(player));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("simpletextoverlay.tag.warpperm.desc");
        }
    }

    public static class WarpTemp extends TagThaumcraft {
        @Override
        public String getValue() {
            return String.valueOf(ThaumcraftHelper.getWarpTemp(player));
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("simpletextoverlay.tag.warptemp.desc");
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new TagThaumcraft.Aura().setName("tclocalaura"));
        TagRegistry.INSTANCE.register(new TagThaumcraft.AuraBase().setName("tclocalaurabase"));
        TagRegistry.INSTANCE.register(new TagThaumcraft.Flux().setName("tclocalflux"));
        TagRegistry.INSTANCE.register(new TagThaumcraft.WarpNorm().setName("tcwarpnorm"));
        TagRegistry.INSTANCE.register(new TagThaumcraft.WarpPerm().setName("tcwarpperm"));
        TagRegistry.INSTANCE.register(new TagThaumcraft.WarpTemp().setName("tcwarptemp"));
    }

}
