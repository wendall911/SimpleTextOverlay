package simpletextoverlay.util;

import net.minecraft.client.renderer.BufferBuilder;

public class GuiHelper {

    public static void drawTexturedRectangle(final BufferBuilder buffer, final double x0, final double y0, final double x1, final double y1, final double z, final double u0, final double v0, final double u1, final double v1) {
        buffer.vertex(x0, y0, z).vertex(u0, v0, z).endVertex();
        buffer.vertex(x0, y1, z).vertex(u0, v1, z).endVertex();
        buffer.vertex(x1, y1, z).vertex(u1, v1, z).endVertex();
        buffer.vertex(x1, y0, z).vertex(u1, v0, z).endVertex();
        //buffer.pos(x0, y0, z).tex(u0, v0).endVertex();
        //buffer.pos(x0, y1, z).tex(u0, v1).endVertex();
        //buffer.pos(x1, y1, z).tex(u1, v1).endVertex();
        //buffer.pos(x1, y0, z).tex(u1, v0).endVertex();
    }

}
