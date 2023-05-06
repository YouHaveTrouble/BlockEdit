package me.youhavetrouble.blockedit.operations;

import me.youhavetrouble.blockedit.api.BlockEditOperation;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public record ReplaceOperation(BlockData blockToReplace, BlockData blockToSet) implements BlockEditOperation {

    @Override
    public void transformBlock(Block block) {
        if (!block.getBlockData().matches(blockToReplace)) return;
        block.setBlockData(blockToSet);
    }
}
