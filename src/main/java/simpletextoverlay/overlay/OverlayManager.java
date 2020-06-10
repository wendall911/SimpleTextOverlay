package simpletextoverlay.overlay;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;

import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.mojang.blaze3d.systems.RenderSystem;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.parser.IParser;
import simpletextoverlay.parser.json.JsonParser;
import simpletextoverlay.printer.IPrinter;
import simpletextoverlay.printer.json.JsonPrinter;
import simpletextoverlay.reference.Names;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.Tag;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.value.Value;

public class OverlayManager {

    private static final Pattern PATTERN = Pattern.compile("\\{ICON\\|( *)\\}", Pattern.CASE_INSENSITIVE);
    private static final Matcher MATCHER = PATTERN.matcher("");
    public static final OverlayManager INSTANCE = new OverlayManager();

    private IParser parser = new JsonParser();

    private final Minecraft client = Minecraft.getInstance();
    private File overlayDirectory;
    private String overlayFile = "";
    private final Map<Alignment, List<List<Value>>> format = new HashMap<>();
    private final List<Info> info = new ArrayList<>();
    private final List<Info> infoItemQueue = new ArrayList<>();
    private Set<String> tagBlacklist = new HashSet<String>();
    private int cycleIndex = 0;

    private OverlayManager() {
        Tag.setInfo(this.infoItemQueue);
        Value.setInfo(this.infoItemQueue);
    }

    public void init(final File modConfigFile) {
        this.overlayDirectory = modConfigFile.toPath().resolve(SimpleTextOverlay.MODID).toFile();
        createPath(this.overlayDirectory);

        cycleIndex = getCycleIndex();

        loadOverlayFile(OverlayConfig.CLIENT.defaultOverlayFile.get(), false);
    }

    public void setTagBlacklist(String[] blacklist) {
        tagBlacklist = new HashSet<String>(Arrays.asList(blacklist));
    }

    public Set<String> getTagBlacklist() {
        return tagBlacklist;
    }

    public String getOverlayFile() {
        return this.overlayFile;
    }

    public File getConfigDirectory() {
        return this.overlayDirectory;
    }

    public boolean reloadOverlayFile() {
        return loadOverlayFile(this.overlayFile, true);
    }

