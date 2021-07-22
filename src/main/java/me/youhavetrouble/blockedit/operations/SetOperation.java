package me.youhavetrouble.blockedit.operations;

import me.youhavetrouble.blockedit.BlockEdit;
import me.youhavetrouble.blockedit.util.ChunkWork;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;

public class SetOperation {

    private final BoundingBox selection;
    private final BlockData blockToSet;

    public SetOperation(HashSet<ChunkWork> chunkWorks, World world, BoundingBox selection, BlockData blockToSet) {
        this.selection = selection;
        this.blockToSet = blockToSet;
        int stagger = 0;
        for (ChunkWork chunkWork : chunkWorks) {
            Bukkit.getScheduler().runTaskLater(BlockEdit.getPlugin(), () -> processChunkWork(chunkWork, world), stagger++);
        }
    }

    private void processChunkWork(ChunkWork chunkWork, World world) {
        chunkWork.getChunkAsync(world).thenAccept(chunk -> {
            // skip y levels that are not in the selection
            for (int y = (int) selection.getMinY(); y <= selection.getMaxY(); y++) {
                for (int x = 0; x <= 15; x++) {
                    for (int z = 0; z <= 15; z++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (!selection.contains(block.getLocation().toVector())) continue;
                        block.setBlockData(blockToSet);
                    }
                }
            }
        });
    }

}
