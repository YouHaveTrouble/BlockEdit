package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
import me.youhavetrouble.blockedit.commands.arguments.BlockDataArgument;
import me.youhavetrouble.blockedit.commands.arguments.InvalidDataException;
import me.youhavetrouble.blockedit.commands.arguments.InvalidMaterialException;
import me.youhavetrouble.blockedit.operations.SetOperation;
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

public class SetCommand extends Command {

    public SetCommand() {
        super("set");
        setPermission("blockedit.command.set");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            for (Material material : Material.values()) {
                if (material.isBlock())
                    suggestions.add(material.name().toLowerCase());
            }
            return StringUtil.copyPartialMatches(args[0], suggestions, new ArrayList<>());
        }
        return suggestions;
    }


    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return true;

        BEPlayer bePlayer = BEPlayer.getByPlayer(player);
        Selection selection = bePlayer.getSelection();
        if (selection == null) {
            player.sendMessage(Component.text("You need to select 2 points to do this"));
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        BlockData blockData;
        try {
            blockData = BlockDataArgument.getBlockData(args[0]);
        } catch (InvalidMaterialException e) {
            player.sendMessage(Component.text("Provided block type does not exist"));
            return true;
        } catch (InvalidDataException e) {
            player.sendMessage(Component.text("Provided block data is invalid"));
            return true;
        }

        BlockEditAPI.runOperation(selection, 1, new SetOperation(blockData));
        player.sendMessage(Component.text("Setting blocks..."));
        return true;
    }

    @Override
    public @NotNull String getUsage() {
        return "/set <block[block=data]>";
    }

}
