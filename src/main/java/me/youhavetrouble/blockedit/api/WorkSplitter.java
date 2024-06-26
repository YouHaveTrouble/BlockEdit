package me.youhavetrouble.blockedit.api;

import me.youhavetrouble.blockedit.BlockEdit;
import me.youhavetrouble.blockedit.util.ChunkWork;
import me.youhavetrouble.blockedit.util.Selection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkSplitter {

    protected static HashSet<ChunkWork> getOperatedOnChunks(BoundingBox boundingBox) {
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

    protected static void runOperation(HashSet<ChunkWork> chunkWorks, Selection selection, int chunksPerTick, BlockEditOperation operation) {
        if (selection == null) return;
        Selection sel = new Selection(selection.clone().expand(0.1), selection.getWorldUuid());
        List<ChunkWork> chunkWorkList = new ArrayList<>(chunkWorks);
        AtomicInteger element = new AtomicInteger(chunkWorkList.size()-1);
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(BlockEdit.getPlugin(), (task -> {
            if (element.get() < 0) {
                task.cancel();
                return;
            }
            for (int i = 0; i< chunksPerTick; i++) {
                int chunkWorkIndex = element.getAndDecrement();
                ChunkWork chunkWork = chunkWorkList.get(chunkWorkIndex);
                Bukkit.getRegionScheduler().execute(BlockEdit.getPlugin(), selection.getWorld(), chunkWork.getX(), chunkWork.getZ(), () -> {
                    processChunkWork(chunkWork, sel, operation);
                });
            }
        }), 1, 1);

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
