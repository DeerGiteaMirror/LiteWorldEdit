package site.deercloud.liteworldedit.Jobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import site.deercloud.liteworldedit.LoggerX;
import site.deercloud.liteworldedit.Notification;

import java.util.HashMap;

public class Job {
    private final World _world;
    private final Location _location;
    private final Long _time;
    private final Player _creator;
    private final Inventory _inventory;

    public Long get_time() {
        return _time;
    }

    public Player get_creator() {
        return _creator;
    }

    public Inventory get_inventory() {
        return _inventory;
    }

    public Location get_location() {
        return _location;
    }

    public World get_world() {
        return _world;
    }

    public Job(World world, Location location, Player player) {
        _world = world;
        _location = location;
        _creator = player;
        _time = System.currentTimeMillis();
        _inventory = player.getInventory();
    }

    public JobErrCode Do() {
        // nothing
        return JobErrCode.OK;
    }

    static public Boolean in_range(Player player, Location location) {
        if (!player.getWorld().getName().equals(location.getWorld().getName())) {
            return false;
        }
        if (player.getLocation().distance(location) > 128) {
            Notification.error(player, "不允许超过128格操作！");
            return false;
        }
        return true;
    }

    public static HashMap<Integer, ?> getNetherPickaxes(Player player) {
        Inventory _inventory = player.getInventory();
        return _inventory.all(Material.NETHERITE_PICKAXE);
    }

    public static ItemStack getUsableNetherPickaxe(HashMap<Integer, ?> pickaxes, Player player) {
        Inventory _inventory = player.getInventory();
        ItemStack pickaxe = null;
        Damageable pickaxe_damage = null;
        for (Integer index : pickaxes.keySet()) {
            ItemStack p = _inventory.getItem(index);
            if (p == null) {
                LoggerX.debug(index + " 获取到的下界合金镐为空！");
                continue;
            }
            ItemMeta pickaxe_meta = p.getItemMeta();
            if (pickaxe_meta == null) {
                LoggerX.debug(index + " 获取到的下界合金镐元数据为空！");
                continue;
            }
            if (!(pickaxe_meta instanceof Damageable)) {
                LoggerX.debug(index + " 无法转换为Damageable！");
                continue;
            }
            // 如果耐久小于10，提示玩家
            pickaxe_damage = (Damageable) pickaxe_meta;
            if (pickaxe_damage.getDamage() >= 2031 - 10) {
                LoggerX.debug(index + " 下界合金镐耐久太低！");
                continue;
            }
            pickaxe = p;
            break;
        }
        return pickaxe;
    }

    public static ItemStack useNetherPickaxe(ItemStack pickaxe) {
        int durability = pickaxe.getEnchantmentLevel(Enchantment.DURABILITY);
        double random = Math.random();
        if (random < 1.0 / (durability + 1)) {
            // 扣除耐久
            Damageable pickaxe_damage = (Damageable) pickaxe.getItemMeta();
            pickaxe_damage.setDamage(pickaxe_damage.getDamage() + 1);
            pickaxe.setItemMeta(pickaxe_damage);
        }
        return pickaxe;
    }
}
