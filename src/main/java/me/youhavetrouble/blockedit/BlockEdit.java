package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.wands.SelectionWand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockEdit extends JavaPlugin {

    private static BlockEdit plugin;
    private static SchematicHandler schematicHandler;
    private static WandsHandler wandsHandler;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        schematicHandler = new SchematicHandler(this);
        wandsHandler = new WandsHandler(this);

        SelectionWand selectionWand = new SelectionWand();
        wandsHandler.registerWand(selectionWand);
        getServer().getPluginManager().registerEvents(selectionWand, this);

        BlockEditCommands.registerCommands(this);
    }

    public static BlockEdit getPlugin() {
        return plugin;
    }

    public static SchematicHandler getSchematicHandler() {
        return schematicHandler;
    }

    public static WandsHandler getWandsHandler() {
        return wandsHandler;
    }

}
