package me.youhavetrouble.blockedit.operations;

import me.youhavetrouble.blockedit.api.BlockEditOperation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

import java.util.Map;

/**
 * Pastes blocks from a map of vectors and block states. Caution! Vectors must be floored to align with block locations.
 * @param blockStateMap
 */
@SuppressWarnings("UnstableApiUsage")
public record PasteOperation(Map<Vector, BlockState> blockStateMap) implements BlockEditOperation {

    @Override
    public void transformBlock(Block block) {
        if (!blockStateMap.containsKey(block.getLocation().toVector())) return;
        BlockState blockState = blockStateMap.get(block.getLocation().toVector());
        BlockState newState = blockState.copy(block.getLocation());
        newState.update(true, false);
    }
}
