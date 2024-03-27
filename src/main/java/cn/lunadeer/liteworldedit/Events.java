package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.Managers.Point;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        LiteWorldEdit.instance.getCache().playerQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        LiteWorldEdit.instance.getCache().playerJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void selectPoint(PlayerInteractEvent event) {
        XPlayer xplayer = LiteWorldEdit.instance.getCache().getPlayer(event.getPlayer());
        if (!xplayer.isSelectMode()) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.NETHERITE_PICKAXE) {
            return;
        }
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if (block == null) {
            return;
        }

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        Point point = new Point(x, y, z, player);

        if (action == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            Notification.info(player, "已选择第一个点: " + x + " " + y + " " + z);
            xplayer.addPoint(1, point);
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            Notification.info(player, "已选择第二个点: " + x + " " + y + " " + z);
            xplayer.addPoint(2, point);
        }
    }
}
