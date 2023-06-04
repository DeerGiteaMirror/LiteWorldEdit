package site.deercloud.liteworldedit.Jobs;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import site.deercloud.liteworldedit.LoggerX;

import java.util.HashMap;

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
            LoggerX.debug("超出距离！");
            return JobErrCode.OUT_OF_RANGE;
        }
        // 跳过非空气方块
        Block raw_block = _world.getBlockAt(_location);
        if (!raw_block.isEmpty()) {
            LoggerX.debug("目标方块不是空气！");
            return JobErrCode.NOT_AIR_BLOCK;
        }
        // 获取到玩家物品中材料的第一个堆叠
        ItemStack stack;
        int stack_index = _inventory.first(_block);
        if (stack_index == -1) {
            // 物品栏没有就去潜影盒里找
            if (!moveBlockFromShulkerBoxToInv()) {
                return JobErrCode.NOT_ENOUGH_ITEMS;
            }
            stack_index = _inventory.first(_block);
            if (stack_index == -1) {
                LoggerX.debug("物品中没有该材料！");
                return JobErrCode.NOT_ENOUGH_ITEMS;
            }
        }
        stack = _inventory.getItem(stack_index);
        if (stack == null) {
            LoggerX.debug("物品中没有该材料！");
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

    private boolean moveBlockFromShulkerBoxToInv() {
        HashMap<Integer, ?> shulkerBoxes = _inventory.all(Material.SHULKER_BOX);
        for (Integer index : shulkerBoxes.keySet()) {
            LoggerX.debug("找到潜影盒：" + index);
            ItemStack itemStack = _inventory.getItem(index);
            if (itemStack == null) {
                continue;
            }
            if (!(itemStack.getItemMeta() instanceof BlockStateMeta)) {
                LoggerX.debug("不是BlockStateMeta！");
                continue;
            }
            BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
            if (!(meta.getBlockState() instanceof ShulkerBox)) {
                LoggerX.debug("不是潜影盒！");
                continue;
            }
            ShulkerBox shulkerBox = (ShulkerBox) meta.getBlockState();
            Inventory boxInv = shulkerBox.getInventory();
            int item_idx = boxInv.first(_block);
            if (item_idx == -1) {
                return false;
            }
            // 把物品放到玩家物品栏
            _inventory.addItem(boxInv.getItem(item_idx));
            // 把潜影盒中的物品移除
            shulkerBox.getInventory().setItem(item_idx, null);
            // 更新潜影盒
            meta.setBlockState(shulkerBox);
            itemStack.setItemMeta(meta);
            return true;
        }
        LoggerX.debug("潜影盒中没有该材料！");
        return false;
    }
}
