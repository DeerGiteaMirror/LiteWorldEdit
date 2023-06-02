package site.deercloud.liteworldedit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        LiteWorldEdit.instance.getCache().deletePlayerCache(event.getPlayer());
    }
}
