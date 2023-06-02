package site.deercloud.liteworldedit.Jobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

enum JobErrCode {
    OK, // 无错误
    NO_PERMISSION, // 没有权限
    NO_PICKAXE, // 没有下界合金镐
    NOT_ENOUGH_DURATION, // 耐久不足
    NOT_ENOUGH_ITEMS, // 物品不足
    OUT_OF_RANGE,       // 超出距离
    NOT_AIR_BLOCK, // 不是空气方块
    NO_BREAKABLE, // 不可破坏
}

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
