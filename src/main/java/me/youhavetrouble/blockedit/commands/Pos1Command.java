package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Pos1Command extends Command {
    public Pos1Command() {
        super("pos1");
        setPermission("blockedit.command.pos");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        BEPlayer bePlayer = BEPlayer.getByPlayer(player);
        bePlayer.setSelectionPoint1(player.getLocation().toBlockLocation());
        player.sendMessage(Component.text("First point set at your location"));
        return true;
    }

}
