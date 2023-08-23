package simpletextoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import sereneseasons.config.ServerConfig;
import sereneseasons.api.season.Season.SubSeason;
import sereneseasons.api.season.SeasonHelper;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.ColorHelper;
import simpletextoverlay.util.FontHelper;


public class SeasonInfo extends Info {

    public SeasonInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(PoseStack matrix, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        if (ServerConfig.isDimensionWhitelisted(mc.level.dimension())) {
            SubSeason subSeason = SeasonHelper.getSeasonState(mc.level).getSubSeason();

            Component seasonName = Component.translatable("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            int x = Alignment.getX(scaledWidth, mc.font.width(super.label) + mc.font.width(seasonName));
            int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

            FontHelper.draw(mc, matrix, seasonName, x, y, ColorHelper.getSeasonColor(subSeason));
        }
    }

}