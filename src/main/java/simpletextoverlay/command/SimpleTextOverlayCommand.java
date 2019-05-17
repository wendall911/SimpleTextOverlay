package simpletextoverlay.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import simpletextoverlay.client.gui.overlay.OverlayManager;
import simpletextoverlay.client.gui.tag.GuiTags;
import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.event.TagOverlayEventHandler;
import simpletextoverlay.reference.Names;

public class SimpleTextOverlayCommand extends CommandBase {

    private final OverlayManager overlayManager = OverlayManager.INSTANCE;

    @Override
    public String getName() {
        return Names.Command.NAME;
    }

    @Override
    public List<String> getAliases() {
        return Lists.newArrayList(Names.Command.SHORT_NAME);
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return Names.Command.Message.USAGE;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, final String[] args, final @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, getCommandList());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(Names.Command.LOAD)) {
                return getListOfStringsMatchingLastWord(args, getFilenames());
            }
            else if (args[0].equalsIgnoreCase(Names.Command.SAVE)) {
                return getListOfStringsMatchingLastWord(args, Names.Files.FILE_JSON);
            }
        }

        return Collections.emptyList();
    }

    private List<String> getCommandList() {
        final List<String> commands = new ArrayList<>();

        commands.add(Names.Command.RELOAD);
        commands.add(Names.Command.LOAD);
        commands.add(Names.Command.SAVE);
        commands.add(Names.Command.ENABLE);
        commands.add(Names.Command.DISABLE);
        commands.add(Names.Command.TAGLIST);
        commands.add(Names.Command.CYCLE);

        return commands;
    }

    private List<String> getFilenames() {
        final File[] files = this.overlayManager.getConfigDirectory().listFiles(
                (File dir, String name) ->
                    name.endsWith(Names.Files.EXT_JSON) &&
                    !name.contains(ConfigHandler.client.general.debugOverlayFile));

        final List<String> filenames = new ArrayList<>();

        for (final File file : files) {
            filenames.add(file.getName());
        }

        for (final String filename : Names.Files.BUILTINS) {
            filenames.add(filename);
        }

        for (final String filename : Names.Files.DEBUG_BUILTINS) {
            filenames.add(filename);
        }

        return filenames;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(Names.Command.RELOAD)) {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.RELOAD));
                final boolean success = this.overlayManager.reloadOverlayFile();
                sender.sendMessage(new TextComponentTranslation(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE));
                return;
            }
            else if (args[0].equalsIgnoreCase(Names.Command.ENABLE)) {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.ENABLE));
                GameOverlayEventHandler.INSTANCE.enabled = true;
                return;
            }
            else if (args[0].equalsIgnoreCase(Names.Command.DISABLE)) {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.DISABLE));
                GameOverlayEventHandler.INSTANCE.enabled = false;
                return;
            }
            else if (args[0].equalsIgnoreCase(Names.Command.TAGLIST)) {
                TagOverlayEventHandler.create(new GuiTags(), true);
                return;
            }
            else if (args[0].equalsIgnoreCase(Names.Command.LOAD)) {
                if (args.length == 2) {
                    sender.sendMessage(new TextComponentTranslation(Names.Command.Message.LOAD, args[1]));
                    final boolean success = this.overlayManager.loadOverlayFile(args[1], false);
                    sender.sendMessage(new TextComponentTranslation(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE)); return; } else { sender.sendMessage(new TextComponentTranslation(Names.Command.Message.MISSING)); return; } }
            else if (args[0].equalsIgnoreCase(Names.Command.SAVE)) {
                if (args.length == 2) {
                    sender.sendMessage(new TextComponentTranslation(Names.Command.Message.SAVE, args[1]));
                    final boolean success = this.overlayManager.saveConfig(args[1]);
                    sender.sendMessage(new TextComponentTranslation(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE));
                    return;
                }
                else {
                    sender.sendMessage(new TextComponentTranslation(Names.Command.Message.MISSING));
                    return;
                }
            }
            else if (args[0].equalsIgnoreCase(Names.Command.CYCLE)) {
                final boolean success = overlayManager.cycleOverlay();
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.LOAD, overlayManager.getOverlayFile()));
                sender.sendMessage(new TextComponentTranslation(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE));
                return;
            }
            else {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.UNKNOWN));
                return;
            }
        }

        throw new WrongUsageException(getUsage(sender), Names.Command.SHORT_NAME);
    }

}
