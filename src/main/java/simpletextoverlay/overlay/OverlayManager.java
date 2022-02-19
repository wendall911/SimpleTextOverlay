package simpletextoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraftforge.client.gui.ForgeIngameGui;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.BiomeInfo;
import simpletextoverlay.overlay.FootInfo;
import simpletextoverlay.overlay.LightInfo;
import simpletextoverlay.overlay.TimeInfo;

public class OverlayManager {

    public static final OverlayManager INSTANCE = new OverlayManager();
    public final List<Info> lines = new ArrayList<>();

    private OverlayManager() {}

    public void init() {
        int lineNum = 1;

        this.lines.clear();

        for (String fieldName : OverlayConfig.fields()) {
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
            }
            lineNum++;
        };
    }

    public void renderOverlay(ForgeIngameGui gui, PoseStack matrix, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        BlockPos pos = mc.getCameraEntity().blockPosition();

        if (mc.level != null && mc.level.isLoaded(pos)) {
            float scale = (float) OverlayConfig.scale();
            int scaledWidth = (int) (mc.getWindow().getGuiScaledWidth() / scale);
            int scaledHeight = (int) (mc.getWindow().getGuiScaledHeight() / scale);

            matrix.pushPose();
            matrix.scale(scale, scale, scale);

            for (final Info line : this.lines) {
                line.renderText(matrix, mc, pos, scaledWidth, scaledHeight);
            }

            matrix.popPose();
        }
    }

}
