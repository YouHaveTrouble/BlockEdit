package me.youhavetrouble.blockedit.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.block.BlockState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Clipboard {

    /**
     * Map of locations relative to the center of the clipboard and their block states
     */
    private final HashMap<Vector, BlockState> blocks = new HashMap<>();
    private Location baseLocation;
    private Vector baseLocationVector;

    public Clipboard(Location baseLocation) {
        this.baseLocation = baseLocation;
    }

    public void addBlock(Vector relativeLocation, BlockState blockState) {
        this.blocks.put(relativeLocation, blockState);
    }

    public Map<Vector, BlockState> getBlocks() {
        return Collections.unmodifiableMap(this.blocks);
    }

    public void setBaseLocation(Location baseLocation) {
        this.baseLocation = baseLocation.toBlockLocation();
        this.baseLocationVector = baseLocation.toVector();
    }

    public Location getBaseLocation() {
        return baseLocation;
    }

    public Vector getBaseLocationVector() {
        return baseLocationVector;
    }

    public void clear() {
        this.blocks.clear();
    }

    public boolean isEmpty() {
        return this.blocks.isEmpty();
    }

    /**
     * Rotates clipboard by specified degrees around the base location.
     * @param angle angle in degrees
     */
    public void rotate(double angle) {
        double radians = Math.toRadians(angle);
        for (Map.Entry<Vector, BlockState> entry : this.blocks.entrySet()) {
            Vector relativeLocation = entry.getKey();
            relativeLocation.rotateAroundY(radians);
            relativeLocation.setX(Math.round(relativeLocation.getX()));
            relativeLocation.setZ(Math.round(relativeLocation.getZ()));
        }
    }

}
