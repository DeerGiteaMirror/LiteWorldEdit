package site.deercloud.liteworldedit.Jobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Job {
    protected World _world;
    protected Location _location;
    protected Long _time;
    protected Player _creator;
    protected Inventory _inventory;

    public Long get_time() {
        return _time;
    }

    public Player get_creator() {
        return _creator;
    }

    public JobErrCode Do() {
        // nothing
        return JobErrCode.OK;
    }

    static public Boolean in_range(Player player, Location location) {
        if (player.getWorld() != location.getWorld()) {
            return false;
        }
        if (player.getLocation().distance(location) > 128) {
            player.sendMessage(ChatColor.RED + "不允许超过128格操作！");
            return false;
        }
        return true;
    }

}
