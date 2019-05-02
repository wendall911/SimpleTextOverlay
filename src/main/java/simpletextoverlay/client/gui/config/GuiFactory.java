package simpletextoverlay.client.gui.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.reference.Names;
import simpletextoverlay.reference.Reference;
import simpletextoverlay.util.GuiConfigComplex;

public class GuiFactory implements IModGuiFactory {
    @Override
    public void initialize(final Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiModConfig(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public static class GuiModConfig extends GuiConfigComplex {
        public GuiModConfig(final GuiScreen guiScreen) {
            super(guiScreen, Reference.MODID, ConfigHandler.configuration, Names.Config.LANG_PREFIX);
        }
    }
}
