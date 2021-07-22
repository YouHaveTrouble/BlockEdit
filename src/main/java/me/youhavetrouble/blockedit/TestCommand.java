package me.youhavetrouble.blockedit;

import me.youhavetrouble.blockedit.util.ChunkWork;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

    BukkitTask task;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            Location location = player.getLocation();
            if (task != null) {
                task.cancel();
            }
            // That's how manually checking if selection is valid looks like.
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(BlockEdit.getPlugin(),() -> {
                BoundingBox box = BEPlayer.getByPlayer(player).getSelection();
                if (box == null) return;
                for (int y = 0; y< 255; y++) {
                    location.getWorld().spawnParticle(Particle.END_ROD, box.getMaxX(), y, box.getMaxZ(), 0, 0.01, 0.01, 0.01);
                    location.getWorld().spawnParticle(Particle.END_ROD, box.getMinX(), y, box.getMinZ(), 0, 0.01, 0.01, 0.01);
                }
            }, 0, 4);



        }
        return true;
    }
}
