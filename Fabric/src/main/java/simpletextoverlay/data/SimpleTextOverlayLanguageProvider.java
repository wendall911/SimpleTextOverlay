package simpletextoverlay.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup.Provider;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.common.Translations;

public class SimpleTextOverlayLanguageProvider extends FabricLanguageProvider {

    protected SimpleTextOverlayLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(Provider provider, TranslationBuilder translationBuilder) {
        addConfigurationTitle(translationBuilder, "Simple Text Overlay");
        addConfigurationName(translationBuilder, "position", "Position");
        addConfigurationDescription(translationBuilder, "position");
        addConfigurationName(translationBuilder, "offsetx", "X Offset");
        addConfigurationDescription(translationBuilder, "offsetx");
        addConfigurationName(translationBuilder, "offsety", "Y Offset");
        addConfigurationDescription(translationBuilder, "offsety");
        addConfigurationName(translationBuilder, "scale", "Scale");
        addConfigurationDescription(translationBuilder, "scale");
        addConfigurationName(translationBuilder, "fields", "Fields");
        addConfigurationDescription(translationBuilder, "fields");
        addConfigurationName(translationBuilder, "textshadow", "Text Shadow");
        addConfigurationDescription(translationBuilder, "textshadow");
        addConfigurationName(translationBuilder, "labelcolor", "Label Color");
        addConfigurationDescription(translationBuilder, "labelcolor");
        addConfigurationName(translationBuilder, "lightlabel", "Light Label");
        addConfigurationDescription(translationBuilder, "lightlabel");
        addConfigurationName(translationBuilder, "lightcolorrange", "Light Color Range");
        addConfigurationDescription(translationBuilder, "lightcolorrange");
        addConfigurationName(translationBuilder, "timelabel", "Time Label");
        addConfigurationDescription(translationBuilder, "timelabel");
        addConfigurationName(translationBuilder, "timecolorrange", "Time Color Range");
        addConfigurationDescription(translationBuilder, "timecolorrange");
        addConfigurationName(translationBuilder, "timeuse12", "Use 12 Hour Display");
        addConfigurationDescription(translationBuilder, "timeuse12");
        addConfigurationName(translationBuilder, "footlabel", "Foot Level Label");
        addConfigurationDescription(translationBuilder, "footlabel");
        addConfigurationName(translationBuilder, "footcolor", "Foot Level Color");
        addConfigurationDescription(translationBuilder, "footcolor");
        addConfigurationName(translationBuilder, "biomelabel", "Biome Label");
        addConfigurationDescription(translationBuilder, "biomelabel");
        addConfigurationName(translationBuilder, "biomecolor", "Biome Color");
        addConfigurationDescription(translationBuilder, "biomecolor");
        addConfigurationName(translationBuilder, "enabled", "Enable HUD Compass");
        addConfigurationDescription(translationBuilder, "enabled");
        addConfigurationName(translationBuilder, "compassopacity", "Compass Opacity");
        addConfigurationDescription(translationBuilder, "compassopacity");
        addConfigurationName(translationBuilder, "dayslabel", "Days Label");
        addConfigurationDescription(translationBuilder, "dayslabel");
        addConfigurationName(translationBuilder, "dayscolor", "Days Color");
        addConfigurationDescription(translationBuilder, "dayscolor");
        addDescription(translationBuilder, "early_spring", "Early Spring");
        addDescription(translationBuilder, "mid_spring", "Mid Spring");
        addDescription(translationBuilder, "late_spring", "Late Spring");
        addDescription(translationBuilder, "early_summer", "Early Summer");
        addDescription(translationBuilder, "mid_summer", "Mid Summer");
        addDescription(translationBuilder, "late_summer", "Late Summer");
        addDescription(translationBuilder, "early_autumn", "Early Autumn");
        addDescription(translationBuilder, "mid_autumn", "Mid Autumn");
        addDescription(translationBuilder, "late_autumn", "Late Autumn");
        addDescription(translationBuilder, "early_winter", "Early Winter");
        addDescription(translationBuilder, "mid_winter", "Mid Winter");
        addDescription(translationBuilder, "late_winter", "Late Winter");
        addKey(translationBuilder, "death_history", "Death History");
    }

    private void addConfigurationTitle(TranslationBuilder builder, String title) {
        builder.add(SimpleTextOverlay.MODID + ".configuration.title", title);
    }

    private void addConfigurationName(TranslationBuilder builder, String id, String name) {
        builder.add(SimpleTextOverlay.MODID + ".configuration." + id + ".name", name);
    }

    private void addConfigurationDescription(TranslationBuilder builder, String id) {
        builder.add(SimpleTextOverlay.MODID + ".configuration." + id + ".description", Translations.get(id));
    }

    private void addDescription(TranslationBuilder builder, String id, String name) {
        builder.add("desc." + SimpleTextOverlay.MODID + "." + id, name);
    }

    private void addKey(TranslationBuilder builder, String id, String name) {
        builder.add("key." + SimpleTextOverlay.MODID + "." + id, name);
    }

}
