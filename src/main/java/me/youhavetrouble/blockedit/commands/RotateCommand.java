package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RotateCommand extends Command {

    public RotateCommand() {
        super("rotate");
        setPermission("blockedit.command.rotate");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You need to be a player to use this command"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Component.text("You need to provide an angle"));
            return true;
        }

        double angle;

        try {
            angle = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("Angle must be a number"));
            return true;
        }

        if (angle > 360 || angle < -360) {
            player.sendMessage(Component.text("Angle must be between -360 and 360"));
            return true;
        }

        BEPlayer bePlayer = BEPlayer.getByPlayer(player);

        bePlayer.getClipboard().rotate(angle);

        player.sendMessage(Component.text("Rotated clipboard by " + angle + " degrees"));

        return false;
    }
}
