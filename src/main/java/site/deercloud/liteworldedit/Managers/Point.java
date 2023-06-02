package site.deercloud.liteworldedit.Managers;

import org.bukkit.entity.Player;

public class Point {
    public Point(Integer x, Integer y, Integer z, Player player) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
    }
    public Integer x;
    public Integer y;
    public Integer z;

    public Player player;
}
