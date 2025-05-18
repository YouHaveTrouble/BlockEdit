package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.schematic.Schematic;
import me.youhavetrouble.blockedit.schematic.SchematicProvider;
import me.youhavetrouble.blockedit.util.Clipboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SchematicHandler<S extends Schematic> {

    private final BlockEdit plugin;

    private final Map<String, SchematicProvider<S>> schematicProvidersByExtension = new HashMap<>();
    private final Map<String, SchematicProvider<S>> schematicProvidersByName = new HashMap<>();

    private final File schematicsDirectory;

    protected SchematicHandler(BlockEdit plugin) {
        this.plugin = plugin;
        this.schematicsDirectory = new File(plugin.getDataFolder(), "schematics");
        createSchematicsDirectory();
    }

    private void createSchematicsDirectory() {
        try {
            this.schematicsDirectory.mkdirs();
        } catch (SecurityException e) {
            plugin.getLogger().warning("Could not create schematics directory. Make sure server has read/write access to the plugin folder.");
        }
    }

    public void registerSchematicProvider(
            @NotNull SchematicProvider<S> schematicProvider
    ) throws IllegalArgumentException {
        if (!schematicProvider.name().matches("^[a-z0-9]+$")) {
            throw new IllegalArgumentException("Schematic provider name can only contain lowercase letters and numbers");
        }

        if (schematicProvidersByName.containsKey(schematicProvider.name())) {
            throw new IllegalArgumentException("Schematic provider " + schematicProvider.name() + " is already registered");
        }

        // Loop extensions to verify if they're valid and not already registered
        for (String extension : schematicProvider.fileExtensions()) {
            String trimmedExtension = extension.trim();
            if (trimmedExtension.isEmpty()) throw new IllegalArgumentException("File extension cannot be empty");
            if (!trimmedExtension.matches("^[a-z0-9]+$")) throw new IllegalArgumentException("File extension can only contain lowercase letters and numbers");
            if (schematicProvidersByExtension.containsKey(trimmedExtension)) {
                throw new IllegalArgumentException("File extension " + trimmedExtension + " is already registered to " + schematicProvidersByExtension.get(trimmedExtension).name());
            }
        }

        // Loop again to actually register the extensions
        for (String extension : schematicProvider.fileExtensions()) {
            String trimmedExtension = extension.trim();
            schematicProvidersByExtension.put(trimmedExtension, schematicProvider);
        }
        schematicProvidersByName.put(schematicProvider.name(), schematicProvider);
    }

    /**
     * Loads a schematic from the schematics directory
     * @param providerName Schematic provider to use
     * @param schematicName Name of the schematic
     * @return Clipboard object containing the schematic. Null if schematic does not exist or could not be loaded.
     */
    public @Nullable Schematic loadSchematic(String providerName, @NotNull String schematicName) {
        SchematicProvider<? extends Schematic> schematicProvider = schematicProvidersByName.get(providerName);
        if (schematicProvider == null) {
            throw new IllegalArgumentException("Schematic provider " + providerName + " is not registered");
        }
        return schematicProvider.load(schematicName);
    }

    /**
     * Saves a schematic to the schematics directory
     * @param schematicName Name of the schematic
     * @param clipboard Clipboard object containing the schematic
     */
    public void saveSchematic(
            @NotNull String schematicName,
            @NotNull String providerName,
            @NotNull Clipboard clipboard
    )  {
        SchematicProvider<S> schematicProvider = schematicProvidersByName.get(providerName);
        if (schematicProvider == null) {
            throw new IllegalArgumentException("Schematic provider " + providerName + " is not registered");
        }
        S schematic = schematicProvider.fromClipboard(schematicName, clipboard);

        schematicProvider.save(schematic);

    }

}
