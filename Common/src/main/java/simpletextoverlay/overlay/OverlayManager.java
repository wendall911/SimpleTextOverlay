package simpletextoverlay.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.joml.Matrix3x2fStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

import technology.roughness.whitenoise.platform.Services;

import simpletextoverlay.config.OverlayConfig;

public class OverlayManager {

    public static final OverlayManager INSTANCE = new OverlayManager();
    public List<Info> lines = new ArrayList<>();
    public final HudCompass hudCompass = new HudCompass();

    private OverlayManager() {}

    public void init() {
        int lineNum = 1;

        this.lines.clear();

        for (String fieldName : OverlayConfig.fields()) {
            boolean skip = false;

            switch (fieldName) {
                case "light":
                    lines.add(new LightInfo(OverlayConfig.lightLabel(), lineNum));
                    break;
                case "foot":
                    lines.add(new FootInfo(OverlayConfig.footLabel(), lineNum));
                    break;
                case "biome":
                    lines.add(new BiomeInfo(OverlayConfig.biomeLabel(), lineNum));
                    break;
                case "time":
                    lines.add(new TimeInfo(OverlayConfig.timeLabel(), lineNum));
                    break;
                case "days":
                    lines.add(new DaysInfo(OverlayConfig.daysLabel(), lineNum));
                    break;
                case "season":
                    if (Services.PLATFORM.isModLoaded("homeostaticseasons")
                            || Services.PLATFORM.isModLoaded("sereneseasons")
                            || Services.PLATFORM.isModLoaded("seasons")
                            || Services.PLATFORM.isModLoaded("eclipticseasons")) {
                        lines.add(new SeasonInfo("", lineNum));
                    }
                    else {
                        skip = true;
                    }
                    break;
            }

            if (!skip) {
                lineNum++;
            }
        }
    }

    public void renderOverlay(GuiGraphics guiGraphics, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        BlockPos pos = Objects.requireNonNull(mc.getCameraEntity()).blockPosition();

        if (mc.level != null && mc.level.isLoaded(pos)) {
            float scale = (float) OverlayConfig.scale();
            int scaledWidth = (int) (mc.getWindow().getGuiScaledWidth() / scale);
            int scaledHeight = (int) (mc.getWindow().getGuiScaledHeight() / scale);
            Matrix3x2fStack matrix = guiGraphics.pose();

            matrix.pushMatrix();
            matrix.scale(scale, scale);

            for (final Info line : this.lines) {
                line.renderText(guiGraphics, mc, pos, scaledWidth, scaledHeight);
            }

            if (OverlayConfig.showCompass()) {
                hudCompass.renderText(guiGraphics, mc, scaledWidth, partialTicks);
            }

            matrix.popMatrix();
        }
    }

}
