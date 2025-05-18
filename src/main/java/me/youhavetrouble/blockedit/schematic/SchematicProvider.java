package me.youhavetrouble.blockedit.schematic;

import me.youhavetrouble.blockedit.util.Clipboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for schematic providers. Schematic providers are used to load and save schematics in different formats.
 */
public interface SchematicProvider<S extends Schematic> {

    /**
     * Get the name of the schematic provider. Can only contain lowercase letters and numbers.
     * @return The name of the schematic provider
     */
    @NotNull String name();

    /**
     * Get the file extensions of the schematic provider.
     */
    @NotNull String[] fileExtensions();

    /**
     * Save the schematic
     * @param schematic The schematic to save
     */
    void save(@NotNull S schematic);

    /**
     * Load a schematic
     * @param name Name of the schematic to load
     * @return The loaded schematic. Returns null if the schematic could not be loaded.
     */
    @Nullable S load(@NotNull String name);

    /**
     * Create a new schematic from the given clipboard
     * @param name Name of the schematic
     * @param clipboard Clipboard object containing the schematic
     * @return The created schematic
     */
    @NotNull S fromClipboard(@NotNull String name, @NotNull Clipboard clipboard);

}
