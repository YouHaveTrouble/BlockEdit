package me.youhavetrouble.blockedit.api;

import me.youhavetrouble.blockedit.WorkSplitter;
import me.youhavetrouble.blockedit.util.ChunkWork;
import me.youhavetrouble.blockedit.util.Selection;

import java.util.HashSet;

public class BlockEditAPI {

    /**
     * @param selection The area that will be operated on
     * @param chunksPerTick Amount of chunks per tick to modify
     * @param operation Operation to execute
     */
    public static void runOperation(Selection selection, int chunksPerTick, BlockEditOperation operation) {
        HashSet<ChunkWork> work = WorkSplitter.getOperatedOnChunks(selection);
        WorkSplitter.runOperation(work, selection, chunksPerTick, operation);
    }

}
