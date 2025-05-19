package me.youhavetrouble.blockedit.exception;

import me.youhavetrouble.blockedit.schematic.SchematicProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchematicHandlerRegistrationException extends IllegalArgumentException {

    private final SchematicProvider<?> schematicProvider;

    public SchematicHandlerRegistrationException(
            @NotNull String message,
            @Nullable SchematicProvider<?> schematicProvider
    ) {
        super(message);
        this.schematicProvider = schematicProvider;
    }

    @Nullable
    public SchematicProvider<?> getSchematicProvider() {
        return schematicProvider;
    }

}
