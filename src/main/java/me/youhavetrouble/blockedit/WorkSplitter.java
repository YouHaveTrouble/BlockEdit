package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.api.BlockEditOperation;
import me.youhavetrouble.blockedit.util.ChunkWork;
import me.youhavetrouble.blockedit.util.Selection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static void runOperation(HashSet<ChunkWork> chunkWorks, Selection selection, int chunksPerTick, BlockEditOperation operation) {
        if (selection == null) return;
        List<ChunkWork> chunkWork = new ArrayList<>(chunkWorks);
        AtomicInteger element = new AtomicInteger(chunkWork.size()-1);
        Bukkit.getScheduler().runTaskTimer(BlockEdit.getPlugin(), (task) -> {
            if (element.get() < 0) {
                task.cancel();
                return;
            }
            for (int i = 0; i< chunksPerTick; i++) {
                processChunkWork(chunkWork.get(element.getAndDecrement()), selection, operation);
            }
        }, 0, 1);
    }

    private static void processChunkWork(ChunkWork chunkWork, Selection selection, BlockEditOperation operation) {
        World world = selection.getWorld();
        if (world == null) return;
        chunkWork.getChunkAsync(world).thenAccept(chunk -> {
            // skip y levels that are not in the selection
            for (int y = (int) selection.getMinY(); y <= selection.getMaxY(); y++) {
                for (int x = 0; x <= 15; x++) {
                    for (int z = 0; z <= 15; z++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (!selection.contains(block.getLocation().toVector())) continue;
                        operation.transformBlock(block);
                    }
                }
            }
        });
    }

}
