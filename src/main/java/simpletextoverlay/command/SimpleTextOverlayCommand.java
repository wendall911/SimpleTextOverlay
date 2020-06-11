package simpletextoverlay.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.reference.Names;
import simpletextoverlay.SimpleTextOverlay;

public final class SimpleTextOverlayCommand {

	static void sendSuccess(CommandSource source, ITextComponent msg) {
        source.sendSuccess(msg, false);
	}

    static void sendFailure(CommandSource source, ITextComponent msg) {
        source.sendFailure(msg);
    }

    public static int execute(CommandContext<CommandSource> context) {
        OverlayManager overlayManager = OverlayManager.INSTANCE;
        List<ParsedCommandNode<CommandSource>> nodes = context.getNodes();

        ITextComponent msg = new StringTextComponent(Names.Command.Message.USAGE);
        boolean success = false;

        if (nodes.size() > 1) {
            String command = nodes.get(1).getNode().getName();

            if (command.equalsIgnoreCase(Names.Command.RELOAD)) {
                sendSuccess(context.getSource(), new StringTextComponent(I18n.get(Names.Command.Message.RELOAD)));
                success = overlayManager.reloadOverlayFile();
                msg = new StringTextComponent(success ? I18n.get(Names.Command.Message.SUCCESS) : I18n.get(Names.Command.Message.FAILURE));
            } else if (command.equalsIgnoreCase(Names.Command.ENABLE)) {
                msg = new StringTextComponent(I18n.get(Names.Command.Message.ENABLE));
                GameOverlayEventHandler.INSTANCE.enabled = true;
                success = true;
            } else if (command.equalsIgnoreCase(Names.Command.DISABLE)) {
                msg = new StringTextComponent(I18n.get(Names.Command.Message.DISABLE));
                GameOverlayEventHandler.INSTANCE.enabled = false;
                success = true;
            } else if (command.equalsIgnoreCase(Names.Command.LOAD)) {
                if (nodes.size() >= 2) {
                    sendSuccess(context.getSource(), new StringTextComponent(I18n.get(Names.Command.Message.LOAD, nodes.get(2).getNode().getName())));
                    success = overlayManager.loadOverlayFile(context.getArgument("file", String.class), false);
                    msg = new StringTextComponent(success ? I18n.get(Names.Command.Message.SUCCESS) : I18n.get(Names.Command.Message.FAILURE));
                } else {
                    msg = new StringTextComponent(I18n.get(Names.Command.Message.MISSING));
                }
            } else if (command.equalsIgnoreCase(Names.Command.SAVE)) {
                if (nodes.size() >= 2) {
                    sendSuccess(context.getSource(), new StringTextComponent(I18n.get(Names.Command.Message.SAVE, nodes.get(2).getNode().getName())));
                    success = overlayManager.saveConfig(context.getArgument("file", String.class));
                    msg = new StringTextComponent(success ? I18n.get(Names.Command.Message.SUCCESS) : I18n.get(Names.Command.Message.FAILURE));
                } else {
                    msg = new StringTextComponent(I18n.get(Names.Command.Message.MISSING));
                }
            } else if (command.equalsIgnoreCase(Names.Command.CYCLE)) {
                success = overlayManager.cycleOverlay();
                sendSuccess(context.getSource(), new StringTextComponent(I18n.get(Names.Command.Message.LOAD, overlayManager.getOverlayFile())));
                msg = new StringTextComponent(success ? I18n.get(Names.Command.Message.SUCCESS) : I18n.get(Names.Command.Message.FAILURE));
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
