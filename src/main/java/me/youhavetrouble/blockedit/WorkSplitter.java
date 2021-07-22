package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.util.ChunkWork;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;

public class WorkSplitter {

    public static HashSet<ChunkWork> getOperatedOnChunks(BoundingBox boundingBox) {
        HashSet<ChunkWork> chunks = new HashSet<>();
        ChunkWork chunkWork = new ChunkWork(0,0);
        // TODO Find a way to get chunks in the selection more efficiently
        for (double x = boundingBox.getMinX(); x <= boundingBox.getMaxX(); x++) {
            for (double z = boundingBox.getMinZ(); z <= boundingBox.getMaxZ(); z++) {
                chunkWork.setCoords(x,z);
                if (chunks.contains(chunkWork)) continue;
                chunks.add(chunkWork.clone());
            }
        }
        return chunks;
    }

    public static ChunkWork locationToChunkWork(double x, double z) {
        int chunkX = (int) x >> 4;
        int chunkZ = (int) z >> 4;
        return new ChunkWork(chunkX, chunkZ);
    }

}
