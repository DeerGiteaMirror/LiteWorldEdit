package site.deercloud.liteworldedit.JobGenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Place;
import site.deercloud.liteworldedit.LiteWorldEdit;
import site.deercloud.liteworldedit.Managers.Point;

public class OverLay {

    public static void overLay(Player player, World world, Point p1, Point p2, Material block) {
        for (int x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); x++) {
            for (int z = Math.min(p1.z, p2.z); z <= Math.max(p1.z, p2.z); z++) {
                for (int y = Math.min(p1.y, p2.y); y <= Math.max(p1.y, p2.y); y++) {
                    Block block1 = world.getBlockAt(x, y, z);
                    if (block1.getType() == Material.AIR) {
                        Location location = new Location(world, (double) x, (double) y, (double) z);
                        Place place_job = new Place(location, player, block);
                        LiteWorldEdit.instance.getCache().getPlayer(player).addJob(place_job);
                        break;
                    }
                }
            }
        }
    }
}
