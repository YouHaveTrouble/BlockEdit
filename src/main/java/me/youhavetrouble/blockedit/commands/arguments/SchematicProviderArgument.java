package me.youhavetrouble.blockedit.commands.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.youhavetrouble.blockedit.SchematicHandler;
import me.youhavetrouble.blockedit.commands.exceptiontype.UnknownProviderExceptionType;
import me.youhavetrouble.blockedit.schematic.Schematic;
import me.youhavetrouble.blockedit.schematic.SchematicProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class SchematicProviderArgument implements CustomArgumentType<SchematicProvider<? extends Schematic>, String> {

    private final SchematicHandler<?> schematicHandler;

    public SchematicProviderArgument(@NotNull SchematicHandler<?> schematicHandler) {
        this.schematicHandler = schematicHandler;
    }

    @Override
    public @NotNull SchematicProvider<? extends Schematic> parse(StringReader reader) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        String arg = reader.getString().toLowerCase(Locale.ENGLISH);

        if (!this.schematicHandler.getSchematicProvidersList().contains(arg)) {
            throw new CommandSyntaxException(
                    new UnknownProviderExceptionType(),
                    new LiteralMessage("Provider not found"),
                    arg,
                    cursor
            );
        }

        return schematicHandler.getSchematicProviderByName(arg);
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for (String providerName : schematicHandler.getSchematicProvidersList()) {
            if (!providerName.startsWith(builder.getRemaining())) continue;
            builder.suggest(providerName);
        }
        return builder.buildFuture();
    }
}
