package simpletextoverlay.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import simpletextoverlay.SimpleTextOverlayCore;
import simpletextoverlay.client.gui.tag.GuiTags;
import simpletextoverlay.handler.ConfigurationHandler;
import simpletextoverlay.handler.Ticker;
import simpletextoverlay.reference.Names;
import simpletextoverlay.util.DelayedGuiDisplayTicker;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SimpleTextOverlayCommand extends CommandBase {
    public static final SimpleTextOverlayCommand INSTANCE = new SimpleTextOverlayCommand();

    private final SimpleTextOverlayCore core = SimpleTextOverlayCore.INSTANCE;

    private SimpleTextOverlayCommand() {}

    @Override
    public String getName() {
        return Names.Command.NAME;
    }

    @Override
    public String getUsage(final ICommandSender sender) {
        return Names.Command.Message.USAGE;
    }

    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, final @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Names.Command.RELOAD, Names.Command.LOAD, Names.Command.SAVE, Names.Command.ENABLE, Names.Command.DISABLE, Names.Command.TAGLIST);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(Names.Command.LOAD)) {
                return getListOfStringsMatchingLastWord(args, getFilenames());
            } else if (args[0].equalsIgnoreCase(Names.Command.SAVE)) {
                return getListOfStringsMatchingLastWord(args, Names.Files.FILE_JSON);
            }
        }

        return Collections.emptyList();
    }

    private List<String> getFilenames() {
        final File[] files = this.core.getConfigDirectory().listFiles((File dir, String name) -> name.startsWith(Names.Files.NAME) && (name.endsWith(Names.Files.EXT_JSON)));

        final List<String> filenames = new ArrayList<>();
        filenames.add("default");
        for (final File file : files) {
            filenames.add(file.getName());
        }

        return filenames;
    }

    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(Names.Command.RELOAD)) {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.RELOAD));
                ConfigurationHandler.reload();
                final boolean success = this.core.reloadConfig();
                sender.sendMessage(new TextComponentTranslation(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE));
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.ENABLE)) {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.ENABLE));
                Ticker.enabled = true;
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.DISABLE)) {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.DISABLE));
                Ticker.enabled = false;
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.TAGLIST)) {
                DelayedGuiDisplayTicker.create(new GuiTags(), 10);
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.LOAD)) {
                if (args.length == 2) {
                    sender.sendMessage(new TextComponentTranslation(Names.Command.Message.LOAD, args[1]));
                    final boolean success = this.core.loadConfig(args[1]);
                    sender.sendMessage(new TextComponentTranslation(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE));
                    if (success) {
                        ConfigurationHandler.setConfigName(args[1]);
                        ConfigurationHandler.save();
                    }
                    return;
                }
                else {
                    sender.sendMessage(new TextComponentTranslation(Names.Command.Message.MISSING));
                    return;
                }
            } else if (args[0].equalsIgnoreCase(Names.Command.SAVE)) {
                if (args.length == 2) {
                    sender.sendMessage(new TextComponentTranslation(Names.Command.Message.SAVE, args[1]));
                    final boolean success = this.core.saveConfig(args[1]);
                    sender.sendMessage(new TextComponentTranslation(success ? Names.Command.Message.SUCCESS : Names.Command.Message.FAILURE));
                    return;
                }
                else {
                    sender.sendMessage(new TextComponentTranslation(Names.Command.Message.MISSING));
                    return;
                }
            }
            else {
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.UNKNOWN));
                return;
            }
        }

        throw new WrongUsageException(getUsage(sender));
    }
}