    public boolean loadOverlayFile(final String filename, boolean reload) {
        if (!reload && this.overlayFile.equals(filename)) {
            return true;
        }
        final File file = new File(this.overlayDirectory, filename);
        InputStream inputStream = null;

        this.overlayFile = filename;
        this.info.clear();
        this.infoItemQueue.clear();
        this.format.clear();

        if (!filename.endsWith(Names.Files.EXT_JSON)) {
            SimpleTextOverlay.logger.error("The overlay config '%s' does not end in .json", filename);
            return false;
        }

        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
            } catch (final Exception e) {
                SimpleTextOverlay.logger.error("Unable to load '%s'.", filename, e);
                return false;
            }
        }
        else {
            inputStream = getResourceOverlay(filename);
        }

        if (this.parser.load(inputStream)) {
            if (!this.parser.parse(this.format)) {
                SimpleTextOverlay.logger.error("Error parsing overlay config file...");
                return false;
            };
        }
        else {
            SimpleTextOverlay.logger.error("Error loading overlay config file...");
            return false;
        }

        return true;
    }

    private InputStream getResourceOverlay(String name) {
        InputStream inputStream = null;

        try {
            String resourceFile = "overlays/" + name;
            ResourceLocation resourceLocation = new ResourceLocation(SimpleTextOverlay.MODID, resourceFile);
            IResource resource = this.client.getResourceManager().getResource(resourceLocation);
            inputStream = resource.getInputStream();
        } catch (final Exception ex) {
            SimpleTextOverlay.logger.error("Unable to load resource '{}', loading default overlay...", name);
            String defaultName = Names.Files.FILE_JSON.toLowerCase(Locale.ENGLISH);

            if (name.equals(OverlayConfig.CLIENT.debugOverlayFile.get())) {
                defaultName = Names.Files.FILE_DEBUG.toLowerCase(Locale.ENGLISH);
            }

            try {
                String resourceFile = "overlays/" + defaultName;
                ResourceLocation resourceLocation = new ResourceLocation(SimpleTextOverlay.MODID, resourceFile);
                IResource resource = this.client.getResourceManager().getResource(resourceLocation);
                inputStream = resource.getInputStream();
            } catch (final Exception e) {
                SimpleTextOverlay.logger.error("Unable to load resource '{}' '{}' '{}'", defaultName, ex, e);
            }
        }

        return inputStream;
    }

    private void createPath(File directory) {
        if (!directory.exists() && !directory.mkdirs()) {
            SimpleTextOverlay.logger.error("Failed to create config directory '{}'.",
                    directory.getAbsolutePath());
        }
    }

    public void updateTagValues() {
        final World world = this.client.level.getLevel();
        if (world == null) {
            return;
        }

        final double scale = OverlayConfig.CLIENT.scale.get();
        final int scaledWidth = (int) (client.getWindow().getGuiScaledWidth() / scale);
        final int scaledHeight = (int) (client.getWindow().getGuiScaledHeight() / scale);

        Tag.setWorld(world);

        final ClientPlayerEntity player = this.client.player;
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

            final FontRenderer fontRenderer = this.client.font;
            final List<Info> queue = new ArrayList<>();

            for (final List<Value> line : lines) {
                StringBuilder str = new StringBuilder();

                this.infoItemQueue.clear();
                for (final Value value : line) {
                    str.append(getValue(value));
                }

                if (str.length() > 0) {
                    final String processed = str.toString().replaceAll("\\{ICON\\|( *)\\}", "$1");

                    x = alignment.getX(scaledWidth, fontRenderer.width(processed));
                    final InfoText text = new InfoText(fontRenderer, processed, x, 0);

                    if (this.infoItemQueue.size() > 0) {
                        MATCHER.reset(str.toString());

                        for (int i = 0; i < this.infoItemQueue.size() && MATCHER.find(); i++) {
                            final Info item = this.infoItemQueue.get(i);
                            item.x = fontRenderer.width(str.substring(0, MATCHER.start()));
                            text.children.add(item);

                            str = new StringBuilder(str.toString().replaceFirst(Pattern.quote(MATCHER.group(0)), MATCHER.group(1)));
                            MATCHER.reset(str.toString());
                        }
                    }
                    queue.add(text);
                }
            }

            y = alignment.getY(scaledHeight, queue.size() * (fontRenderer.lineHeight + 1));
            for (final Info item : queue) {
                item.y = y;
                this.info.add(item);
                y += fontRenderer.lineHeight + 1;
            }

            this.info.addAll(queue);
        }

        Tag.releaseResources();
    }

    public void renderOverlay() {
        final double scale = OverlayConfig.CLIENT.scale.get();

        RenderSystem.pushMatrix();

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.scaled(scale, scale, scale);

        for (final Info info : this.info) {
            if (info.text != null && info.text.contains("SCALESMALL")) {
                RenderSystem.scaled(0.5, 0.5, 0.5);
            }
            info.draw();
            if (info.text != null && info.text.contains("SCALESMALL")) {
                RenderSystem.scaled(1.0f / 0.5, 1.0f / 0.5, 1.0f / 0.5);
            }
        }

        RenderSystem.scaled(1.0f / scale, 1.0f / scale, 1.0f / scale);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);

        RenderSystem.popMatrix();
    }

    public boolean saveConfig(final String filename) {
        IPrinter printer = null;
        final File file = new File(this.overlayDirectory, filename);
        if (filename.endsWith(Names.Files.EXT_JSON)) {
            printer = new JsonPrinter();
        } else {
            SimpleTextOverlay.logger.warn("'{}' is an invalid file name");
            return false;
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

    public boolean cycleOverlay() {
        int size = OverlayConfig.CLIENT.cycleOverlays.get().size();

        cycleIndex++;

        if (cycleIndex > size) {
            cycleIndex = 0;
        }

        return loadOverlayFile(OverlayConfig.CLIENT.cycleOverlays.get().get(cycleIndex), false);
    }

    private int getCycleIndex() {
        int size = OverlayConfig.CLIENT.cycleOverlays.get().size();
        String currentName = getOverlayFile();

        for (int i = 0; i < size; i++) {
            String name = OverlayConfig.CLIENT.cycleOverlays.get().get(i);
            if (name == currentName) return i;
        }

        return 0;
    }

}
