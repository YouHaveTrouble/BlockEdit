package me.youhavetrouble.blockedit.wands;

import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.BlockEdit;
import me.youhavetrouble.blockedit.api.BlockEditWand;
import me.youhavetrouble.blockedit.api.BlockEditWands;
import me.youhavetrouble.blockedit.optionals.SelectionHighlight;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectionWand implements Listener, BlockEditWand {

    @Override
    public String getId() {
        return "select";
    }

    @Override
    public Component getName() {
        return Component.text("Selection Wand").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public String getPermission() {
        return "blockedit.selectwand";
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSelectBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String wandId = BlockEditWands.isWand(event.getItem());
        if (wandId == null) return;
        if (!wandId.equals(getId())) return;
        if (!player.hasPermission(getPermission())) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        Action action = event.getAction();
        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            event.setCancelled(true);
            BEPlayer.getByPlayer(player).setSelectionPoint1(block.getLocation());
            highlightPoints(player);
            player.sendMessage(Component.text("First point set"));
            return;
        }
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            BEPlayer.getByPlayer(player).setSelectionPoint2(block.getLocation());
            highlightPoints(player);
            player.sendMessage(Component.text("Second point set"));
            return;
        }
    }

    private void highlightPoints(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(BlockEdit.getPlugin(), () -> {
            BEPlayer bePlayer = BEPlayer.getByPlayer(player);
            SelectionHighlight.sendStop(player);

            Location selection1 = bePlayer.getSelectionPoint1();
            Location selection2 = bePlayer.getSelectionPoint2();

            if (selection1 != null && selection1.equals(selection2)) {
                SelectionHighlight.highlightBlock(player, bePlayer.getSelectionPoint1(), "#ffffff", "Selection Points", 10000);
                return;
            }
            if (selection1 != null && !selection1.equals(selection2)) {
                SelectionHighlight.highlightBlock(player, bePlayer.getSelectionPoint1(), "#ffffff", "Selection Point 1", 10000);
            }
            if (selection2 != null && !selection2.equals(selection1)) {
                SelectionHighlight.highlightBlock(player, bePlayer.getSelectionPoint2(), "#ffffff", "Selection Point 2", 10000);
            }





        });
    }
}
