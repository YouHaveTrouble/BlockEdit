package me.youhavetrouble.blockedit.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchematicException extends RuntimeException {

    private final String schematicName;

    public SchematicException(@NotNull String message, @Nullable String schematicName, Throwable cause) {
        super(message);
        this.schematicName = schematicName;
        initCause(cause);
    }

    public SchematicException(@NotNull String message, @Nullable String schematicName) {
        super(message);
        this.schematicName = schematicName;
    }

    public String getSchematicName() {
        return schematicName;
    }

}
