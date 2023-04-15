package me.youhavetrouble.blockedit.operations;

import me.youhavetrouble.blockedit.api.BlockEditOperation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

import java.util.Map;

public record PasteOperation(Map<Vector, BlockState> blockStateMap) implements BlockEditOperation {

    @Override
    public void transformBlock(Block block) {
        if (!blockStateMap.containsKey(block.getLocation().toVector())) return;
        block.setBlockData(blockStateMap.get(block.getLocation().toVector()).getBlockData());
    }
}
