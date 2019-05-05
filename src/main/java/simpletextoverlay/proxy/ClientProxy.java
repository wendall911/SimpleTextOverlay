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

import simpletextoverlay.command.SimpleTextOverlayCommand;
import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.core.Core;
import simpletextoverlay.event.ConfigEventHandler;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.event.KeyInputEventHandler;
import simpletextoverlay.network.PacketHandler;
import simpletextoverlay.tag.Tag;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.value.registry.ValueRegistry;

public class ClientProxy extends CommonProxy {

    private final Core core = Core.INSTANCE;

    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);

        ValueRegistry.INSTANCE.init();
        ConfigEventHandler.INSTANCE.applyConfigSettings();

        this.core.setConfigDirectory(event.getModConfigurationDirectory());
        this.core.loadConfig(ConfigHandler.client.general.overlayConfig);

        for (final KeyBinding keyBinding : KeyInputEventHandler.INSTANCE.KEY_BINDINGS) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        super.init(event);

        MinecraftForge.EVENT_BUS.register(GameOverlayEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ConfigEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(KeyInputEventHandler.INSTANCE);
        PacketHandler.initClient();
    }

    @Override
    public void postInit(final FMLPostInitializationEvent event) {
        super.postInit(event);

        TagRegistry.INSTANCE.init();
        ClientCommandHandler.instance.registerCommand(new SimpleTextOverlayCommand());
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
