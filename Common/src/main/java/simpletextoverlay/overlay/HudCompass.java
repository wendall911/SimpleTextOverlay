package simpletextoverlay.overlay;

import java.util.Map;

import org.joml.Matrix3x2fStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.compass.Pin.PinType;
import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.ColorHelper;
import simpletextoverlay.util.FontHelper;
import simpletextoverlay.util.VecMath;

public class HudCompass {

    public void renderText(GuiGraphics guiGraphics, Minecraft mc, int scaledWidth, float _partialTicks) {
        final Player player = mc.player;

        if (player == null) {
            return;
        }
        final float partialTicks = mc.isPaused() ? 0 : _partialTicks;
        final double posX = player.getX();
        final double posY = player.getY();
        final double posZ = player.getZ();
        final String compassText = "·";
        final String worldSpawnText = "⊙";
        final String bedSpawnText = "⌂";
        final String lastDeathText = "✕";
        final float yaw = Mth.lerp(partialTicks, player.yRotO, player.getYRot()) % 360;

        final int x = Alignment.getCompassX(scaledWidth, mc.font.width(compassText));
        final int y = Alignment.getCompassY();

        final int bgColor = ColorHelper.rgb(0, 0, 0, OverlayConfig.getCompassOpacity());

        guiGraphics.fill(x - 92, y - 1, x + 96, mc.font.lineHeight + 2, bgColor);

        drawCardinal(mc, guiGraphics, yaw, 0, x, y, "S");
        drawCardinal(mc, guiGraphics, yaw, 90, x, y, "W");
        drawCardinal(mc, guiGraphics, yaw, 180, x, y, "N");
        drawCardinal(mc, guiGraphics, yaw, 270, x, y, "E");

        FontHelper.draw(mc, guiGraphics, compassText, x, y, ColorHelper.decode("#b02e26").getRGB(), false, FontHelper.TextType.NONE);

        Services.CAPABILITY_PLATFORM.getDataManagerCapability(player).ifPresent(pinsData -> {
            final Map<String, PinInfo<?>> pins = pinsData.get(player).getPins();
            float offset = 0.5F;
            final PinInfo<?> bedSpawn = pins.get(PinType.BEDSPAWN.toString());
            final PinInfo<?> lastDeath = pins.get(PinType.LASTDEATH.toString());
            final PinInfo<?> worldSpawn = pins.get(PinType.WORLDSPAWN.toString());

            if (bedSpawn != null) {
                final Vec2 bedSpawnAngle = VecMath.angleFromPos(bedSpawn.getPosition(), posX, posY, posZ);

                drawInfo(mc, guiGraphics, yaw, bedSpawnAngle.x, x, y + 3, bedSpawnText, 0.5F, offset, ColorHelper.decode("#9c9d97").getRGB());
            }

            if (lastDeath != null) {
                final Vec2 lastDeathAngle = VecMath.angleFromPos(lastDeath.getPosition(), posX, posY, posZ);

                drawInfo(mc, guiGraphics, yaw, lastDeathAngle.x, x, y, lastDeathText, 0.5F, offset, ColorHelper.decode("#b02e26").getRGB());
            }

            if (worldSpawn != null) {
                final Vec2 worldSpawnAngle = VecMath.angleFromPos(worldSpawn.getPosition(), posX, posY, posZ);

                drawInfo(mc, guiGraphics, yaw, worldSpawnAngle.x, x, y, worldSpawnText, 1.0F, offset, ColorHelper.decode("#5d7c15").getRGB());
            }
        });
    }

    private void drawCardinal(Minecraft mc, GuiGraphics guiGraphics, float yaw, float angle, int x, int y, String text) {
        drawInfo(mc, guiGraphics, yaw, angle, x, y, text, 1.0F, 1.0F, ColorHelper.decode("#FFFFFF").getRGB());
    }

    private void drawInfo(Minecraft mc, GuiGraphics guiGraphics, float yaw, float angle, int x, int y, String text, float size, float offset, int color) {
        Matrix3x2fStack matrix = guiGraphics.pose();
        int aDist = (int)VecMath.angleDistance(yaw, angle);
        float scale = (float) OverlayConfig.scale();
        float resize = 1 / size;
        int xPos;
        int yPos;

        if (Math.abs(aDist) <= (90 * offset)) {
            if (size == 1.0) {
                xPos = x + aDist;
                yPos = y;
            }
            else {
                xPos = (int)(x * resize) + aDist;
                yPos = (int)(y * resize);
            }

            if (size != 1.0) {
                matrix.pushMatrix();
                matrix.scale(size, size);
            }

            FontHelper.draw(mc, guiGraphics, text, xPos, yPos, color, FontHelper.TextType.NONE);

            if (size != 1.0) {
                matrix.popMatrix();
                matrix.scale(scale, scale);
            }
        }
    }

}
