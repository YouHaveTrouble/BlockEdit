package me.youhavetrouble.blockedit.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchematicLoadException extends SchematicException {

    public SchematicLoadException(@NotNull String message, @NotNull String schematicName, @Nullable Throwable cause) {
        super(message, schematicName, cause);
    }

    public SchematicLoadException(@NotNull String message, @NotNull String schematicName) {
        super(message, schematicName);
    }

}
