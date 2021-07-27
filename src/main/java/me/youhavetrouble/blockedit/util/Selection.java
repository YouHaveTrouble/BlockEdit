package me.youhavetrouble.blockedit.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.UUID;

public class Selection extends BoundingBox {

    private UUID worldUuid;

    public Selection(Location location1, Location location2, UUID worldUuid) {
        super(location1.getX(), location1.getY(), location1.getZ(), location2.getX(), location2.getY(), location2.getZ());
        this.worldUuid = worldUuid;
    }

    public Selection(BoundingBox boundingBox, UUID worldUuid) {
        super(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
        this.worldUuid = worldUuid;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldUuid);
    }

    public UUID getWorldUuid() {
        return worldUuid;
    }

    public void setWorldUuid(UUID worldUuid) {
        this.worldUuid = worldUuid;
    }
}
