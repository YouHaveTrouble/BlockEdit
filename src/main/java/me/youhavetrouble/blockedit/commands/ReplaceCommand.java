package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
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
                if (material.isBlock())
                    suggestions.add(material.name().toLowerCase());
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
        Material materialToReplace = Material.getMaterial(args[1].toUpperCase());
        if (materialToReplace == null) {
            player.sendMessage(Component.text("Provided material does not exist"));
            return true;
        }
        Material material = Material.getMaterial(args[0].toUpperCase());
        if (material == null) {
            player.sendMessage(Component.text("Provided material does not exist"));
            return true;
        }

        BlockData blockData = material.createBlockData();
        BlockData blockDataToReplaceWith = materialToReplace.createBlockData();
        BEPlayer bePlayer = BEPlayer.getByPlayer(player);
        Selection selection = bePlayer.getSelection();
        if (selection == null) {
            player.sendMessage(Component.text("You need to select 2 points to do this"));
            return true;
        }
        BlockEditAPI.runOperation(selection, 1, new ReplaceOperation(blockData, blockDataToReplaceWith));
        return true;
    }
}
