package simpletextoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.fml.ModList;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.BiomeInfo;
import simpletextoverlay.overlay.FootInfo;
import simpletextoverlay.overlay.HudCompass;
import simpletextoverlay.overlay.LightInfo;
import simpletextoverlay.overlay.SeasonInfo;
import simpletextoverlay.overlay.TimeInfo;

public class OverlayManager {

    public static final OverlayManager INSTANCE = new OverlayManager();
    public final List<Info> lines = new ArrayList<>();
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
                case "season":
                    if (ModList.get().isLoaded("sereneseasons")) {
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

    public void renderOverlay(PoseStack matrix, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        BlockPos pos = Objects.requireNonNull(mc.getCameraEntity()).blockPosition();

        if (mc.level != null && mc.level.isLoaded(pos)) {
            float scale = (float) OverlayConfig.scale();
            int scaledWidth = (int) (mc.getWindow().getGuiScaledWidth() / scale);
            int scaledHeight = (int) (mc.getWindow().getGuiScaledHeight() / scale);

            matrix.pushPose();
            matrix.scale(scale, scale, scale);

            for (final Info line : this.lines) {
                line.renderText(matrix, mc, pos, scaledWidth, scaledHeight);
            }

            if (OverlayConfig.showCompass()) {
                hudCompass.renderText(matrix, mc, scaledWidth, scaledHeight, partialTicks);
            }

            matrix.popPose();
        }
    }

}
