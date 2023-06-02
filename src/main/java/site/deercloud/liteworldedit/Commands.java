package site.deercloud.liteworldedit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import site.deercloud.liteworldedit.JobGenerator.Empty;
import site.deercloud.liteworldedit.JobGenerator.Fill;
import site.deercloud.liteworldedit.Jobs.Job;
import site.deercloud.liteworldedit.Jobs.Place;
import site.deercloud.liteworldedit.Jobs.Remove;
import site.deercloud.liteworldedit.Managers.Point;

import java.util.*;

public class Commands implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
                sender.sendMessage(ChatColor.GREEN + "LiteWorldEdit by DeerCloud");
                sender.sendMessage(ChatColor.GREEN + "使用 /lwe help 查看帮助。");
                return true;
        }
        if (Objects.equals(args[0], "point") || Objects.equals(args[0], "p")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 5) {
                    try {
                        Integer index = Integer.parseInt(args[1]);
                        int x = Integer.parseInt(args[2]);
                        int y = Integer.parseInt(args[3]);
                        int z = Integer.parseInt(args[4]);
                        // 选择的点不允许超过128格范围
                        if (!Job.in_range(player, new Location(player.getWorld(), x, y, z))) {
                            return true;
                        }
                        Point point = new Point(x, y, z, player);
                        LiteWorldEdit.instance.getCache().add_point(player, index, point);
                        sender.sendMessage("点 " + index + " 已设置为 " + x + ", " + y + ", " + z + "。");
                    } catch (NumberFormatException e) {
                        sender.sendMessage("参数错误。");
                    }
                } else {
                    sender.sendMessage("参数错误。");
                }
            } else {
                sender.sendMessage("该命令只能由玩家执行。");
            }
        } else if (Objects.equals(args[0], "points")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Map<Integer, Point> points = LiteWorldEdit.instance.getCache().get_points(player);
                if (points != null) {
                    sender.sendMessage("你创建的点：");
                    for (Map.Entry<Integer, Point> entry : points.entrySet()) {
                        Point point = entry.getValue();
                        sender.sendMessage(entry.getKey() + ": " + point.x + ", " + point.y + ", " + point.z);
                    }
                } else {
                    sender.sendMessage("你没有设置任何点。");
                }
            } else {
                sender.sendMessage("该命令只能由玩家执行。");
            }
        } else if (Objects.equals(args[0], "place")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 3) {
                    try {
                        Integer indexA = Integer.parseInt(args[1]);
                        Integer indexB = Integer.parseInt(args[2]);
                        Map<Integer, Point> points = LiteWorldEdit.instance.getCache().get_points(player);
                        if (points == null) {
                            sender.sendMessage("你没有设置任何点。");
                            return true;
                        }
                        Point pointA = points.get(indexA);
                        Point pointB = points.get(indexB);
                        if (pointA == null || pointB == null) {
                            sender.sendMessage("点不存在。");
                            return true;
                        }
                        if (out_of_region(pointA, pointB)) {
                            sender.sendMessage("选择的区域不可以超过 " + LiteWorldEdit.instance.getConfigMgr().getXMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getYMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getZMax() + "。");
                            return true;
                        }
                        ItemStack items_in_hand = player.getInventory().getItemInMainHand();
                        if (!items_in_hand.getType().isBlock() || items_in_hand.getType() == Material.AIR) {
                            sender.sendMessage("你手上没有方块。");
                            return true;
                        }
                        Material material = Material.getMaterial(items_in_hand.getType().name());
                        Fill.fill(player, player.getWorld(), pointA, pointB, material);
                        sender.sendMessage("已添加任务。");
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage("参数错误。");
                    }
                } else {
                    sender.sendMessage("参数错误。");
                }
            } else {
                sender.sendMessage("该命令只能由玩家执行。");
            }
        } else if (Objects.equals(args[0], "break")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 3) {
                    try {
                        Integer indexA = Integer.parseInt(args[1]);
                        Integer indexB = Integer.parseInt(args[2]);
                        Map<Integer, Point> points = LiteWorldEdit.instance.getCache().get_points(player);
                        if (points == null) {
                            sender.sendMessage("你没有设置任何点。");
                            return true;
                        }
                        Point pointA = points.get(indexA);
                        Point pointB = points.get(indexB);
                        if (pointA != null && pointB != null) {
                            if (out_of_region(pointA, pointB)) {
                                sender.sendMessage("选择的区域不可以超过 " + LiteWorldEdit.instance.getConfigMgr().getXMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getYMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getZMax() + "。");
                                return true;
                            }
                            Empty.empty(player, player.getWorld(), pointA, pointB);
                            sender.sendMessage("已添加任务。");
                        } else {
                            sender.sendMessage("点不存在。");
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage("参数错误。");
                    }
                } else {
                    sender.sendMessage("参数错误。");
                }
            } else {
                sender.sendMessage("该命令只能由玩家执行。");
            }
        } else if (Objects.equals(args[0], "help")) {
            print_help(sender);
        } else {
            sender.sendMessage("参数错误。");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("point", "p", "points", "place", "break", "help");
        } else if (args.length == 2) {
            switch (args[1]) {
                case "point":
                    return Collections.singletonList("[点序号(整数)] [x] [y] [z]");
                case "place":
                    return Collections.singletonList("[点序号A] [点序号B] (需要手持被放置的方块)");
                case "break":
                    return Collections.singletonList("[点序号A] [点序号B] (需要拥有下届合金锄)");
            }
        }
        return Collections.emptyList();
    }

    public void print_help(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "LiteWorldEdit 帮助");
        sender.sendMessage(ChatColor.GREEN + "/lwe point [点序号(整数)] [x] [y] [z] - 创建点");
        sender.sendMessage(ChatColor.GREEN + "/lwe points - 查看所有点");
        sender.sendMessage(ChatColor.GREEN + "/lwe place [点序号A] [点序号B] - (在AB点对角线间放置方块 - 需要手持被放置的方块)");
        sender.sendMessage(ChatColor.GREEN + "/lwe break [点序号A] [点序号B] - (破坏AB点对角线间方块 - 需要拥有下届合金镐)");
        sender.sendMessage(ChatColor.GREEN + "/lwe help - 查看帮助");
    }

    static public boolean out_of_region(Point A, Point B) {
        int minX = Math.min(A.x, B.x);
        int minY = Math.min(A.y, B.y);
        int minZ = Math.min(A.z, B.z);
        int maxX = Math.max(A.x, B.x);
        int maxY = Math.max(A.y, B.y);
        int maxZ = Math.max(A.z, B.z);
        return !(maxX - minX <= LiteWorldEdit.instance.getConfigMgr().getXMax() && maxY - minY <= LiteWorldEdit.instance.getConfigMgr().getYMax() && maxZ - minZ <= LiteWorldEdit.instance.getConfigMgr().getZMax());
    }

}
