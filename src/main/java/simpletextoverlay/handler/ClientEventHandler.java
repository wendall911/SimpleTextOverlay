package simpletextoverlay.handler;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.ParseResults;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import simpletextoverlay.command.FileNameArgument;
import simpletextoverlay.command.SimpleTextOverlayCommand;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.reference.Names;
import simpletextoverlay.tag.Tag;
import simpletextoverlay.util.PacketHandlerHelper;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public void preInit(final FMLServerAboutToStartEvent event) {
        SimpleTextOverlay.logger.warn("FMLServerAboutToStartEvent");
    }

    @SubscribeEvent
    public void serverStarting(final FMLServerStartingEvent event) {
        Tag.setServer(event.getServer());

        event.getCommandDispatcher().register(
            literal(Names.Command.SHORT_NAME)
                .then(literal(Names.Command.LOAD)
                    .then(argument("file", FileNameArgument.fileNameArgument())
                    .executes(ctx -> SimpleTextOverlayCommand.execute(ctx))))
                .then(literal(Names.Command.RELOAD).executes(ctx -> SimpleTextOverlayCommand.execute(ctx)))
                .then(literal(Names.Command.SAVE)
                    .then(argument("file", FileNameArgument.fileNameArgument())
                    .executes(ctx -> SimpleTextOverlayCommand.execute(ctx))))
                .then(literal(Names.Command.ENABLE).executes(ctx -> SimpleTextOverlayCommand.execute(ctx)))
                .then(literal(Names.Command.DISABLE).executes(ctx -> SimpleTextOverlayCommand.execute(ctx)))
                .then(literal(Names.Command.CYCLE).executes(ctx -> SimpleTextOverlayCommand.execute(ctx)))
        );

    }

    @SubscribeEvent
    public void serverStopping(final FMLServerStoppingEvent event) {
        Tag.setServer(null);
    }

    @SubscribeEvent
    public static void command(CommandEvent event) {
        ParseResults<CommandSource> results = event.getParseResults();
        List<ParsedCommandNode<CommandSource>> nodes = results.getContext().getNodes();
        CommandDispatcher<CommandSource> dispatcher = results.getContext().getDispatcher();

        if (nodes.get(0).getNode() == dispatcher.getRoot().getChild("sto")) {
            ITextComponent msg = new StringTextComponent("Only works in single player worlds for now.");
            event.setException(new CommandException(msg));
            event.setCanceled(true);
        }
    }

}

