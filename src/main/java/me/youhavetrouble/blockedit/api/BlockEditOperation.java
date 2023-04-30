package me.youhavetrouble.blockedit.api;

import me.youhavetrouble.blockedit.util.Selection;
import org.bukkit.block.Block;

/**
 * This interface is used to define a BlockEdit operation.<br>
 * Implement this interface and pass it to {@link BlockEditAPI#runOperation(Selection, int, BlockEditOperation)} to run it.
 */
public interface BlockEditOperation {

    /**
     * This function will run for every block in the selection it is executed on.
     * @param block Current block.
     */
    void transformBlock(Block block);

}
