package cn.lunadeer.liteworldedit.Jobs;

import cn.lunadeer.liteworldedit.LoggerX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Absorb extends Job {
    public Absorb(Location location, Player player) {
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
        // 跳过非流体
        if (!raw_block.isLiquid()) {
            LoggerX.debug("目标方块不是流体！");
            return JobErrCode.NOT_LIQUID;
        }
        HashMap<Integer, ?> sponge = _creator.getInventory().all(Material.SPONGE);
        if (sponge.size() == 0) {
            return JobErrCode.NO_SPONGE;
        }
        // 模拟海绵吸水事件
        BlockPlaceEvent event = new BlockPlaceEvent(raw_block, raw_block.getState(), raw_block, new ItemStack(Material.SPONGE), _creator, true, null);
        Bukkit.getPluginManager().callEvent(event);
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
        if (!event.isCancelled()) {
            raw_block.setType(Material.SPONGE);
            raw_block.setType(Material.AIR);
            // 损坏镐
            useNetherPickaxe(pickaxe);
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}
