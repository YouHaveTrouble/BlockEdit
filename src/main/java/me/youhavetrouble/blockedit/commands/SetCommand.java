package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.WorkSplitter;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
import me.youhavetrouble.blockedit.operations.SetOperation;
import me.youhavetrouble.blockedit.util.Selection;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length == 0) {
            player.sendMessage(Component.text("You need to provide block type"));
            return true;
        } else {
            Material material = Material.getMaterial(args[0].toUpperCase());
            if (material == null) {
                player.sendMessage(Component.text("Provided material does not exist"));
                return true;
            }
            BlockData blockData = material.createBlockData();
            BEPlayer bePlayer = BEPlayer.getByPlayer(player);
            BoundingBox selection = bePlayer.getSelection();
            if (selection == null) {
                player.sendMessage(Component.text("You need to select 2 points to do this"));
                return true;
            }
            BlockEditAPI.runOperation(new Selection(selection, bePlayer.getSelectionWorld()), 1, new SetOperation(blockData));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> suggestions = new ArrayList<>();
            for (Material material : Material.values()) {
                if (material.isBlock())
                    suggestions.add(material.name().toLowerCase());
            }
            return StringUtil.copyPartialMatches(args[0], suggestions, new ArrayList<>());
        }
        return null;
    }


}
