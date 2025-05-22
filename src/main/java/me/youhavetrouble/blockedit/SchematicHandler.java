package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.exception.NoProviderForSchematicFileExtensionException;
import me.youhavetrouble.blockedit.exception.SchematicHandlerRegistrationException;
import me.youhavetrouble.blockedit.exception.SchematicLoadException;
import me.youhavetrouble.blockedit.exception.SchematicSaveException;
import me.youhavetrouble.blockedit.schematic.FileSchematicProvider;
import me.youhavetrouble.blockedit.schematic.Schematic;
import me.youhavetrouble.blockedit.schematic.SchematicProvider;
import me.youhavetrouble.blockedit.util.Clipboard;
import me.youhavetrouble.blockedit.util.NameFilenameFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SchematicHandler<S extends Schematic> {

    private final BlockEdit plugin;

    private final Map<String, SchematicProvider<S>> schematicProvidersByExtension = new HashMap<>();
    private final Map<String, SchematicProvider<S>> schematicProvidersByName = new HashMap<>();

    public final File schematicsDirectory;

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

    public Collection<String> getSchematicProvidersList() {
        return schematicProvidersByName.keySet();
    }

    public SchematicProvider<? extends Schematic> getSchematicProviderByName(String name) {
        return schematicProvidersByName.get(name);
    }

    public void registerSchematicProvider(
            @NotNull SchematicProvider<S> schematicProvider
    ) throws SchematicHandlerRegistrationException {

        String schematicProviderName = schematicProvider.name().trim();

        if (!schematicProviderName.matches("^[a-z0-9]+$")) {
            throw new SchematicHandlerRegistrationException("Schematic provider name can only contain lowercase letters and numbers", schematicProvider);
        }

        if (schematicProviderName.equals("file")) {
            throw new SchematicHandlerRegistrationException("Schematic provider name cannot be 'file'", schematicProvider);
        }

        if (schematicProvidersByName.containsKey(schematicProviderName)) {
            throw new SchematicHandlerRegistrationException("Schematic provider with name " + schematicProvider.name() + " is already registered", schematicProvider);
        }

        // Register the provider as a file provider that associates the provider with file extensions
        if (schematicProvider instanceof FileSchematicProvider<S> fileSchematicProvider) {
            // Loop extensions to verify if they're valid and not already registered
            for (String extension : fileSchematicProvider.fileExtensions()) {
                String trimmedExtension = extension.trim();
                if (trimmedExtension.isEmpty()) throw new SchematicHandlerRegistrationException("File extension cannot be empty", fileSchematicProvider);
                if (!trimmedExtension.matches("^[a-z0-9]+$")) throw new SchematicHandlerRegistrationException("File extension can only contain lowercase letters and numbers", fileSchematicProvider);
                if (schematicProvidersByExtension.containsKey(trimmedExtension)) {
                    throw new SchematicHandlerRegistrationException("File extension " + trimmedExtension + " is already registered to " + schematicProvidersByExtension.get(trimmedExtension).name(), fileSchematicProvider);
                }
            }
            // Loop again to actually register the extensions
            for (String extension : fileSchematicProvider.fileExtensions()) {
                String trimmedExtension = extension.trim();
                schematicProvidersByExtension.put(trimmedExtension, schematicProvider);
            }
            return;
        }

        schematicProvidersByName.put(schematicProvider.name(), schematicProvider);
    }

    /**
     * Loads a schematic from the schematic provider
     * @param providerName Schematic provider to use
     * @param schematicName Name of the schematic
     * @return Clipboard object containing the schematic. Null if schematic does not exist or could not be loaded.
     */
    public @Nullable Schematic loadSchematic(@NotNull String providerName, @NotNull String schematicName) {

        // file provider is a special case here
        if (providerName.equals("file")) {
            return loadSchematic(schematicName);
        }

        SchematicProvider<? extends Schematic> schematicProvider = schematicProvidersByName.get(providerName);
        if (schematicProvider == null) {
            throw new IllegalArgumentException("Schematic provider " + providerName + " is not registered");
        }
        return schematicProvider.load(schematicName);
    }

    /**
     * Loads a schematic from the schematic directory. File schematic provider will be matched by the file extenstion
     * @param schematicName File name without extension
     * @return Schematic object
     */
    public @Nullable Schematic loadSchematic(@NotNull String schematicName) throws SchematicLoadException {
        File[] files;
        try {
            files = this.schematicsDirectory.listFiles(new NameFilenameFilter(schematicName));
            if (files == null || files.length == 0) return null;
        } catch (SecurityException e) {
            throw new SchematicLoadException("Could not list schematics directory", schematicName, e);
        }

        File schematicFile = files[0];
        String fileName = schematicFile.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        SchematicProvider<S> schematicProvider = schematicProvidersByExtension.get(fileExtension);
        if (schematicProvider == null) {
            throw new NoProviderForSchematicFileExtensionException("No schematic provider found for file extension " + fileExtension, schematicName, fileExtension);
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
    ) throws SchematicSaveException {
        SchematicProvider<S> schematicProvider = schematicProvidersByName.get(providerName);
        if (schematicProvider == null) {
            throw new SchematicSaveException("Schematic provider " + providerName + " is not registered", schematicName);
        }
        S schematic = schematicProvider.fromClipboard(schematicName, clipboard);
        schematicProvider.save(schematic);
    }

}
