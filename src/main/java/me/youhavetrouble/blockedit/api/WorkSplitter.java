package me.youhavetrouble.blockedit.api;

import me.youhavetrouble.blockedit.util.ChunkWork;
import me.youhavetrouble.blockedit.util.Selection;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class WorkSplitter {

    protected static Set<ChunkWork> getOperatedOnChunks(BoundingBox boundingBox) {
        HashSet<ChunkWork> chunks = new HashSet<>();
        int minChunkX = (int) Math.floor(boundingBox.getMinX()) >> 4;
        int maxChunkX = (int) Math.floor(boundingBox.getMaxX()) >> 4;
        int minChunkZ = (int) Math.floor(boundingBox.getMinZ()) >> 4;
        int maxChunkZ = (int) Math.floor(boundingBox.getMaxZ()) >> 4;

        ChunkWork chunkWork = new ChunkWork(0,0);
        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                chunkWork.setCoords(x << 4, z << 4);
                chunks.add(chunkWork.clone());
            }
        }
        return chunks;
    }

    protected static OperationWork runOperation(@NotNull Set<ChunkWork> chunkWorks, @NotNull Selection selection, int chunksPerTick, @NotNull BlockEditOperation operation) {
        OperationWork operationWork = new OperationWork(chunkWorks, selection, chunksPerTick, operation);
        operationWork.start();
        return operationWork;
    }

}
