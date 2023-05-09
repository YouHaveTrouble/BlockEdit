package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.util.Clipboard;
import me.youhavetrouble.blockedit.util.Schematic;

import java.io.*;

public class SchematicHandler {

    private final BlockEdit plugin;

    protected SchematicHandler(BlockEdit plugin) {
        this.plugin = plugin;
        createSchematicsDirectory();
    }

    private void createSchematicsDirectory() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
            }
            File schematicsDir = new File(plugin.getDataFolder(), "schematics");
            if (!schematicsDir.exists()) {
                schematicsDir.mkdir();
            }
        } catch (SecurityException e) {
            plugin.getLogger().warning("Could not create schematics directory. Make sure server has read/write access to the plugin folder.");
        }

    }

    /**
     * Loads a schematic from the schematics directory
     * @param schematicName Name of the schematic
     * @return Clipboard object containing the schematic. Null if schematic does not exist
     */
    public Clipboard loadSchematic(String schematicName) {
        File schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + ".schematic");
        if (!schematicFile.exists()) {
            schematicFile = new File(plugin.getDataFolder(), "schematics/" + schematicName + ".schem");
        }
        if (!schematicFile.exists()) {
            return null;
        }
        Schematic schematic = new Schematic(schematicFile);


        return null;
    }

    /**
     * Saves a schematic to the schematics directory
     * @param schematicName Name of the schematic
     * @param clipboard Clipboard object containing the schematic
     */
    public void saveSchematic(String schematicName, Clipboard clipboard) {

    }



}
