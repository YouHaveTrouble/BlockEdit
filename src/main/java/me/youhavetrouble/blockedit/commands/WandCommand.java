package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.api.BlockEditWands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WandCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        ItemStack wand;
        if (args.length == 0) {
            wand = BlockEditWands.getWand("select");
            if (wand == null) return true;
        } else {
            wand = BlockEditWands.getWand(args[0]);
            if (wand == null) {
                player.sendMessage(Component.text("Could not find wand with id "+args[0]));
                return true;
            }
        }
        player.getInventory().addItem(wand);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return StringUtil.copyPartialMatches(args[0], BlockEditWands.getWandIds(), new ArrayList<>());
        return null;
    }
}
