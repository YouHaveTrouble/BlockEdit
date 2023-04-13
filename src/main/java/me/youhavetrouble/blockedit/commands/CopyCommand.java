package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CopyCommand extends Command {

    public CopyCommand() {
        super("copy");
        setPermission("blockedit.command.copy");
    }
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        BEPlayer bePlayer = BEPlayer.getByPlayer(player);
        try {
            bePlayer.setClipboardFromSelection();
            player.sendMessage(Component.text("Copied selection to clipboard"));
        } catch (IllegalStateException e) {
            player.sendMessage(Component.text("You need to select an area first"));
        }

        return true;
    }
}
