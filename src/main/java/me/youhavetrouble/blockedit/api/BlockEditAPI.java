package me.youhavetrouble.blockedit.api;

import me.youhavetrouble.blockedit.BlockEdit;
import me.youhavetrouble.blockedit.SchematicHandler;
import me.youhavetrouble.blockedit.WandsHandler;
import me.youhavetrouble.blockedit.util.ChunkWork;
import me.youhavetrouble.blockedit.util.Selection;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class BlockEditAPI {

    /**
     * Runs an operation on the given selection with the given amount of chunks per tick
     * @param selection The area that will be operated on
     * @param chunksPerTick Amount of chunks per tick to modify
     * @param operation Operation to execute
     */
    public static void runOperation(
            @NotNull Selection selection,
            int chunksPerTick,
            @NotNull BlockEditOperation operation
    ) {
        HashSet<ChunkWork> work = WorkSplitter.getOperatedOnChunks(selection);
        WorkSplitter.runOperation(work, selection, chunksPerTick, operation);
    }

    /**
     * Gets the wands handler object that can be used to work with wands
     * @return Wands handler
     */
    public static WandsHandler getWandsHandler() {
        return BlockEdit.getWandsHandler();
    }

    /**
     * Gets schematic handler object that can be used to work with schematics
     * @return Schematic handler
     */
    public static SchematicHandler getSchematicHandler() {
        return BlockEdit.getSchematicHandler();
    }
}
