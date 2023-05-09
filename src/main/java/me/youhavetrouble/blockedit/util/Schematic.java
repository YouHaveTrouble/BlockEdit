package me.youhavetrouble.blockedit.util;

import me.youhavetrouble.blockedit.commands.arguments.BlockDataArgument;
import me.youhavetrouble.blockedit.exception.SchematicLoadException;
import net.querz.nbt.io.NBTInputStream;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import org.bukkit.block.data.BlockData;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class Schematic {

    private final short width, height, length;
    private final int bitsPerBlock;
    private final byte[] blocks;

    private BlockData[] blockPalette = new BlockData[0];

    public Schematic(File file) throws SchematicLoadException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))))) {
            NamedTag tag = new NBTInputStream(dis).readTag(Tag.DEFAULT_MAX_DEPTH);

            if (tag == null) throw new SchematicLoadException("Could not load schematic. Invalid file format");
            if (!(tag.getTag() instanceof CompoundTag compoundTag)) throw new SchematicLoadException("Could not load schematic. Invalid file format");

            this.width = compoundTag.getShort("Width");
            this.height = compoundTag.getShort("Height");
            this.length = compoundTag.getShort("Length");

            CompoundTag paletteMap = compoundTag.getCompoundTag("Palette");
            this.blocks = compoundTag.getByteArray("BlockData");
            this.bitsPerBlock = blocks.length >> 6;

            if (paletteMap.size() > 0) {
                this.blockPalette = new BlockData[paletteMap.size()];
            }

            for (String key : paletteMap.keySet()) {
                int index = paletteMap.getInt(key);
                String[] split = key.split(":");
                if (split.length == 2) {
                    key = split[1];
                }
                this.blockPalette[index] = BlockDataArgument.getBlockData(key);
            }

        } catch (IOException e) {
            throw new SchematicLoadException("Could not load schematic due to I/O error");
        }
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    public short getLength() {
        return length;
    }

    public List<BlockData> getBlockPalette() {
        return List.of(blockPalette);
    }
}
