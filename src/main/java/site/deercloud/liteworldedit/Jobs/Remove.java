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
import site.deercloud.liteworldedit.LiteWorldEdit;
import site.deercloud.liteworldedit.LoggerX;

import java.util.HashMap;

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
            LoggerX.debug("超出距离！");
            return JobErrCode.OUT_OF_RANGE;
        }
        Block raw_block = _world.getBlockAt(_location);
        // 跳过不破坏的对象
        if (raw_block.isLiquid() || raw_block.isEmpty() || raw_block.getType().getHardness() == -1) {
            LoggerX.debug("目标方块是液体或空气或不可破坏！");
            return JobErrCode.NO_BREAKABLE;
        }
        // 获取玩家背包中的下届合金镐
        HashMap<Integer, ?> pickaxes = _inventory.all(Material.NETHERITE_PICKAXE);
        if (pickaxes.size() == 0) {
            return JobErrCode.NO_PICKAXE;
        }
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
        // 没有合适的镐
        if (pickaxe == null) {
            return JobErrCode.NOT_ENOUGH_DURATION;
        }
        BlockBreakEvent event = new BlockBreakEvent(raw_block, _creator);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            raw_block.setType(Material.AIR);
            if (LiteWorldEdit.instance.getConfigMgr().isDropItems()) {
                raw_block.getWorld().dropItemNaturally(raw_block.getLocation(), new ItemStack(raw_block.getType()));
            }
            // 获取耐久附魔
            int durability = pickaxe.getEnchantmentLevel(Enchantment.DURABILITY);
            double random = Math.random();
            if (random < 1.0 / (durability + 1)) {
                // 扣除耐久
                pickaxe_damage.setDamage(pickaxe_damage.getDamage() + 1);
                pickaxe.setItemMeta((ItemMeta) pickaxe_damage);
            }
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}
