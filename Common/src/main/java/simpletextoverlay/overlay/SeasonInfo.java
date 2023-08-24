package simpletextoverlay.overlay;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import simpletextoverlay.platform.Services;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.ColorHelper;
import simpletextoverlay.util.FontHelper;
import simpletextoverlay.util.SubSeason;

public class SeasonInfo extends Info {

    public SeasonInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(GuiGraphics guiGraphics, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        Pair<Component, SubSeason> seasonInfo = Services.SEASON_INFO.getSeasonName(mc, pos);

        if (seasonInfo != null) {
            SubSeason subSeason = seasonInfo.getSecond();
            Component seasonName = seasonInfo.getFirst();

            int x = Alignment.getX(scaledWidth, mc.font.width(super.label) + mc.font.width(seasonName));
            int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

            FontHelper.draw(mc, guiGraphics, seasonName, x, y, ColorHelper.getSeasonColor(subSeason));
        }
    }

}
