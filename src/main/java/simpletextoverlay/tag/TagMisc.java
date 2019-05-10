package simpletextoverlay.tag;

import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;

import net.minecraftforge.fml.common.Loader;

import org.lwjgl.opengl.Display;

import simpletextoverlay.client.gui.overlay.InfoIcon;
import simpletextoverlay.tag.registry.TagRegistry;

public abstract class TagMisc extends Tag {
    protected static final ResourcePackRepository resourcePackRepository = minecraft.getResourcePackRepository();

    @Override
    public String getCategory() {
        return "misc";
    }

    public static class MemoryMaximum extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().maxMemory());
        }
    }

    public static class MemoryMaximumMB extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().maxMemory() / 1024L / 1024L);
        }
    }

    public static class MemoryTotal extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().totalMemory());
        }
    }

    public static class MemoryTotalMB extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().totalMemory() / 1024L / 1024L);
        }
    }

    public static class MemoryFree extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().freeMemory());
        }
    }

    public static class MemoryUsed extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        }
    }

    public static class MemoryUse extends TagMisc {
        @Override
        public String getValue() {
            long memMax = Runtime.getRuntime().maxMemory();
            long memTotal = Runtime.getRuntime().totalMemory();
            long memFree = Runtime.getRuntime().freeMemory();
            long memUsed = memTotal - memFree;

            return String.valueOf(memUsed * 100L / memMax);
        }
    }

    public static class MemoryUseMB extends TagMisc {
        @Override
        public String getValue() {
            long memTotal = Runtime.getRuntime().totalMemory();
            long memFree = Runtime.getRuntime().freeMemory();
            long memUsed = memTotal - memFree;

            return String.valueOf(memUsed / 1024L / 1024L);
        }
    }

    public static class FPS extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Minecraft.getDebugFPS());
        }
    }

    public static class ResourcePack extends TagMisc {
        @Override
        public String getValue() {
            final List<ResourcePackRepository.Entry> repositoryEntries = resourcePackRepository.getRepositoryEntries();
            if (repositoryEntries.size() > 0) {
                return repositoryEntries.get(0).getResourcePackName();
            }
            return resourcePackRepository.rprDefaultResourcePack.getPackName();
        }
    }

    public static class EntitiesRendered extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(minecraft.renderGlobal.countEntitiesRendered);
        }
    }

    public static class EntitiesTotal extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(minecraft.renderGlobal.countEntitiesTotal);
        }
    }

    public static class EntitiesHidden extends TagMisc {
        @Override
        public String getValue() {
            String entities = minecraft.renderGlobal.getDebugInfoEntities();
            return entities.split(" ")[3];
        }
    }

    public static class Server extends TagMisc {
        @Override
        public String getValue() {
            final String str = player.connection.getNetworkManager().getRemoteAddress().toString();
            final int i = str.indexOf("/");
            final int j = str.indexOf(":");
            if (i < 0) {
                return "localhost";
            }

            final String name = (i == 0) ? str.substring(i + 1, j) : str.substring(0, i);
            final String port = str.substring(j + 1);
            return name + (port.equals("25565") ? "" : ":" + port);
        }
    }

    public static class ServerName extends TagMisc {
        @Override
        public String getValue() {
            final String str = player.connection.getNetworkManager().getRemoteAddress().toString();
            final int i = str.indexOf("/");
            if (i < 0) {
                return "localhost";
            } else if (i == 0) {
                return str.substring(i + 1, str.indexOf(":"));
            }
            return str.substring(0, i);
        }
    }

    public static class ServerIP extends TagMisc {
        @Override
        public String getValue() {
            final String str = player.connection.getNetworkManager().getRemoteAddress().toString();
            final int i = str.indexOf("/");
            if (i < 0) {
                return "127.0.0.1";
            }
            return str.substring(i + 1, str.indexOf(":"));
        }
    }

    public static class ServerPort extends TagMisc {
        @Override
        public String getValue() {
            final String str = player.connection.getNetworkManager().getRemoteAddress().toString();
            final int i = str.indexOf("/");
            if (i < 0) {
                return "-1";
            }
            return str.substring(str.indexOf(":") + 1);
        }
    }

    public static class Ping extends TagMisc {
        @Override
        public String getValue() {
            try {
                final NetworkPlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(player.getUniqueID());
                return String.valueOf(playerInfo.getResponseTime());
            } catch (final Exception e) {
            }
            return "-1";
        }
    }

    public static class PingIcon extends TagMisc {
        @Override
        public String getValue() {
            try {
                final NetworkPlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(player.getUniqueID());
                final int responseTime = playerInfo.getResponseTime();
                int pingIndex = 4;
                if (responseTime < 0) {
                    pingIndex = 5;
                } else if (responseTime < 150) {
                    pingIndex = 0;
                } else if (responseTime < 300) {
                    pingIndex = 1;
                } else if (responseTime < 600) {
                    pingIndex = 2;
                } else if (responseTime < 1000) {
                    pingIndex = 3;
                }

                final InfoIcon icon = new InfoIcon("textures/gui/icons.png");
                icon.setDisplayDimensions(0, 0, 10, 8);
                icon.setTextureData(0, 176 + pingIndex * 8, 10, 8, 256, 256);
                info.add(icon);
                return getIconTag(icon);
            } catch (final Exception e) {
            }
            return "-1";
        }
    }

    public static class MinecraftVersion extends TagMisc {
        @Override
        public String getValue() {
            return net.minecraftforge.common.ForgeVersion.mcVersion;
        }
    }

    public static class ForgeVersion extends TagMisc {
        @Override
        public String getValue() {
            return net.minecraftforge.common.ForgeVersion.getVersion();
        }
    }

    public static class MCPVersion extends TagMisc {
        @Override
        public String getValue() {
            return net.minecraftforge.common.ForgeVersion.mcpVersion;
        }
    }

    public static class ModsTotal extends TagMisc {
        @Override
        public String getValue() {
            return String.format("%d", Loader.instance().getModList().size());
        }
    }

    public static class ModsActive extends TagMisc {
        @Override
        public String getValue() {
            return String.format("%d", Loader.instance().getActiveModList().size());
        }
    }

    public static class VsyncEnabled extends TagMisc {
        @Override
        public String getValue() {
            return minecraft.getPlayerUsageSnooper().getCurrentStats().get("vsync_enabled");
        }
    }

    public static class JavaVersion extends TagMisc {
        @Override
        public String getValue() {
            return minecraft.getPlayerUsageSnooper().getCurrentStats().get("java_version");
        }
    }

    public static class JavaBit extends TagMisc {
        @Override
        public String getValue() {
            return (minecraft.isJava64bit() ? "64" : "32") + "bit";
        }
    }

    public static class CpuInfo extends TagMisc {
        @Override
        public String getValue() {
            return OpenGlHelper.getCpu();
        }
    }

    public static class DebugLoadedEntities extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(minecraft.world.getDebugLoadedEntities());
        }
    }

    public static class DisplayWidth extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Display.getWidth());
        }
    }

    public static class DisplayHeight extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(Display.getHeight());
        }
    }

    public static class ProviderName extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(minecraft.world.getProviderName());
        }
    }

    public static class OpenglVendor extends TagMisc {
        @Override
        public String getValue() {
            return GlStateManager.glGetString(7936);
        }
    }

    public static class VideoCardInfo extends TagMisc {
        @Override
        public String getValue() {
            return GlStateManager.glGetString(7937);
        }
    }

    public static class OpenglVersion extends TagMisc {
        @Override
        public String getValue() {
            return GlStateManager.glGetString(7938);
        }
    }

    public static class RenderChunks extends TagMisc {
        @Override
        public String getValue() {
            String debug = minecraft.renderGlobal.getDebugInfoRenders();
            return debug.split("D:")[0].split(" ")[1].split("/")[1];
        }
    }

    public static class RenderedChunks extends TagMisc {
        @Override
        public String getValue() {
            String debug = minecraft.renderGlobal.getDebugInfoRenders();
            return debug.split("D:")[0].split(" ")[1].split("/")[0];
        }
    }

    public static class RenderChunksUpdated extends TagMisc {
        @Override
        public String getValue() {
            return String.format("%d", RenderChunk.renderChunksUpdated);
        }
    }

    public static class RenderChunksMany extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(minecraft.renderChunksMany);
        }
    }

    public static class EffectRenderStats extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(minecraft.effectRenderer.getStatistics());
        }
    }

    public static class RenderDistanceChunks extends TagMisc {
        @Override
        public String getValue() {
            String debug = minecraft.renderGlobal.getDebugInfoRenders();
            return debug.split("D: ")[1].split(" ")[0].split(",")[0];
        }
    }

    public static class LightUpdates extends TagMisc {
        @Override
        public String getValue() {
            String debug = minecraft.renderGlobal.getDebugInfoRenders();
            return debug.split("D: ")[1].split(" ")[2].split(",")[0];
        }
    }

    public static class RenderDebugInfo extends TagMisc {
        @Override
        public String getValue() {
            return "pC" + minecraft.renderGlobal.getDebugInfoRenders().split("pC")[1];
        }
    }

    public static class FrameRate extends TagMisc {
        @Override
        public String getValue() {
			return minecraft.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : String.format("%d", (int)minecraft.gameSettings.limitFramerate);
        }
    }

    public static class FancyGraphics extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(minecraft.gameSettings.fancyGraphics);
        }
    }

    public static class Clouds extends TagMisc {
        @Override
        public String getValue() {
            return minecraft.gameSettings.clouds == 0 ? "no-clouds" : (minecraft.gameSettings.clouds == 1 ? "fast-clouds" : "fancy-clouds");
        }
    }

    public static class UseVbo extends TagMisc {
        @Override
        public String getValue() {
            return String.valueOf(OpenGlHelper.useVbo());
        }
    }

    public static class Zero extends TagMisc {
        @Override
        public String getValue() {
            return "0";
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new Clouds().setName("clouds"));
        TagRegistry.INSTANCE.register(new CpuInfo().setName("cpu"));
        TagRegistry.INSTANCE.register(new DebugLoadedEntities().setName("debugloadedentities"));
        TagRegistry.INSTANCE.register(new DisplayHeight().setName("displaywidth"));
        TagRegistry.INSTANCE.register(new DisplayWidth().setName("displaywidth"));
        TagRegistry.INSTANCE.register(new EffectRenderStats().setName("effectrenderstats"));
        TagRegistry.INSTANCE.register(new EntitiesHidden().setName("entitieshidden"));
        TagRegistry.INSTANCE.register(new EntitiesRendered().setName("entitiesrendered"));
        TagRegistry.INSTANCE.register(new EntitiesTotal().setName("entitiestotal"));
        TagRegistry.INSTANCE.register(new FancyGraphics().setName("fancygraphics"));
        TagRegistry.INSTANCE.register(new ForgeVersion().setName("forgeversion"));
        TagRegistry.INSTANCE.register(new FPS().setName("fps"));
        TagRegistry.INSTANCE.register(new FrameRate().setName("framerate"));
        TagRegistry.INSTANCE.register(new JavaBit().setName("javabit"));
        TagRegistry.INSTANCE.register(new JavaVersion().setName("javaversion"));
        TagRegistry.INSTANCE.register(new LightUpdates().setName("lightupdates"));
        TagRegistry.INSTANCE.register(new MCPVersion().setName("mcpversion"));
        TagRegistry.INSTANCE.register(new MemoryFree().setName("memfree"));
        TagRegistry.INSTANCE.register(new MemoryMaximum().setName("memmax"));
        TagRegistry.INSTANCE.register(new MemoryMaximumMB().setName("memmaxmb"));
        TagRegistry.INSTANCE.register(new MemoryTotal().setName("memtotal"));
        TagRegistry.INSTANCE.register(new MemoryTotalMB().setName("memtotalmb"));
        TagRegistry.INSTANCE.register(new MemoryUsed().setName("memused"));
        TagRegistry.INSTANCE.register(new MemoryUseMB().setName("memusemb"));
        TagRegistry.INSTANCE.register(new MemoryUse().setName("memuse"));
        TagRegistry.INSTANCE.register(new MinecraftVersion().setName("mcversion"));
        TagRegistry.INSTANCE.register(new ModsActive().setName("modsactive"));
        TagRegistry.INSTANCE.register(new ModsTotal().setName("modstotal"));
        TagRegistry.INSTANCE.register(new OpenglVendor().setName("openglvendor"));
        TagRegistry.INSTANCE.register(new OpenglVersion().setName("openglversion"));
        TagRegistry.INSTANCE.register(new PingIcon().setName("pingicon"));
        TagRegistry.INSTANCE.register(new Ping().setName("ping"));
        TagRegistry.INSTANCE.register(new ProviderName().setName("providername"));
        TagRegistry.INSTANCE.register(new RenderChunksMany().setName("renderchunksmany"));
        TagRegistry.INSTANCE.register(new RenderChunks().setName("renderchunks"));
        TagRegistry.INSTANCE.register(new RenderChunksUpdated().setName("renderchunksupdated"));
        TagRegistry.INSTANCE.register(new RenderDebugInfo().setName("renderdebuginfo"));
        TagRegistry.INSTANCE.register(new RenderedChunks().setName("renderedchunks"));
        TagRegistry.INSTANCE.register(new RenderDistanceChunks().setName("renderdistancechunks"));
        TagRegistry.INSTANCE.register(new ResourcePack().setName("resourcepack"));
        TagRegistry.INSTANCE.register(new ServerIP().setName("serverip"));
        TagRegistry.INSTANCE.register(new ServerName().setName("servername"));
        TagRegistry.INSTANCE.register(new ServerPort().setName("serverport"));
        TagRegistry.INSTANCE.register(new Server().setName("server"));
        TagRegistry.INSTANCE.register(new UseVbo().setName("vbo"));
        TagRegistry.INSTANCE.register(new VideoCardInfo().setName("vidcard"));
        TagRegistry.INSTANCE.register(new VsyncEnabled().setName("vsync"));
        TagRegistry.INSTANCE.register(new Zero().setName("zero"));
    }
}
