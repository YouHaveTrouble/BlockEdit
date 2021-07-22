package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.api.BlockEditWands;
import me.youhavetrouble.blockedit.commands.WandCommand;
import me.youhavetrouble.blockedit.wands.SelectionWand;
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

        getCommand("test").setExecutor(new TestCommand());

        WandCommand wandCommand = new WandCommand();
        getCommand("/wand").setExecutor(wandCommand);
        getCommand("/wand").setTabCompleter(wandCommand);

    }


    public static BlockEdit getPlugin() {
        return plugin;
    }
}
