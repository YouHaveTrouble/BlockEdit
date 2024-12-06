package me.youhavetrouble.blockedit.api;

import me.youhavetrouble.blockedit.BlockEdit;
import me.youhavetrouble.blockedit.util.ChunkWork;
import me.youhavetrouble.blockedit.util.Selection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class OperationWork {

    private boolean started, finished = false;
    private final List<ChunkWork> chunkWorkList = new ArrayList<>();
    private final Selection selection;
    private final int chunksPerTick;
    private final BlockEditOperation operation;
    private final AtomicInteger chunksLeft;
    private final AtomicInteger chunksProcessed = new AtomicInteger(0);

    protected OperationWork(
            Set<ChunkWork> chunkWorks,
            Selection selection,
            int chunksPerTick,
            BlockEditOperation operation
    ) {
        this.chunkWorkList.addAll(chunkWorks);
        this.chunksLeft = new AtomicInteger(this.chunkWorkList.size() - 1);
        this.selection = new Selection(selection.clone().expand(0.1), selection.getWorldUuid());
        this.chunksPerTick = chunksPerTick;
        this.operation = operation;
    }

    /**
     * Starts the operation. This will start processing chunks in the background.
     * This method can only be called once.
     *
     * @throws IllegalStateException if called when the operation has already been started
     */
    protected void start() {
        if (started) throw new IllegalStateException("Operation already started");
        started = true;
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(BlockEdit.getPlugin(), (task -> {
            if (chunksLeft.get() < 0) {
                this.finished = true;
                task.cancel();
                return;
            }
            for (int i = 0; i < chunksPerTick; i++) {
                int chunkWorkIndex = chunksLeft.getAndDecrement();
                if (chunkWorkIndex < 0) {
                    this.finished = true;
                    task.cancel();
                    return;
                }
                ChunkWork chunkWork = chunkWorkList.get(chunkWorkIndex);
                World world = selection.getWorld();
                if (world == null) return;
                Bukkit.getRegionScheduler().execute(BlockEdit.getPlugin(), selection.getWorld(), chunkWork.getX(), chunkWork.getZ(), () -> {
                    processChunkWork(chunkWork, selection, operation);
                });
            }
        }), 1, 1);
    }

    /**
     * Returns the total amount of chunks that will be processed.
     *
     * @return Total amount of chunks
     */
    public int getTotalChunks() {
        return chunkWorkList.size();
    }

    /**
     * Returns the amount of chunks processed. This might not match total amount of chunks after operation finishes.
     *
     * @return Amount of chunks processed
     * @see #finished to check if the operation has finished
     */
    public int getChunksProcessed() {
        return chunksProcessed.get();
    }

    /**
     * Returns the amount of chunks left to process.
     *
     * @return Amount of chunks left to process
     */
    public int getChunksLeft() {
        return chunksLeft.get();
    }

    /**
     * Returns whether the operation has finished. This will return true after all work tasks have been delegated.
     *
     * @return Whether the operation has finished
     * @see #getChunksLeft() to get the amount of chunks left to process
     * @see #getChunksProcessed() to get the amount of chunks processed
     */
    public boolean isFinished() {
        return finished;
    }

    private void processChunkWork(ChunkWork chunkWork, Selection selection, BlockEditOperation operation) {
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
        }).thenRunAsync(this.chunksProcessed::incrementAndGet);
    }

}
