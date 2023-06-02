package site.deercloud.liteworldedit.JobGenerator;

import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Place;
import site.deercloud.liteworldedit.LiteWorldEdit;
import site.deercloud.liteworldedit.Managers.Point;

public class Fill {

    public static void fill(Player player, World world, Point p1, Point p2, Material block) {
        for (int x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); x++) {
            for (int y = Math.max(p1.y, p2.y); y >= Math.min(p1.y, p2.y); y--) {
                for (int z = Math.min(p1.z, p2.z); z <= Math.max(p1.z, p2.z); z++) {
                    Location location = new Location(world, (double) x, (double) y, (double) z);
                    Place place_job = new Place(location, player, block);
                    LiteWorldEdit.instance.getCache().addJob(player, place_job);
                }
            }
        }

    }
}
