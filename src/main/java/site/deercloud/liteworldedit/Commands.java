package site.deercloud.liteworldedit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import site.deercloud.liteworldedit.JobGenerator.Empty;
import site.deercloud.liteworldedit.JobGenerator.Fill;
import site.deercloud.liteworldedit.JobGenerator.OverLay;
import site.deercloud.liteworldedit.Jobs.Job;
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
            return addPoint(sender, args);
        } else if (Objects.equals(args[0], "points")) {
            listPoints(sender);
            return true;
        } else if (Objects.equals(args[0], "fill")) {
            return fillTask(sender, args);
        } else if (Objects.equals(args[0], "empty")) {
            return emptyTask(sender, args);
        } else if (Objects.equals(args[0], "overlay")) {
            return overlayTask(sender, args);
        } else if (Objects.equals(args[0], "help")) {
            print_help(sender);
            return true;
        } else if (Objects.equals(args[0], "cancel")) {
            cancerJobs(sender);
            return true;
        } else if (Objects.equals(args[0], "reload")) {
            reloadConfigs(sender);
            return true;
        } else if (Objects.equals(args[0], "pause")) {
            pauseJobs(sender);
            return true;
        } else if (Objects.equals(args[0], "resume")) {
            resumeJobs(sender);
            return true;
        } else {
            sender.sendMessage("参数错误。");
        }
        return true;
    }

    private static void resumeJobs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LiteWorldEdit.instance.getCache().getQueueOf(player) == null) {
                sender.sendMessage("你没有正在进行的任务。");
            } else {
                LiteWorldEdit.instance.getCache().getQueueOf(player).resume();
                sender.sendMessage("已恢复。");
            }
        } else {
            sender.sendMessage("该命令只能由玩家执行。");
        }
    }

    private static void pauseJobs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LiteWorldEdit.instance.getCache().getQueueOf(player) == null) {
                sender.sendMessage("你没有正在进行的任务。");
            } else {
                LiteWorldEdit.instance.getCache().getQueueOf(player).pause();
                sender.sendMessage("已暂停。");
            }
        } else {
            sender.sendMessage("该命令只能由玩家执行。");
        }
    }

    private static void reloadConfigs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                sender.sendMessage("你没有权限。");
            } else {
                LiteWorldEdit.instance.reloadConfig();
                sender.sendMessage("已重载配置文件。");
            }
        } else {
            LiteWorldEdit.instance.reloadConfig();
            sender.sendMessage("已重载配置文件。");
        }
    }

    private static void cancerJobs(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LiteWorldEdit.instance.getCache().getQueueOf(player) == null) {
                sender.sendMessage("你没有正在进行的任务。");
            } else {
                LiteWorldEdit.instance.getCache().getQueueOf(player).cancel();
                sender.sendMessage("已取消。");
            }
        } else {
            sender.sendMessage("该命令只能由玩家执行。");
        }
    }

    private static boolean emptyTask(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 3) {
            try {
                Vector2 diagonalPoint = getVector2(sender, args, player);
                if (diagonalPoint == null) return true;
                Empty.empty(player, player.getWorld(), diagonalPoint.pointA, diagonalPoint.pointB);
                sender.sendMessage("已添加任务。");

            } catch (NumberFormatException e) {
                sender.sendMessage("参数错误。");
            }
        } else {
            sender.sendMessage("参数错误。");
        }
        return true;
    }

    private static boolean overlayTask(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 3) {
            try {
                Vector2 diagonalPoint = getVector2(sender, args, player);
                if (diagonalPoint == null) return true;
                ItemStack items_in_hand = player.getInventory().getItemInMainHand();
                if (!items_in_hand.getType().isBlock() || items_in_hand.getType() == Material.AIR) {
                    sender.sendMessage("你手上没有方块。");
                    return true;
                }
                Material material = Material.getMaterial(items_in_hand.getType().name());
                OverLay.overLay(player, player.getWorld(), diagonalPoint.pointA, diagonalPoint.pointB, material);
                sender.sendMessage("已添加任务。");
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage("参数错误。");
            }
        } else {
            sender.sendMessage("参数错误。");
        }
        return true;
    }

    private static boolean fillTask(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 3) {
            try {
                Vector2 diagonalPoint = getVector2(sender, args, player);
                if (diagonalPoint == null) return true;
                ItemStack items_in_hand = player.getInventory().getItemInMainHand();
                if (!items_in_hand.getType().isBlock() || items_in_hand.getType() == Material.AIR) {
                    sender.sendMessage("你手上没有方块。");
                    return true;
                }
                Material material = Material.getMaterial(items_in_hand.getType().name());
                Fill.fill(player, player.getWorld(), diagonalPoint.pointA, diagonalPoint.pointB, material);
                sender.sendMessage("已添加任务。");
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage("参数错误。");
            }
        } else {
            sender.sendMessage("参数错误。");
        }
        return true;
    }

    private static Vector2 getVector2(CommandSender sender, String[] args, Player player) {
        Integer indexA = Integer.parseInt(args[1]);
        Integer indexB = Integer.parseInt(args[2]);
        Map<Integer, Point> points = LiteWorldEdit.instance.getCache().getPoints(player);
        if (points == null) {
            sender.sendMessage("你没有设置任何点。");
            return null;
        }
        Point pointA = points.get(indexA);
        Point pointB = points.get(indexB);
        if (pointA == null || pointB == null) {
            sender.sendMessage("点不存在。");
            return null;
        }
        if (out_of_region(pointA, pointB)) {
            sender.sendMessage("选择的区域不可以超过 " + LiteWorldEdit.instance.getConfigMgr().getXMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getYMax() + "x" + LiteWorldEdit.instance.getConfigMgr().getZMax() + "。");
            return null;
        }
        return new Vector2(pointA, pointB);
    }

    private static class Vector2 {
        public final Point pointA;
        public final Point pointB;

        public Vector2(Point pointA, Point pointB) {
            this.pointA = pointA;
            this.pointB = pointB;
        }
    }

    private static void listPoints(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return;
        }
        Player player = (Player) sender;
        Map<Integer, Point> points = LiteWorldEdit.instance.getCache().getPoints(player);
        if (points != null) {
            sender.sendMessage("你创建的点：");
            for (Map.Entry<Integer, Point> entry : points.entrySet()) {
                Point point = entry.getValue();
                sender.sendMessage(entry.getKey() + ": " + point.x + ", " + point.y + ", " + point.z);
            }
        } else {
            sender.sendMessage("你没有设置任何点。");
        }
    }

    private static boolean addPoint(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return true;
        }
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
                if (!LiteWorldEdit.instance.getCache().addPoint(player, index, point)) {
                    sender.sendMessage("点的数量不允许超过20，请使用已有点序号覆盖已有点。");
                    return true;
                }
                sender.sendMessage("点 " + index + " 已设置为 " + x + ", " + y + ", " + z + "。");
            } catch (NumberFormatException e) {
                sender.sendMessage("参数错误。");
            }
        } else {
            sender.sendMessage("参数错误。");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("point", "p", "points", "fill", "empty", "overlay", "cancel", "pause", "resume", "help", "reload");
        } else if (args.length == 2) {
            if (args[0].equals("point") || args[0].equals("p")) {
                return Collections.singletonList("[点序号(整数)] [x] [y] [z] - 创建点");
            } else if (args[0].equals("fill") || args[0].equals("empty") || args[0].equals("overlay")) {
                return Collections.singletonList("[点序号A] [点序号B]");
            }
        }
        return Collections.emptyList();
    }

    public void print_help(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "LiteWorldEdit 帮助");
        sender.sendMessage(ChatColor.GREEN + "/lwe help - 查看帮助");
        sender.sendMessage(ChatColor.GREEN + "/lwe point|p [点序号(整数)] [x] [y] [z] - 创建点");
        sender.sendMessage(ChatColor.GREEN + "/lwe points - 查看所有点");
        sender.sendMessage(ChatColor.GREEN + "/lwe fill [点序号A] [点序号B] - (在AB点对角线间放置方块 - 需要手持被放置的方块)");
        sender.sendMessage(ChatColor.GREEN + "/lwe empty [点序号A] [点序号B] - (破坏AB点对角线间方块 - 需要拥有下届合金镐)");
        sender.sendMessage(ChatColor.GREEN + "/lwe overlay [点序号A] [点序号B] - (在选区地面上铺一层方块 - 需要手持被放置的方块)");
        sender.sendMessage(ChatColor.GREEN + "/lwe cancel - 取消所有任务");
        sender.sendMessage(ChatColor.GREEN + "/lwe pause - 暂停工作");
        sender.sendMessage(ChatColor.GREEN + "/lwe resume - 恢复工作");
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
