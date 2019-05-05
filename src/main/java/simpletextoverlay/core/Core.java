package simpletextoverlay.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import simpletextoverlay.client.gui.overlay.Info;
import simpletextoverlay.client.gui.overlay.InfoText;
import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.parser.IParser;
import simpletextoverlay.parser.json.JsonParser;
import simpletextoverlay.printer.IPrinter;
import simpletextoverlay.printer.json.JsonPrinter;
import simpletextoverlay.reference.Names;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.Tag;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.value.Value;
import simpletextoverlay.value.ValueComplex;

public class Core {
    private static final Pattern PATTERN = Pattern.compile("\\{ICON\\|( *)\\}", Pattern.CASE_INSENSITIVE);
    private static final Matcher MATCHER = PATTERN.matcher("");
    public static final Core INSTANCE = new Core();

    private IParser parser = new JsonParser();

    private final Minecraft client = Minecraft.getMinecraft();
    private File configDirectory = null;
    private File configFile = null;
    private String configFilename;
    private final Map<Alignment, List<List<Value>>> format = new HashMap<>();
    private final List<Info> info = new ArrayList<>();
    private final List<Info> infoItemQueue = new ArrayList<>();

    private Core() {
        Tag.setInfo(this.infoItemQueue);
        Value.setInfo(this.infoItemQueue);
    }

    public boolean setConfigDirectory(final File directory) {
        this.configDirectory = directory;
        return true;
    }

    public File getConfigDirectory() {
        return this.configDirectory;
    }

    public boolean loadConfig(final String filename) {
        final File file = new File(this.configDirectory, filename);

        this.configFilename = filename;

        if (file.exists()) {
            if (filename.endsWith(Names.Files.EXT_JSON)) {
                this.configFile = file;
            }
            else {
                SimpleTextOverlay.logger.error("The config '{}' does not end in .json", filename);
                return false;
            }
        }
        else {
            SimpleTextOverlay.logger.warn("The config '{}' does not exist", filename);
        }

        return reloadConfig();
    }

    public void updateTagValues() {
        final World world = this.client.world;
        if (world == null) {
            return;
        }

        final ScaledResolution scaledResolution = new ScaledResolution(this.client);
        final float scale = (float) ConfigHandler.client.general.scale;
        final int scaledWidth = (int) (scaledResolution.getScaledWidth() / scale);
        final int scaledHeight = (int) (scaledResolution.getScaledHeight() / scale);

        Tag.setWorld(world);

        final EntityPlayerSP player = this.client.player;
        if (player == null) {
            return;
        }
        Tag.setPlayer(player);

        this.info.clear();
        int x, y;

        for (final Alignment alignment : Alignment.values()) {
            final List<List<Value>> lines = this.format.get(alignment);

            if (lines == null) {
                continue;
            }

            final FontRenderer fontRenderer = this.client.fontRenderer;
            final List<Info> queue = new ArrayList<>();

            for (final List<Value> line : lines) {
                StringBuilder str = new StringBuilder();

                this.infoItemQueue.clear();
                for (final Value value : line) {
                    str.append(getValue(value));
                }

                if (str.length() > 0) {
                    final String processed = str.toString().replaceAll("\\{ICON\\|( *)\\}", "$1");

                    x = alignment.getX(scaledWidth, fontRenderer.getStringWidth(processed));
                    final InfoText text = new InfoText(fontRenderer, processed, x, 0);

                    if (this.infoItemQueue.size() > 0) {
                        MATCHER.reset(str.toString());

                        for (int i = 0; i < this.infoItemQueue.size() && MATCHER.find(); i++) {
                            final Info item = this.infoItemQueue.get(i);
                            item.x = fontRenderer.getStringWidth(str.substring(0, MATCHER.start()));
                            text.children.add(item);

                            str = new StringBuilder(str.toString().replaceFirst(Pattern.quote(MATCHER.group(0)), MATCHER.group(1)));
                            MATCHER.reset(str.toString());
                        }
                    }
                    queue.add(text);
                }
            }

            y = alignment.getY(scaledHeight, queue.size() * (fontRenderer.FONT_HEIGHT + 1));
            for (final Info item : queue) {
                item.y = y;
                this.info.add(item);
                y += fontRenderer.FONT_HEIGHT + 1;
            }

            this.info.addAll(queue);
        }

        Tag.releaseResources();
        ValueComplex.ValueFile.tick();
    }

    public void renderOverlay() {
        final float scale = (float) ConfigHandler.client.general.scale;

        GlStateManager.pushMatrix();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.scale(scale, scale, scale);

        for (final Info info : this.info) {
            info.draw();
        }

        GlStateManager.scale(1.0f / scale, 1.0f / scale, 1.0f / scale);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        GlStateManager.popMatrix();
    }

    public boolean reloadConfig() {
        this.info.clear();
        this.infoItemQueue.clear();
        this.format.clear();

        if (this.parser == null) {
            return false;
        }

        final InputStream inputStream = getInputStream();
        if (inputStream == null) {
            return false;
        }

        if (this.parser.load(inputStream) && this.parser.parse(this.format)) {
            return true;
        }

        this.format.clear();
        return false;
    }

    private InputStream getInputStream() {
        InputStream inputStream = null;

        try {
            if (this.configFile != null && this.configFile.exists()) {
                SimpleTextOverlay.logger.warn("Loading file config...");
                inputStream = new FileInputStream(this.configFile);
            } else {  
                final ResourceLocation resourceLocation = new ResourceLocation("simpletextoverlay", Names.Files.FILE_JSON.toLowerCase(Locale.ENGLISH));
                final IResource resource = this.client.getResourceManager().getResource(resourceLocation);
                InputStream resourceInputStream = resource.getInputStream();

                if (this.configFilename.equals(Names.Files.FILE_JSON.toLowerCase(Locale.ENGLISH))) {
                    SimpleTextOverlay.logger.warn("Creating overlay config...", this.configFilename);

                    byte[] buffer = new byte[resourceInputStream.available()];
                    resourceInputStream.read(buffer);
                    this.configFile = new File(this.configDirectory, this.configFilename);

                    OutputStream outStream = new FileOutputStream(this.configFile);
                    outStream.write(buffer);

                    inputStream = new FileInputStream(this.configFile);
                }
                else {
                    SimpleTextOverlay.logger.warn("Loading default config...");
                    inputStream = resourceInputStream;
                }
            }
        } catch (final Exception e) {
            SimpleTextOverlay.logger.error("", e);
        }

        return inputStream;
    }

    public boolean saveConfig(final String filename) {
        IPrinter printer = null;
        final File file = new File(this.configDirectory, filename);
        if (filename.endsWith(Names.Files.EXT_JSON)) {
            printer = new JsonPrinter();
        } else {
            SimpleTextOverlay.logger.warn("'{}' is an invalid file name");
        }

        return printer != null && printer.print(file, this.format);
    }

    private String getValue(final Value value) {
        try {
            if (value.isValidSize()) {
                return value.getReplacedValue();
            }
        } catch (final Exception e) {
            SimpleTextOverlay.logger.debug("Failed to get value!", e);
            return "null";
        }

        return "";
    }
}
