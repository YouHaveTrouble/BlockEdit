package me.youhavetrouble.blockedit.exception;

import org.jetbrains.annotations.NotNull;

public class SchematicSaveException extends SchematicException {

    public SchematicSaveException(@NotNull String message, @NotNull String schematicName, Throwable cause) {
        super(message, schematicName, cause);
    }

    public SchematicSaveException(@NotNull String message, @NotNull String schematicName) {
        super(message, schematicName);
    }
}
