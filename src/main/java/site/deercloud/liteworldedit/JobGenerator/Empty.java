package site.deercloud.liteworldedit.JobGenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Remove;
import site.deercloud.liteworldedit.LiteWorldEdit;
import site.deercloud.liteworldedit.Managers.Point;

public class Empty {
    public static void empty(Player player, World world, Point p1, Point p2) {
        for (int y = Math.max(p1.y, p2.y); y >= Math.min(p1.y, p2.y); y--) {
            for (int x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); x++) {
                for (int z = Math.min(p1.z, p2.z); z <= Math.max(p1.z, p2.z); z++) {
                    Location location = new Location(world, (double) x, (double) y, (double) z);
                    Remove remove_job = new Remove(location, player);
                    LiteWorldEdit.instance.getCache().getPlayer(player).addJob(remove_job);
                }
            }
        }

    }
}
