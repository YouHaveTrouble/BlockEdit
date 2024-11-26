package me.youhavetrouble.blockedit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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
                                    if (!(ctx.getSource().getSender() instanceof Player player)) {
                                        ctx.getSource().getSender().sendMessage(Component.text("Only players can use this command", NamedTextColor.RED));
                                        return Command.SINGLE_SUCCESS;
                                    }
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
                    if (!(ctx.getSource().getSender() instanceof Player player)) {
                        ctx.getSource().getSender().sendMessage(Component.text("Only players can use this command", NamedTextColor.RED));
                        return Command.SINGLE_SUCCESS;
                    }
                    ItemStack wand = BlockEditAPI.getWandsHandler().getWand("select");
                    player.getInventory().addItem(wand);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

}
