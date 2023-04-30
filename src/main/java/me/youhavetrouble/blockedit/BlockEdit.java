package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.commands.*;
import me.youhavetrouble.blockedit.wands.SelectionWand;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockEdit extends JavaPlugin {

    private static BlockEdit plugin;

    private static SchematicHandler schematicHandler;
    private static WandsHandler wandsHandler;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        registerCommand(new WandCommand());
        registerCommand(new SetCommand());
        registerCommand(new ReplaceCommand());
        registerCommand(new DeselCommand());
        registerCommand(new Pos1Command());
        registerCommand(new Pos2Command());
        registerCommand(new CopyCommand());
        registerCommand(new PasteCommand());
        registerCommand(new RotateCommand());

        schematicHandler = new SchematicHandler(this);
        wandsHandler = new WandsHandler(this);

        SelectionWand selectionWand = new SelectionWand();
        wandsHandler.registerWand(selectionWand);
        getServer().getPluginManager().registerEvents(selectionWand, this);


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

    private void registerCommand(Command command) {
        getServer().getCommandMap().register("blockedit", command);
    }

}
