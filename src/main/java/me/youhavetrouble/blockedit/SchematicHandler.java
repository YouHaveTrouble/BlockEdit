package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.util.Clipboard;
import net.querz.nbt.io.NBTInputStream;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import org.bukkit.block.BlockState;

import java.io.*;
import java.util.zip.GZIPInputStream;

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
