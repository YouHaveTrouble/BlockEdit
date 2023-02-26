package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Pos2Command extends Command {

    public Pos2Command() {
        super("pos2");
        setPermission("blockedit.command.pos");
    }
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        BEPlayer bePlayer = BEPlayer.getByPlayer(player);
        bePlayer.setSelectionPoint2(player.getLocation().toBlockLocation());
        player.sendMessage(Component.text("Second point set at your location"));
        return true;
    }
}
