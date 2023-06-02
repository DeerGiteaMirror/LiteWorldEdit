package site.deercloud.liteworldedit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.EventListener;

public class Events implements Listener {


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        LiteWorldEdit.instance.getCache().delete_player_points(event.getPlayer());
        LiteWorldEdit.instance.getCache().delete_player_jobs(event.getPlayer());
    }
}
