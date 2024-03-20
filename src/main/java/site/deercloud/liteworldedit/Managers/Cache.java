package site.deercloud.liteworldedit.Managers;

import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.XPlayer;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final Map<String, XPlayer> players;

    public Cache() {
        this.players = new HashMap<>();
    }

    public void playerJoin(Player player) {
        players.put(player.getName(), new XPlayer(player));
    }

    public void playerQuit(Player player) {
        players.remove(player.getName());
    }

    public XPlayer getPlayer(Player player) {
        String name = player.getName();
        if (!players.containsKey(name)) playerJoin(player);
        return players.get(name);
    }
}
