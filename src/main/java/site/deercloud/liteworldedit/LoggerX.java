package site.deercloud.liteworldedit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class LoggerX {
    private static final LiteWorldEdit _plugin = LiteWorldEdit.instance;
    private static final Logger _logger = _plugin.getLogger();

    public static void info(Player player, String message) {
        player.sendMessage(ChatColor.GREEN + " LWE I | " + message);
        if (_plugin.getConfigMgr().isDebug())
            debug("来自玩家[ " + player.getName() + " ] 的信息 | " + message);
    }

    public static void info(String message) {
        _logger.info(ChatColor.GREEN + " I | " + message);
    }

    public static void warn(Player player, String message) {
        player.sendMessage(ChatColor.YELLOW + " LWE W | " + message);
        if (_plugin.getConfigMgr().isDebug())
            debug("来自玩家[ " + player.getName() + " ] 的警告 | " + message);
    }

    public static void warn(String message) {
        _logger.info(ChatColor.YELLOW + " W | " + message);
    }

    public static void err(Player player, String message) {
        player.sendMessage(ChatColor.RED + " LWE E | " + message);
        if (_plugin.getConfigMgr().isDebug())
            debug("来自玩家[ " + player.getName() + " ] 的报错 | " + message);
    }

    public static void err(String message) {
        _logger.info(ChatColor.RED + " E | " + message);
    }

    public static void debug(Player player, String message) {
        if (!_plugin.getConfigMgr().isDebug()) return;
        if (player.isOp())
            player.sendMessage(ChatColor.BLUE + " LWE D | " + message);
        debug("来自玩家[ " + player.getName() + " ] 的调试 | " + message);
    }

    public static void debug(String message) {
        _logger.info(ChatColor.BLUE + " D | " + message);
    }
}
