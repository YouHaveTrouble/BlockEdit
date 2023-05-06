package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
import me.youhavetrouble.blockedit.commands.arguments.BlockDataArgument;
import me.youhavetrouble.blockedit.commands.arguments.InvalidDataException;
import me.youhavetrouble.blockedit.commands.arguments.InvalidMaterialException;
import me.youhavetrouble.blockedit.operations.ReplaceOperation;
import me.youhavetrouble.blockedit.util.Selection;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReplaceCommand extends Command {

    public ReplaceCommand() {
        super("replace");
        setPermission("blockedit.command.replace");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 || args.length == 2) {
            ArrayList<String> suggestions = new ArrayList<>();
            for (Material material : Material.values()) {
                if (material.isBlock()) suggestions.add(material.name().toLowerCase());
            }
            return StringUtil.copyPartialMatches(args[args.length-1], suggestions, new ArrayList<>());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return true;
        if (args.length == 0) {
            player.sendMessage(Component.text("You need to provide block type"));
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(Component.text("You need to provide block type to replace"));
            return true;
        }

        BlockData blockData = null;
        BlockData blockDataToReplaceWith = null;

        try {
            blockData = BlockDataArgument.getBlockData(args[0]);
        } catch (InvalidMaterialException e) {
            player.sendMessage(Component.text("Provided block type does not exist"));
            return true;
        } catch (InvalidDataException e) {
            player.sendMessage(Component.text("Provided block data is invalid"));
            return true;
        }

        try {
            blockDataToReplaceWith = BlockDataArgument.getBlockData(args[1]);
        } catch (InvalidMaterialException e) {
            player.sendMessage(Component.text("Provided block type does not exist"));
            return true;
        } catch (InvalidDataException e) {
            player.sendMessage(Component.text("Provided block data is invalid"));
            return true;
        }

        BEPlayer bePlayer = BEPlayer.getByPlayer(player);
        Selection selection = bePlayer.getSelection();
        if (selection == null) {
            player.sendMessage(Component.text("You need to select 2 points to do this"));
            return true;
        }

        BlockEditAPI.runOperation(selection, 1, new ReplaceOperation(blockData, blockDataToReplaceWith));
        player.sendMessage(Component.text("Replacing blocks..."));
        return true;
    }
}
