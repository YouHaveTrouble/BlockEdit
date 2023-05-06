package me.youhavetrouble.blockedit.commands.arguments;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Locale;


public class BlockDataArgument {

    public static BlockData getBlockData(String string) throws InvalidMaterialException, InvalidDataException {

        String[] split = string.split("\\[");
        Material material = null;
        BlockData blockData = null;
        if (split.length >= 1) {
            String materialString = split[0].toUpperCase(Locale.ENGLISH);
            material = Material.getMaterial(materialString);
            if (material == null) throw new InvalidMaterialException();
        }

        if (split.length == 1) {
            blockData = material.createBlockData();
        }
        if (split.length == 2) {
            try {
                blockData = material.createBlockData("[" + split[1]);
            } catch (IllegalArgumentException e) {
                throw new InvalidDataException();
            }
        }

        return blockData;

    }

}
