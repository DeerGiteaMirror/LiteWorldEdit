package site.deercloud.liteworldedit.Jobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import site.deercloud.liteworldedit.LoggerX;

public class Remove extends Job {

    public Remove(Location location, Player player) {
        _world = player.getWorld();
        _location = location;
        _creator = player;
        _time = System.currentTimeMillis();
        _inventory = player.getInventory();
    }

    @Override
    public JobErrCode Do() {
        // 超出距离
        if (!in_range(_creator, _location)) {
            return JobErrCode.OUT_OF_RANGE;
        }
        Block raw_block = _world.getBlockAt(_location);
        // 跳过不破坏的对象
        if (raw_block.isLiquid() || raw_block.isEmpty() || raw_block.getType().getHardness() == -1) {
            return JobErrCode.NO_BREAKABLE;
        }
        // 获取玩家背包中的下届合金镐
        int stack_index = _inventory.first(Material.NETHERITE_PICKAXE);
        if (stack_index == -1) {
            return JobErrCode.NO_PICKAXE;
        }
        ItemStack pickaxe = _inventory.getItem(stack_index);
        if (pickaxe == null) {
            LoggerX.debug("获取到的下界合金镐为空！");
            return JobErrCode.NO_PICKAXE;
        }
        ItemMeta pickaxe_meta = pickaxe.getItemMeta();
        if (pickaxe_meta == null) {
            LoggerX.debug("获取到的下界合金镐元数据为空！");
            return JobErrCode.NO_PICKAXE;
        }
        if (!(pickaxe_meta instanceof Damageable)) {
            LoggerX.debug("无法转换为Damageable！");
            return JobErrCode.NO_PICKAXE;
        }
        // 如果耐久小于10，提示玩家
        Damageable damageable = (Damageable) pickaxe_meta;
        if (damageable.getDamage() >= 2031 - 10) {
            LoggerX.err(_creator, "下界合金镐耐久太低！");
            return JobErrCode.NOT_ENOUGH_DURATION;
        }
        BlockBreakEvent event = new BlockBreakEvent(raw_block, _creator);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            raw_block.setType(Material.AIR);
            // 获取耐久附魔
            int durability = pickaxe.getEnchantmentLevel(Enchantment.DURABILITY);
            double random = Math.random();
            if (random < 1.0 / (durability + 1)) {
                // 扣除耐久
                damageable.setDamage(damageable.getDamage() + 1);
                pickaxe.setItemMeta((ItemMeta) damageable);

            }
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}
