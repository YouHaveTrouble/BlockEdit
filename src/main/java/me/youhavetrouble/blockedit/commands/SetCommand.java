package me.youhavetrouble.blockedit.commands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
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
        // TODO refactor and/or abstract this
        if (args.length > 1) {
            Material material = Material.getMaterial(args[0].toUpperCase());
            if (material == null) return suggestions;
            if (!material.isBlock()) return suggestions;
            BlockData blockData = material.createBlockData();
            String[] split = args[args.length-1].split("=");
            if (split.length == 1) {
                String datas = blockData.getAsString(false);
                String[] nameAndDatas = datas.split("\\[");
                if (nameAndDatas.length != 2) return suggestions;
                datas = nameAndDatas[1].substring(0, nameAndDatas[1].length()-2);
                String[] splitDatas = datas.split(",");
                for (String data : splitDatas) {
                    String[] splitData = data.split("=");
                    if (splitData.length != 2) continue;
                    String suggestion = splitData[0]+"=";
                    boolean alreadyUsed = false;
                    for (String arg : args) {
                        if (arg.startsWith(suggestion)) {
                            alreadyUsed = true;
                            break;
                        };
                    }
                    if (alreadyUsed) continue;
                    suggestions.add(suggestion);
                }
                return StringUtil.copyPartialMatches(split[0], suggestions, new ArrayList<>());
            }

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

        if (args.length == 0) {
            player.sendMessage(Component.text("You need to provide block type"));
            return true;
        }

        BlockData blockData;
        try {
            blockData = getBlockData(args);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Component.text("Provided block data is invalid"));
            return true;
        }

        if (blockData == null) {
            player.sendMessage(Component.text("Provided material does not exist"));
            return true;
        }

        BlockEditAPI.runOperation(selection, 1, new SetOperation(blockData));
        return true;
    }


    private BlockData getBlockData(String[] args) throws IllegalArgumentException {
        ArrayList<String> argsList = new ArrayList<>(List.of(args));
        if (argsList.size() == 0) return null;
        Material material = Material.getMaterial(argsList.get(0).toUpperCase());
        if (material == null) return null;
        argsList.remove(0);
        if (argsList.size() == 0) return material.createBlockData();
        String dataString = "[" + String.join(",", argsList) + "]";
        return material.createBlockData(dataString);
    }
}
