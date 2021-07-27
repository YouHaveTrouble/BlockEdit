package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.api.BlockEditWands;
import me.youhavetrouble.blockedit.commands.*;
import me.youhavetrouble.blockedit.wands.SelectionWand;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockEdit extends JavaPlugin {

    private static BlockEdit plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        SelectionWand selectionWand = new SelectionWand();
        BlockEditWands.registerWand(selectionWand);
        getServer().getPluginManager().registerEvents(selectionWand, this);

        registerCommand("/wand", new WandCommand());
        registerCommand("/set", new SetCommand());
        registerCommand("/replace", new ReplaceCommand());
        registerCommand("/pos1", new Pos1Command());
        registerCommand("/pos2", new Pos2Command());
        registerCommand("/desel", new DeselCommand());

    }


    public static BlockEdit getPlugin() {
        return plugin;
    }

    private void registerCommand(String command, TabExecutor executor) {
        PluginCommand bukkitReplaceCommand = getCommand(command);
        if (bukkitReplaceCommand != null) {
            bukkitReplaceCommand.setExecutor(executor);
            bukkitReplaceCommand.setTabCompleter(executor);
        }
    }
}
