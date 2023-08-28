package simpletextoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Map;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.ColorHelper;
import simpletextoverlay.util.FontHelper;
import simpletextoverlay.util.VecMath;

import static simpletextoverlay.events.SimpleTextOverlayEvents.BEDSPAWN;
import static simpletextoverlay.events.SimpleTextOverlayEvents.LASTDEATH;
import static simpletextoverlay.events.SimpleTextOverlayEvents.WORLDSPAWN;

public class HudCompass extends GuiComponent {

    public HudCompass() {}

    public void renderText(PoseStack matrix, Minecraft mc, int scaledWidth, int scaledHeight, float _partialTicks) {
        final Player player = mc.player;

        if (player == null) {
            return;
        }
        final float partialTicks = mc.isPaused() ? 0 : _partialTicks;
        final double posX = Mth.lerp(partialTicks, player.xo, player.getX());
        final double posY = Mth.lerp(partialTicks, player.yo, player.getY());
        final double posZ = Mth.lerp(partialTicks, player.zo, player.getZ());
        final String compassText = "·";
        final String worldSpawnText = "⊙";
        final String bedSpawnText = "⌂";
        final String lastDeathText = "✕";
        final float yaw = Mth.lerp(partialTicks, player.yRotO, player.getYRot()) % 360;

        final int x = Alignment.getCompassX(scaledWidth, mc.font.width(compassText));
        final int y = Alignment.getCompassY();

        final int bgColor = ColorHelper.rgb(0, 0, 0, OverlayConfig.getCompassOpacity());

        fill(matrix, x - 92, y - 1, x + 96, mc.font.lineHeight + 2, bgColor);

        drawCardinal(mc, matrix, yaw, 0, x, y, "S");
        drawCardinal(mc, matrix, yaw, 90, x, y, "W");
        drawCardinal(mc, matrix, yaw, 180, x, y, "N");
        drawCardinal(mc, matrix, yaw, 270, x, y, "E");

        FontHelper.draw(mc, matrix, compassText, x, y, ColorHelper.decode("#b02e26").getRGB(), false);

        Services.CAPABILITY_PLATFORM.getDataManagerCapability(player).ifPresent(pinsData -> {
            final Map<String, PinInfo<?>> pins = pinsData.get(player.level).getPins();
            float offset = 0.5F;
            final PinInfo<?> bedSpawn = pins.get(BEDSPAWN);
            final PinInfo<?> lastDeath = pins.get(LASTDEATH);
            final PinInfo<?> worldSpawn = pins.get(WORLDSPAWN);

            if (bedSpawn != null) {

                final Vec2 bedSpawnAngle = VecMath.angleFromPos(bedSpawn.getPosition(), posX, posY, posZ);

                drawInfo(mc, matrix, yaw, bedSpawnAngle.x, x, y + 3, bedSpawnText, 0.5F, offset, ColorHelper.decode("#9c9d97").getRGB());
            }

            if (lastDeath != null) {
                final Vec2 lastDeathAngle = VecMath.angleFromPos(lastDeath.getPosition(), posX, posY, posZ);

                drawInfo(mc, matrix, yaw, lastDeathAngle.x, x, y, lastDeathText, 0.5F, offset, ColorHelper.decode("#b02e26").getRGB());
            }

            if (worldSpawn != null) {
                final Vec2 worldSpawnAngle = VecMath.angleFromPos(worldSpawn.getPosition(), posX, posY, posZ);

                drawInfo(mc, matrix, yaw, worldSpawnAngle.x, x, y, worldSpawnText, 1.0F, offset, ColorHelper.decode("#5d7c15").getRGB());
            }
        });
    }

    private void drawCardinal(Minecraft mc, PoseStack matrix, float yaw, float angle, int x, int y, String text) {
        drawInfo(mc, matrix, yaw, angle, x, y, text, 1.0F, 1.0F, ColorHelper.decode("#FFFFFF").getRGB());
    }

    private void drawInfo(Minecraft mc, PoseStack matrix, float yaw, float angle, int x, int y, String text, float size, float offset, int color) {
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
                matrix.pushPose();
                matrix.scale(size, size, size);
            }

            FontHelper.draw(mc, matrix, text, xPos, yPos, color);

            if (size != 1.0) {
                matrix.popPose();
                matrix.scale(scale, scale, scale);
            }
        }
    }

}
