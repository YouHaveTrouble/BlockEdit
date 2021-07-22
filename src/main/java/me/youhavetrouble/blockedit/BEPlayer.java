package me.youhavetrouble.blockedit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.UUID;

public class BEPlayer {

    private static final HashMap<UUID, BEPlayer> playerHashMap = new HashMap<>();
    private BoundingBox selection;
    private Location selectionPoint1, selectionPoint2;
    private UUID selectionWorldUuid;

    public BoundingBox getSelection() {
        return selection;
    }

    private void updateSelection() {
        if (selectionPoint1 == null || selectionPoint2 == null) {
            selection = null;
            return;
        }
        if (selectionPoint1.getWorld() == null || selectionPoint2 == null) {
            selection = null;
            return;
        }
        if (!selectionPoint1.getWorld().equals(selectionPoint2.getWorld())) {
            selection = null;
            return;
        }
        selectionWorldUuid = selectionPoint1.getWorld().getUID();
        selection = BoundingBox.of(selectionPoint1, selectionPoint2);
        // bounding boxes are dumb.
        selection.expand(0.5, 0.5, 0.5);
        selection.shift(0.5,0.5,0.5);
    }

    public void setSelectionPoint1(Location selectionPoint1) {
        if (this.selectionPoint1 != null && this.selectionPoint1.equals(selectionPoint1)) return;
        this.selectionPoint1 = selectionPoint1;
        updateSelection();
    }

    public void setSelectionPoint2(Location selectionPoint2) {
        if (this.selectionPoint2 != null && this.selectionPoint2.equals(selectionPoint2)) return;
        this.selectionPoint2 = selectionPoint2;
        updateSelection();
    }

    /**
     * @return World withinn which the selection is made.
     */
    public UUID getSelectionWorld() {
        if (selection == null) return null;
        return selectionPoint1.getWorld().getUID();
    }

    /**
     * @return Clone of selectionPoint1
     */
    public Location getSelectionPoint1() {
        return selectionPoint1.clone();
    }

    /**
     * @return Clone of selectionPoint2
     */
    public Location getSelectionPoint2() {
        return selectionPoint2.clone();
    }

    protected static void addPlayer(Player player) {
        playerHashMap.put(player.getUniqueId(), new BEPlayer());
    }

    protected static void removePlayer(Player player) {
        playerHashMap.remove(player.getUniqueId());
    }

    public static BEPlayer getByPlayer(Player player) {
        return getByUuid(player.getUniqueId());
    }

    protected static BEPlayer getByUuid(UUID uuid) {
        return playerHashMap.get(uuid);
    }
}
