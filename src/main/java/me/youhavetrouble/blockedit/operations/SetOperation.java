package me.youhavetrouble.blockedit.operations;


import me.youhavetrouble.blockedit.api.BlockEditOperation;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;


public record SetOperation(BlockData blockData) implements BlockEditOperation {

    @Override
    public void transformBlock(Block block) {
        block.setBlockData(blockData);
    }
}
