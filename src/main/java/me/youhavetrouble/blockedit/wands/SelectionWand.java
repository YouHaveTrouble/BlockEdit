package me.youhavetrouble.blockedit.wands;

import me.youhavetrouble.blockedit.BELocale;
import me.youhavetrouble.blockedit.BEPlayer;
import me.youhavetrouble.blockedit.api.BlockEditAPI;
import me.youhavetrouble.blockedit.api.BlockEditWand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@SuppressWarnings("UnstableApiUsage")
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
        String wandId = BlockEditAPI.getWandsHandler().getWandId(event.getItem());
        if (wandId == null) return;
        if (!wandId.equals(getId())) return;
        if (!player.hasPermission(getPermission())) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        Action action = event.getAction();
        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            event.setCancelled(true);
            Location location = block.getLocation();
            BEPlayer.getByPlayer(player).setSelectionPoint1(location);
            String locationString = "X: " + location.blockX() + " Y: " + location.blockY() + " Z: " + location.blockZ();
            player.sendMessage(Component.text(BELocale.getLocale(player.locale()).firstPositionSet.formatted(locationString), NamedTextColor.GRAY));
            return;
        }
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            Location location = block.getLocation();
            BEPlayer.getByPlayer(player).setSelectionPoint2(location);
            String locationString = "X: " + location.blockX() + " Y: " + location.blockY() + " Z: " + location.blockZ();
            player.sendMessage(Component.text(BELocale.getLocale(player.locale()).secondPositionSet.formatted(locationString), NamedTextColor.GRAY));
            return;
        }
    }

}
