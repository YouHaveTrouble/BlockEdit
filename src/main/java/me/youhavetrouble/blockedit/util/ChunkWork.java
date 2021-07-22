package me.youhavetrouble.blockedit.util;

import org.bukkit.World;
import org.bukkit.util.BoundingBox;

public class ChunkWork {

    private final int x, z, minHeight, maxHeight;

    public ChunkWork(double x, double z, World world) {
        this.x = (int) x;
        this.z = (int) z;
        if (world == null) {
            this.maxHeight = 256;
            this.minHeight = 0;
        } else {
            this.minHeight = world.getMinHeight();
            this.maxHeight = world.getMaxHeight();
        }
    }

    public BoundingBox getWorkspace(BoundingBox selection) {
        // TODO make it return shared space of getChunkBox and selection to cull some of the blocks from iterations
        return getChunkBox();
    }

    private BoundingBox getChunkBox() {
        return new BoundingBox(x*16, minHeight, z*16, (x+1)*16, maxHeight, (z+1)*16);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
