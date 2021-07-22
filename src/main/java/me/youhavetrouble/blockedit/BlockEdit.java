package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.api.BlockEditWands;
import me.youhavetrouble.blockedit.commands.SetCommand;
import me.youhavetrouble.blockedit.commands.WandCommand;
import me.youhavetrouble.blockedit.wands.SelectionWand;
import org.bukkit.command.PluginCommand;
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

        //getCommand("test").setExecutor(new TestCommand());

        WandCommand wandCommand = new WandCommand();
        PluginCommand bukkitWandCommand = getCommand("/wand");
        if (bukkitWandCommand != null) {
            bukkitWandCommand.setExecutor(wandCommand);
            bukkitWandCommand.setTabCompleter(wandCommand);
        }
        SetCommand setCommand = new SetCommand();
        PluginCommand bukkitSetCommand = getCommand("/set");
        if (bukkitSetCommand != null) {
            bukkitSetCommand.setExecutor(setCommand);
            bukkitSetCommand.setTabCompleter(setCommand);
        }

    }


    public static BlockEdit getPlugin() {
        return plugin;
    }
}
