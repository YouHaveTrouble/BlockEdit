package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeselCommand extends Command {
    public DeselCommand() {
        super("desel");
        setPermission("blockedit.command.desel");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        BEPlayer.getByPlayer(player).resetSelection();
        player.sendMessage(Component.text("You have reset your selection"));
        return true;
    }

}
