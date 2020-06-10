package simpletextoverlay.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.command.ISuggestionProvider;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

public class FileNameArgument implements ArgumentType<String> {
    public static FileNameArgument fileNameArgument() {
        return new FileNameArgument();
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }
}
