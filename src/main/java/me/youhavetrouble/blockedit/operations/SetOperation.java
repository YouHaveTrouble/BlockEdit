package me.youhavetrouble.blockedit.operations;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.BlockEdit;
import me.youhavetrouble.blockedit.util.ChunkWork;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SetOperation {

    private final BoundingBox selection;
    private final BlockData blockToSet;
    private final List<ChunkWork> chunkwork = new ArrayList<>();

    public SetOperation(HashSet<ChunkWork> chunkWorks, BEPlayer bePlayer, BlockData blockToSet, int chunksPerTick) {
        this.selection = bePlayer.getSelection();
        this.blockToSet = blockToSet;
        this.chunkwork.addAll(chunkWorks);
        AtomicInteger element = new AtomicInteger(chunkwork.size()-1);

        Bukkit.getScheduler().runTaskTimer(BlockEdit.getPlugin(), (task) -> {
            if (element.get() < 0) {
                task.cancel();
                return;
            }
            for (int i = 0; i<= chunksPerTick; i++) {
                processChunkWork(chunkwork.get(element.getAndDecrement()), bePlayer.getSelectionWorld());
            }
        }, 0, 1);
    }

    private void processChunkWork(ChunkWork chunkWork, UUID worldUuid) {
        World world = Bukkit.getWorld(worldUuid);
        if (world == null) return;
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
