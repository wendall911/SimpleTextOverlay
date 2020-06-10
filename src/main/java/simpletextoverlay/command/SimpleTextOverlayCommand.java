package simpletextoverlay.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.ParseResults;

import java.util.List;

import net.minecraft.command.CommandSource;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.reference.Names;
import simpletextoverlay.SimpleTextOverlay;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID)
public final class SimpleTextOverlayCommand {

    @SubscribeEvent
    public static void register(FMLServerStartingEvent event) {
        event.getCommandDispatcher().register(
            literal(Names.Command.SHORT_NAME)
                .then(literal(Names.Command.LOAD)
                    .then(argument("file", FileNameArgument.fileNameArgument())
                    .executes(ctx -> execute(ctx))))
                .then(literal(Names.Command.RELOAD).executes(ctx -> execute(ctx)))
                .then(literal(Names.Command.SAVE)
                    .then(argument("file", FileNameArgument.fileNameArgument())
                    .executes(ctx -> execute(ctx))))
                .then(literal(Names.Command.ENABLE).executes(ctx -> execute(ctx)))
                .then(literal(Names.Command.DISABLE).executes(ctx -> execute(ctx)))
                .then(literal(Names.Command.CYCLE).executes(ctx -> execute(ctx)))
        );
    }

    @SubscribeEvent
    public static void command(CommandEvent event) {
        ParseResults<CommandSource> results = event.getParseResults();
        List<ParsedCommandNode<CommandSource>> nodes = results.getContext().getNodes();
        CommandDispatcher<CommandSource> dispatcher = results.getContext().getDispatcher();

        if (nodes.get(0).getNode() == dispatcher.getRoot().getChild("sto")) {
            if (OverlayConfig.side == OverlayConfig.Side.SERVER) {
                ITextComponent msg = new StringTextComponent("Only works in single player worlds for now.");
                event.setException(new CommandException(msg));
                event.setCanceled(true);
            }
        }
    }

	static void sendSuccess(CommandSource source, ITextComponent msg) {
        source.sendSuccess(msg, false);
	}

    static void sendFailure(CommandSource source, ITextComponent msg) {
        source.sendFailure(msg);
    }

    private static int execute(CommandContext<CommandSource> context) {
        OverlayManager overlayManager = OverlayManager.INSTANCE;
        List<ParsedCommandNode<CommandSource>> nodes = context.getNodes();

        ITextComponent msg = new StringTextComponent(Names.Command.Message.USAGE);
        boolean success = false;

        if (nodes.size() > 1) {
            String command = nodes.get(1).getNode().getName();

            if (command.equalsIgnoreCase(Names.Command.RELOAD)) {
                sendSuccess(context.getSource(), new StringTextComponent(Names.Command.Message.RELOAD));
                success = overlayManager.reloadOverlayFile();
                msg = new StringTextComponent(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE);
            } else if (command.equalsIgnoreCase(Names.Command.ENABLE)) {
                msg = new StringTextComponent(Names.Command.Message.ENABLE);
                GameOverlayEventHandler.INSTANCE.enabled = true;
                success = true;
            } else if (command.equalsIgnoreCase(Names.Command.DISABLE)) {
                msg = new StringTextComponent(Names.Command.Message.DISABLE);
                GameOverlayEventHandler.INSTANCE.enabled = false;
                success = true;
            } else if (command.equalsIgnoreCase(Names.Command.LOAD)) {
                if (nodes.size() >= 2) {
                    sendSuccess(context.getSource(), new StringTextComponent(String.format(Names.Command.Message.LOAD, nodes.get(2).getNode().getName())));
                    success = overlayManager.loadOverlayFile(context.getArgument("file", String.class), false);
                    msg = new StringTextComponent(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE);
                } else {
                    msg = new StringTextComponent(Names.Command.Message.MISSING);
                }
            } else if (command.equalsIgnoreCase(Names.Command.SAVE)) {
                if (nodes.size() >= 2) {
                    sendSuccess(context.getSource(), new StringTextComponent(String.format(Names.Command.Message.SAVE, nodes.get(2).getNode().getName())));
                    success = overlayManager.saveConfig(context.getArgument("file", String.class));
                    msg = new StringTextComponent(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE);
                } else {
                    msg = new StringTextComponent(Names.Command.Message.MISSING);
                }
            } else if (command.equalsIgnoreCase(Names.Command.CYCLE)) {
                success = overlayManager.cycleOverlay();
                sendSuccess(context.getSource(), new StringTextComponent(String.format(Names.Command.Message.LOAD, overlayManager.getOverlayFile())));
                msg = new StringTextComponent(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE);
            }
            else {
                msg = new StringTextComponent(Names.Command.Message.UNKNOWN);
            }
        }

        if (success) {
            sendSuccess(context.getSource(), msg);
            return 1;
        }
        else {
            sendFailure(context.getSource(), msg);
            return 0;
        }

    }

}
