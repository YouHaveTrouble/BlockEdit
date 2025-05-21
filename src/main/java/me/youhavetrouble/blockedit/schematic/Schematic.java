package me.youhavetrouble.blockedit.schematic;

import me.youhavetrouble.blockedit.util.Clipboard;
import org.jetbrains.annotations.NotNull;

public abstract class Schematic {

    /**
     * Get the schematic as a Clipboard object
     * @return The clipboard object containing the schematic
     */
    public abstract @NotNull Clipboard asClipboard();

}
