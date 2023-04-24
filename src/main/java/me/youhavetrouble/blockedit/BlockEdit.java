package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.api.BlockEditWands;
import me.youhavetrouble.blockedit.commands.*;
import me.youhavetrouble.blockedit.wands.SelectionWand;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockEdit extends JavaPlugin {

    private static BlockEdit plugin;

    private static SchematicHandler schematicHandler;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        SelectionWand selectionWand = new SelectionWand();
        BlockEditWands.registerWand(selectionWand);
        getServer().getPluginManager().registerEvents(selectionWand, this);

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


    }


    public static BlockEdit getPlugin() {
        return plugin;
    }

    public static SchematicHandler getSchematicHandler() {
        return schematicHandler;
    }

    private void registerCommand(Command command) {
        getServer().getCommandMap().register("blockedit", command);
    }

}
