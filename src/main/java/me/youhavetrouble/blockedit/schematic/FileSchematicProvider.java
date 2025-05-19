package me.youhavetrouble.blockedit.schematic;

import org.jetbrains.annotations.NotNull;

public interface FileSchematicProvider<S extends Schematic> extends SchematicProvider<S> {

    /**
     * Get the file extensions of the schematic provider.
     */
    @NotNull String[] fileExtensions();

}
