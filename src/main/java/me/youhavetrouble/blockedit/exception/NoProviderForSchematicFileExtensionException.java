package me.youhavetrouble.blockedit.exception;

import org.jetbrains.annotations.NotNull;

public class NoProviderForSchematicFileExtensionException extends SchematicLoadException {

    private String extension;

    public NoProviderForSchematicFileExtensionException(
            @NotNull String message,
            @NotNull String schematicName,
            @NotNull String extension
    ) {
        super(message, schematicName);
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

}
