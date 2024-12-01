package me.youhavetrouble.blockedit.operations;

import me.youhavetrouble.blockedit.api.BlockEditOperation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

@SuppressWarnings("UnstableApiUsage")
public record ReplaceOperation(BlockData blockToReplace, BlockState blockToSet) implements BlockEditOperation {

    @Override
    public void transformBlock(Block block) {
        if (!block.getBlockData().matches(blockToReplace)) return;
        BlockState newState = blockToSet.copy(block.getLocation());
        newState.update(true, false);
    }
}
