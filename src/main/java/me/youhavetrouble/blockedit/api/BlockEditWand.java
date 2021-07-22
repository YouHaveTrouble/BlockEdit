package me.youhavetrouble.blockedit.api;


import net.kyori.adventure.text.Component;

public interface BlockEditWand {

    /**
     * A unique id to identify the wand. Also used in //wand command.
     */
    String getId();

    /**
     * Name of the wand that will be used as wand item name.
     */
    Component getName();

    /**
     * Custom model data for the wand item. Set to 0 to not give the wand custom model data.
     */
    int getCustomModelData();

    /**
     * Permission for the wand usage
     */
    String getPermission();

}
