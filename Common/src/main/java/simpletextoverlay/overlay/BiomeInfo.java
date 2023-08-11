package simpletextoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.world.level.biome.Biome;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.FontHelper;

import java.util.Objects;

public class BiomeInfo extends Info {

    public BiomeInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(PoseStack matrix, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        Holder<Biome> biome = Objects.requireNonNull(mc.level).getBiome(pos);

        biome.unwrapKey().ifPresent(key -> {
            Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));

            int x = Alignment.getX(scaledWidth, mc.font.width(super.label) + mc.font.width(biomeName));
            int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

            FontHelper.draw(mc, matrix, super.label, x, y, OverlayConfig.labelColor().getRGB());

            x = x + mc.font.width(super.label);

            FontHelper.draw(mc, matrix, biomeName, x, y, OverlayConfig.biomeColor().getRGB());
        });
    }

}
