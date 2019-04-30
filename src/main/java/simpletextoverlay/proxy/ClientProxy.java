package simpletextoverlay.proxy;

import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import simpletextoverlay.SimpleTextOverlayCore;
import simpletextoverlay.command.SimpleTextOverlayCommand;
import simpletextoverlay.handler.ConfigurationHandler;
import simpletextoverlay.handler.KeyInputHandler;
import simpletextoverlay.handler.Ticker;
import simpletextoverlay.network.PacketHandler;
import simpletextoverlay.tag.Tag;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.value.registry.ValueRegistry;

public class ClientProxy extends CommonProxy {
    private final SimpleTextOverlayCore core = SimpleTextOverlayCore.INSTANCE;

    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);

        ValueRegistry.INSTANCE.init();

        this.core.setConfigDirectory(event.getModConfigurationDirectory());
        this.core.setConfigFile(ConfigurationHandler.configName);
        this.core.reloadConfig();

        ConfigurationHandler.propFileInterval.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);

        for (final KeyBinding keyBinding : KeyInputHandler.KEY_BINDINGS) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        super.init(event);

        MinecraftForge.EVENT_BUS.register(Ticker.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ConfigurationHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(KeyInputHandler.INSTANCE);
        ClientCommandHandler.instance.registerCommand(SimpleTextOverlayCommand.INSTANCE);

        PacketHandler.initClient();
    }

    @Override
    public void postInit(final FMLPostInitializationEvent event) {
        TagRegistry.INSTANCE.init();
    }

    @Override
    public void serverStarting(final FMLServerStartingEvent event) {
        Tag.setServer(event.getServer());
    }

    @Override
    public void serverStopping(final FMLServerStoppingEvent event) {
        Tag.setServer(null);
    }
}
