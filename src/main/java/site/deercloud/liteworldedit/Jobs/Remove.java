package site.deercloud.liteworldedit.Jobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import site.deercloud.liteworldedit.LiteWorldEdit;
import site.deercloud.liteworldedit.LoggerX;

import java.util.HashMap;

public class Remove extends Job {

    public Remove(Location location, Player player) {
        super(player.getWorld(), location, player);
    }

    @Override
    public JobErrCode Do() {
        Player _creator = this.get_creator();
        Location _location = this.get_location();
        World _world = this.get_world();
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
        HashMap<Integer, ?> pickaxes = getNetherPickaxes(_creator);
        if (pickaxes.size() == 0) {
            return JobErrCode.NO_PICKAXE;
        }
        ItemStack pickaxe = getUsableNetherPickaxe(pickaxes, _creator);
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
            // 损坏镐
            useNetherPickaxe(pickaxe);
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}
