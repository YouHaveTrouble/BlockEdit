package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
import me.youhavetrouble.blockedit.operations.PasteOperation;
import me.youhavetrouble.blockedit.util.Selection;
import net.kyori.adventure.text.Component;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PasteCommand extends Command {

    public PasteCommand() {
        super("paste");
        setPermission("blockedit.command.paste");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You need to be a player to do this"));
            return true;
        }
        BEPlayer bePlayer = BEPlayer.getByPlayer(player);
        Vector playerLocationVector = player.getLocation().toBlockLocation().toVector();

        HashMap<Vector, BlockState> absoluteBlocks = new HashMap<>(bePlayer.getClipboard().getBlocks().size());

        bePlayer.getClipboard().getBlocks().forEach((vector, blockState) -> {
            Vector absolutePosition = vector.clone().add(playerLocationVector);
            absoluteBlocks.put(absolutePosition, blockState);
        });

        Selection selection = Selection.fromClipboard(absoluteBlocks.keySet(), player.getWorld());
        BlockEditAPI.runOperation(selection, 1, new PasteOperation(absoluteBlocks));

        return false;
    }
}
