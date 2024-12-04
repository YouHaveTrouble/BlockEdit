package me.youhavetrouble.blockedit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
import me.youhavetrouble.blockedit.operations.PasteOperation;
import me.youhavetrouble.blockedit.operations.ReplaceOperation;
import me.youhavetrouble.blockedit.operations.SetOperation;
import me.youhavetrouble.blockedit.util.Selection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("UnstableApiUsage")
public class BlockEditCommands {

    protected static void registerCommands(@NotNull BlockEdit plugin) {
        LifecycleEventManager<@NotNull Plugin> manager = plugin.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register(
                    wandCommand(),
                    "Gives the player chosen wand"
            );

            commands.register(
                    copyCommand(),
                    "Copies the selected area to the clipboard"
            );

            commands.register(
                    pasteCommand(),
                    "Pastes the clipboard at the player's location"
            );

            commands.register(
                    deselCommand(),
                    "Resets the player's selection"
            );

            commands.register(
                    pos1Command(),
                    "Sets the first point of the player's selection"
            );

            commands.register(
                    pos2Command(),
                    "Sets the second point of the player's selection"
            );

            commands.register(
                    rotateCommand(),
                    "Rotates the clipboard by the specified angle"
            );

            commands.register(
                    setCommand(),
                    "Sets the selected area to the specified block"
            );

            commands.register(
                    replaceCommand(),
                    "Replaces the specified block with another block"
            );

        });

    }

    private static LiteralCommandNode<CommandSourceStack> wandCommand() {
        return Commands.literal("wand")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.wand");
                })
                .then(
                        Commands.argument("wand_id", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    String[] inputArgs = ctx.getInput().split(" ");
                                    String lastArg = inputArgs[inputArgs.length - 1];
                                    for (String id : BlockEditAPI.getWandsHandler().getWandIds()) {
                                        if (inputArgs.length != 1) continue;
                                        if (id.startsWith(lastArg)) continue;
                                        builder.suggest(id);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String wandId = ctx.getArgument("wand_id", String.class);
                                    ItemStack wand = BlockEditAPI.getWandsHandler().getWand(wandId);
                                    if (wand == null) {
                                        ctx.getSource().getSender().sendMessage(Component.text("Could not find wand with id %s".formatted(wandId), NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    player.getInventory().addItem(wand);
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    ItemStack wand = BlockEditAPI.getWandsHandler().getWand("select");
                    player.getInventory().addItem(wand);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> copyCommand() {
        return Commands.literal("copy")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.copy");
                })
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    BEPlayer bePlayer = BEPlayer.getByPlayer(player);
                    try {
                        bePlayer.setClipboardFromSelection();
                        player.sendMessage(Component.text("Copied selection to clipboard"));
                    } catch (IllegalStateException e) {
                        player.sendMessage(Component.text("You need to select an area first"));
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> deselCommand() {
        return Commands.literal("desel")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.desel");
                })
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    BEPlayer.getByPlayer(player).resetSelection();
                    player.sendMessage(Component.text("You have reset your selection"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> pos1Command() {
        return Commands.literal("pos1")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.pos");
                })
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    BEPlayer.getByPlayer(player).setSelectionPoint1(player.getLocation().toBlockLocation());
                    player.sendMessage(Component.text("First point set at your location"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> pos2Command() {
        return Commands.literal("pos2")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.pos");
                })
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    BEPlayer.getByPlayer(player).setSelectionPoint2(player.getLocation().toBlockLocation());
                    player.sendMessage(Component.text("Second point set at your location"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> pasteCommand() {
        return Commands.literal("paste")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.paste");
                })
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    BEPlayer bePlayer = BEPlayer.getByPlayer(player);
                    Vector playerLocationVector = player.getLocation().toBlockLocation().toVector();

                    HashMap<Vector, BlockState> absoluteBlocks = new HashMap<>(bePlayer.getClipboard().getBlocks().size());

                    bePlayer.getClipboard().getBlocks().forEach((vector, blockState) -> {
                        Vector absolutePosition = vector.clone().add(playerLocationVector);
                        absoluteBlocks.put(absolutePosition, blockState);
                    });

                    Selection selection = Selection.fromClipboard(absoluteBlocks.keySet(), player.getWorld());
                    BlockEditAPI.runOperation(selection, 1, new PasteOperation(absoluteBlocks));
                    player.sendMessage(Component.text("Pasting clipboard..."));

                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> rotateCommand() {
        return Commands.literal("rotate")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.rotate");
                })
                .then(
                        Commands.argument("angle", DoubleArgumentType.doubleArg(-360, 360))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    double angle = ctx.getArgument("angle", Double.class);
                                    BEPlayer bePlayer = BEPlayer.getByPlayer(player);
                                    bePlayer.getClipboard().rotate(angle);
                                    player.sendMessage(Component.text("Rotated clipboard by " + angle + " degrees"));
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> setCommand() {
        return Commands.literal("set")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.set");
                })
                .then(
                        Commands.argument("block", ArgumentTypes.blockState())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    BEPlayer bePlayer = BEPlayer.getByPlayer(player);
                                    BlockState blockState = ctx.getArgument("block", BlockState.class);
                                    Selection selection = bePlayer.getSelection();
                                    BlockEditAPI.runOperation(selection, 1, new SetOperation(blockState));
                                    player.sendMessage(Component.text("Setting blocks..."));
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .then(
                        Commands.argument("chunks_per_tick", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    BEPlayer bePlayer = BEPlayer.getByPlayer(player);
                                    BlockState blockState = ctx.getArgument("block", BlockState.class);
                                    int chunksPerTick = ctx.getArgument("chunks_per_tick", Integer.class);
                                    Selection selection = bePlayer.getSelection();
                                    BlockEditAPI.runOperation(selection, chunksPerTick, new SetOperation(blockState));
                                    player.sendMessage(Component.text("Setting blocks..."));
                                    return Command.SINGLE_SUCCESS;
                                }))
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> replaceCommand() {
        return Commands.literal("replace")
                .requires(css -> {
                    if (!(css.getSender() instanceof Player player)) return false;
                    return player.hasPermission("blockedit.command.replace");
                })
                .then(
                        Commands.argument("to_replace", ArgumentTypes.blockState())
                )
                .then(
                        Commands.argument("replace_with", ArgumentTypes.blockState())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    BEPlayer bePlayer = BEPlayer.getByPlayer(player);
                                    BlockState toReplace = ctx.getArgument("to_replace", BlockState.class);
                                    BlockState replaceWith = ctx.getArgument("replace_with", BlockState.class);
                                    Selection selection = bePlayer.getSelection();
                                    BlockEditAPI.runOperation(selection, 1, new ReplaceOperation(toReplace, replaceWith));
                                    player.sendMessage(Component.text("Replacing blocks..."));
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .then(
                        Commands.argument("chunks_per_tick", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    BEPlayer bePlayer = BEPlayer.getByPlayer(player);
                                    BlockState toReplace = ctx.getArgument("to_replace", BlockState.class);
                                    BlockState replaceWith = ctx.getArgument("replace_with", BlockState.class);
                                    int chunksPerTick = ctx.getArgument("chunks_per_tick", Integer.class);
                                    Selection selection = bePlayer.getSelection();
                                    BlockEditAPI.runOperation(selection, chunksPerTick, new ReplaceOperation(toReplace, replaceWith));
                                    player.sendMessage(Component.text("Replacing blocks..."));
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .build();
    }

}
