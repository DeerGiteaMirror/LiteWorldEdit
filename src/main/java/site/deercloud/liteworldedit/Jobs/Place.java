package site.deercloud.liteworldedit.Jobs;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import site.deercloud.liteworldedit.LoggerX;

public class Place extends Job {
    private final Material _block;


    public Place(Location location, Player player, Material material) {
        _world = player.getWorld();
        _location = location;
        _creator = player;
        _time = System.currentTimeMillis();
        _block = material;
        _inventory = player.getInventory();
    }

    @Override
    public JobErrCode Do() {
        // 超出距离
        if (!in_range(_creator, _location)) {
            return JobErrCode.OUT_OF_RANGE;
        }
        // 跳过非空气方块
        Block raw_block = _world.getBlockAt(_location);
        if (!raw_block.isEmpty()) {
            return JobErrCode.NOT_AIR_BLOCK;
        }
        // 获取到玩家物品中材料的第一个堆叠
        int stack_index = _inventory.first(_block);
        if (stack_index == -1) {
            return JobErrCode.NOT_ENOUGH_ITEMS;
        }
        ItemStack stack = _inventory.getItem(stack_index);
        if (stack == null) {
            return JobErrCode.NOT_ENOUGH_ITEMS;
        }

        Block block = _world.getBlockAt(raw_block.getX() + 1, raw_block.getY(), raw_block.getZ());
        // 校验是否可以放置
        BlockPlaceEvent event = new BlockPlaceEvent(
                block,
                raw_block.getState(),
                block,
                stack,
                _creator,
                true,
                EquipmentSlot.valueOf("HAND"));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            raw_block.setType(_block);
            stack.setAmount(stack.getAmount() - 1);
            return JobErrCode.OK;
        } else {
            return JobErrCode.NO_PERMISSION;
        }
    }
}
