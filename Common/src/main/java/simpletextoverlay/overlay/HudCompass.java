package simpletextoverlay.overlay;

import java.util.Map;

import org.joml.Matrix3x2fStack;

import net.minecraft.client.gui.GuiGraphicsExtractor;
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

    public void renderText(GuiGraphicsExtractor guiGraphics, Minecraft mc, int scaledWidth, float _partialTicks) {
        final Player player = mc.player;

        if (player == null) {
            return;
        }

        final float partialTicks = mc.isPaused() ? 0 : _partialTicks;
        double posX = player.getX();
        double posY = player.getY();
        double posZ = player.getZ();
        final String compassText = "·";
        float yaw;

        final int x = Alignment.getCompassX(scaledWidth, mc.font.width(compassText));
        final int y = Alignment.getCompassY();

        final int bgColor = ColorHelper.rgb(0, 0, 0, OverlayConfig.getCompassOpacity());

        if (player.isPassenger()) {
            yaw = player.getYRot() % 360;
        }
        else {
            yaw = Mth.lerp(partialTicks, player.yRotO, player.getYRot()) % 360;
        }

        guiGraphics.fill(x - 92, y - 1, x + 96, mc.font.lineHeight + 2, bgColor);

        drawCardinal(mc, guiGraphics, yaw, 0, x, y, "S");
        drawCardinal(mc, guiGraphics, yaw, 90, x, y, "W");
        drawCardinal(mc, guiGraphics, yaw, 180, x, y, "N");
        drawCardinal(mc, guiGraphics, yaw, 270, x, y, "E");

        FontHelper.draw(mc, guiGraphics, compassText, x, y, ColorHelper.decode("#b02e26").getRGB(), false, FontHelper.TextType.NONE);

        Services.CAPABILITY_PLATFORM.getDataManagerCapability(player).ifPresent(pinsData -> {
            final Map<String, PinInfo<?>> pins = pinsData.get(player).getPins();
            float offset = 0.5F;

            for (PinType type : PinType.values()) {
                PinInfo<?> pin = pins.get(type.toString());
                if (pin != null) {
                    PinIcon icon = getPinIcon(type);

                    if (icon != null) {
                        Vec2 angle = VecMath.angleFromPos(pin.getPosition(), posX, posY, posZ);

                        drawInfo(mc, guiGraphics, yaw, angle.x, x, y, icon.getIcon(), icon.getSize(), offset, icon.getColor());
                    }
                }
            }
        });
    }

    private void drawCardinal(Minecraft mc, GuiGraphicsExtractor guiGraphics, float yaw, float angle, int x, int y, String text) {
        drawInfo(mc, guiGraphics, yaw, angle, x, y, text, 1.0F, 1.0F, ColorHelper.decode("#FFFFFF").getRGB());
    }

    private void drawInfo(Minecraft mc, GuiGraphicsExtractor guiGraphics, float yaw, float angle, int x, int y, String text, float size, float offset, int color) {
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

    public enum PinIcon {

        BEDSPAWN(PinType.BEDSPAWN, "#9c9d97", 0.5F),
        LASTDEATH(PinType.LASTDEATH, "#b02e26", 0.5F),
        WORLDSPAWN(PinType.WORLDSPAWN, "#5d7c15", 1.0F),
        WAYPOINT0(PinType.WAYPOINT0, "#3F67CD", 0.5F),
        WAYPOINT1(PinType.WAYPOINT1, "#F13AB2", 0.5F),
        WAYPOINT2(PinType.WAYPOINT2, "#0F952F", 0.5F),
        WAYPOINT3(PinType.WAYPOINT3, "#DC0EFA", 0.5F),
        WAYPOINT4(PinType.WAYPOINT4, "#4ACD2A", 0.5F),
        WAYPOINT5(PinType.WAYPOINT5, "#61DD4A", 0.5F),
        WAYPOINT6(PinType.WAYPOINT6, "#CD53AF", 0.5F),
        WAYPOINT7(PinType.WAYPOINT7, "#8DF4F2", 0.5F),
        WAYPOINT8(PinType.WAYPOINT8, "#325038", 0.5F),
        WAYPOINT9(PinType.WAYPOINT9, "#6BC5EE", 0.5F);

        private final PinType type;
        private final String color;
        private final float size;

        PinIcon(PinType type, String color, float size) {
            this.type = type;
            this.color = color;
            this.size = size;
        }

        public String getIcon() {
            if (this.type == PinType.BEDSPAWN) {
                return "⌂";
            }
            else if (this.type == PinType.LASTDEATH) {
                return "✕";
            }
            else if (this.type == PinType.WORLDSPAWN) {
                return "⊙";
            }
            else {
                return "⯆";
            }
        }

        public int getColor() {
            return ColorHelper.decode(this.color).getRGB();
        }

        public float getSize() {
            return this.size;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

    }

    public PinIcon getPinIcon(PinType pinType) {
        for (PinIcon icon : PinIcon.values()) {
            if (icon.type == pinType) {
                return icon;
            }
        }

        return null;
    }

}
