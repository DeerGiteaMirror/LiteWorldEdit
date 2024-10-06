package cn.lunadeer.liteworldedit.Jobs;

import cn.lunadeer.liteworldedit.LiteWorldEdit;
import cn.lunadeer.liteworldedit.LoggerX;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

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
        if (pickaxes.isEmpty()) {
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
            Material block_type = raw_block.getType();
            raw_block.setType(Material.AIR);
            if (LiteWorldEdit.instance.getConfigMgr().isDropItems()) {
                raw_block.getWorld().dropItemNaturally(raw_block.getLocation(), new ItemStack(block_type));
            }
            // 损坏镐
            if (!_creator.isOp() && _creator.getGameMode() != GameMode.CREATIVE) {
                useNetherPickaxe(pickaxe);
            }
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}
