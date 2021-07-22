package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.util.ChunkWork;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;
import java.util.Set;

public class WorkSplitter {

    public static Set<ChunkWork> getOperatedOnChunks(BoundingBox boundingBox, World world) {
        HashSet<ChunkWork> chunks = new HashSet<>();
        for (double x = boundingBox.getMinX(); x<= boundingBox.getMaxX(); x+=16) {
            for (double z = boundingBox.getMinZ(); z <= boundingBox.getMaxZ(); z+=16) {
                ChunkWork chunkWork = locationToChunkWork(x,z, world);
                if (chunks.contains(chunkWork)) continue;
                chunks.add(chunkWork);
            }
        }
        return chunks;
    }

    public static ChunkWork locationToChunkWork(double x, double z, World world) {
        int chunkX = (int) x >> 4;
        int chunkZ = (int) z >> 4;
        return new ChunkWork(chunkX, chunkZ, world);
    }

}
