import me.youhavetrouble.blockedit.WorkSplitter;
import me.youhavetrouble.blockedit.util.ChunkWork;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkSplitterTest {


    /**
     * Test if location -> chunk location works properly
     */
    @Test
    public void testLocationToChunk() {
        ChunkWork testOne = new ChunkWork(0,0, null);
        ChunkWork workSplitterTestOne = WorkSplitter.locationToChunkWork(15, 15, null);

        ChunkWork testTwo = WorkSplitter.locationToChunkWork(10.233D, -138.788D, null);
        ChunkWork workSplitterTestTwo = new ChunkWork(0,-9, null);

        assertEquals(workSplitterTestOne.getX(), testOne.getX());
        assertEquals(workSplitterTestOne.getZ(), testOne.getZ());

        assertEquals(workSplitterTestTwo.getX(), testTwo.getX());
        assertEquals(workSplitterTestTwo.getZ(), testTwo.getZ());
    }

}
