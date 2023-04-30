package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.api.BlockEditAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WandCommand extends Command {

    public WandCommand() {
        super("wand");
        setPermission("blockedit.command.wand");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return true;
        ItemStack wand;
        if (args.length == 0) {
            wand = BlockEditAPI.getWandsHandler().getWand("select");
            if (wand == null) return true;
        } else {
            wand = BlockEditAPI.getWandsHandler().getWand(args[0]);
            if (wand == null) {
                player.sendMessage(Component.text("Could not find wand with id "+args[0]));
                return true;
            }
        }
        player.getInventory().addItem(wand);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return StringUtil.copyPartialMatches(args[0], BlockEditAPI.getWandsHandler().getWandIds(), new ArrayList<>());
        return new ArrayList<>();
    }

}
