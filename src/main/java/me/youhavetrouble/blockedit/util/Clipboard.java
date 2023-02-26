package me.youhavetrouble.blockedit.util;

import org.bukkit.block.BlockState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Clipboard {

    private HashMap<RelativeLocation, BlockState> blocks = new HashMap<>();

    public Clipboard() {}

    public void setBlocks(HashMap<RelativeLocation, BlockState> newClipboard) {
        this.blocks = newClipboard;
    }

    public Map<RelativeLocation, BlockState> getBlocks() {
        return Collections.unmodifiableMap(this.blocks);
    }

    public void clear() {
        this.blocks.clear();
    }

    public boolean isEmpty() {
        return this.blocks.isEmpty();
    }
}
