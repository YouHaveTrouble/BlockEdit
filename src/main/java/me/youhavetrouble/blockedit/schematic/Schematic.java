package me.youhavetrouble.blockedit.schematic;

import me.youhavetrouble.blockedit.util.Clipboard;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public abstract class Schematic {

    /**
     * Get the schematic as a Clipboard object
     * @param originLocation The location to use as the origin for the clipboard
     * @return The clipboard object containing the schematic
     */
    public abstract @NotNull Clipboard asClipboard(@NotNull Location originLocation);

}
