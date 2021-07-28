package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.BlockEdit;
import me.youhavetrouble.blockedit.optionals.SelectionHighlight;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeselCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        BEPlayer.getByPlayer(player).resetSelection();
        Bukkit.getScheduler().runTaskAsynchronously(BlockEdit.getPlugin(),() -> SelectionHighlight.sendStop(player));
        player.sendMessage(Component.text("You have reset your selection"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
