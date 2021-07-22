package me.youhavetrouble.blockedit.util;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.concurrent.CompletableFuture;

public class ChunkWork {

    private int x, z;

    public ChunkWork(double x, double z) {
        setCoords(x,z);
    }

    public CompletableFuture<Chunk> getChunkAsync(World world) {
        return world.getChunkAtAsync(x,z, true);
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
