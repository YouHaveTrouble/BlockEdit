package me.youhavetrouble.blockedit.util;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.concurrent.CompletableFuture;

public class ChunkWork {

    private int x, z;

    public ChunkWork(double x, double z) {
        setCoords(x,z);
    }

    public BoundingBox getWorkspace(BoundingBox selection, World world) {
        // TODO make it return shared space of getChunkBox and selection to cull some of the blocks from iterations
        return getChunkBox(world);
    }

    public CompletableFuture<Chunk> getChunkAsync(World world) {
        return world.getChunkAtAsync(x,z, true);
    }

    private BoundingBox getChunkBox(World world) {
        return new BoundingBox(x*16, world.getMinHeight(), z*16, (x+1)*16, world.getMaxHeight(), (z+1)*16);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setCoords(double x, double z) {
        this.x = (int) x >> 4;
        this.z = (int) z >> 4;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ChunkWork chunkWork)) return false;
        return this.x == chunkWork.getX() && this.z == chunkWork.getZ();
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = res * 31 + Math.min(x, z);
        res = res * 31 + Math.max(x, z);
        return res;
    }

    public ChunkWork clone() {
        return new ChunkWork(x*16,z*16);
    }


}
