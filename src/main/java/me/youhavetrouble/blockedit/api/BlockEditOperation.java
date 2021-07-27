package me.youhavetrouble.blockedit.api;

import org.bukkit.block.Block;

public interface BlockEditOperation {

    /**
     * This function will run for every block in the selection it is executed on.
     * @param block Current block.
     */
    void transformBlock(Block block);

}
