package cn.lunadeer.liteworldedit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Notification {
    private static final Style i_style = Style.style(TextColor.color(139, 255, 123));
    private static final Style w_style = Style.style(TextColor.color(255, 185, 69));
    private static final Style e_style = Style.style(TextColor.color(255, 96, 72));

    private static final String prefix = "[LWE] ";

    public static void info(Player player, String msg) {
        player.sendMessage(Component.text(prefix + msg, i_style));
    }

    public static void warn(Player player, String msg) {
        player.sendMessage(Component.text(prefix + msg, w_style));
    }

    public static void error(Player player, String msg) {
        player.sendMessage(Component.text(prefix + msg, e_style));
    }

    public static void info(CommandSender sender, String msg) {
        sender.sendMessage(Component.text(prefix + msg, i_style));
    }

    public static void warn(CommandSender sender, String msg) {
        sender.sendMessage(Component.text(prefix + msg, w_style));
    }

    public static void error(CommandSender sender, String msg) {
        sender.sendMessage(Component.text(prefix + msg, e_style));
    }

    public static void info(Player player, Component msg) {
        player.sendMessage(Component.text(prefix, i_style).append(msg));
        if (LiteWorldEdit.config.isDebug())
            LoggerX.debug("来自玩家[ " + player.getName() + " ] 的提示 | " + msg);
    }

    public static void warn(Player player, Component msg) {
        player.sendMessage(Component.text(prefix, w_style).append(msg));
        if (LiteWorldEdit.config.isDebug())
            LoggerX.debug("来自玩家[ " + player.getName() + " ] 的警告 | " + msg);
    }

    public static void error(Player player, Component msg) {
        player.sendMessage(Component.text(prefix, e_style).append(msg));
        if (LiteWorldEdit.config.isDebug())
            LoggerX.debug("来自玩家[ " + player.getName() + " ] 的报错 | " + msg);
    }

    public static void info(CommandSender player, Component msg) {
        player.sendMessage(Component.text(prefix, i_style).append(msg));
    }

    public static void warn(CommandSender player, Component msg) {
        player.sendMessage(Component.text(prefix, w_style).append(msg));
    }

    public static void error(CommandSender player, Component msg) {
        player.sendMessage(Component.text(prefix, e_style).append(msg));
    }

    private static void sendTitle(Player player, Component title, Component subtitle) {
        Title title_t = Title.title(title, subtitle);
        player.showTitle(title_t);
    }

    public static void titleInfo(Player player, String title, String subtitle) {
        Component title_c = Component.text(title, i_style);
        Component subtitle_c = Component.text(subtitle, i_style);
        sendTitle(player, title_c, subtitle_c);
    }

    public static void titleWarn(Player player, String title, String subtitle) {
        Component title_c = Component.text(title, w_style);
        Component subtitle_c = Component.text(subtitle, w_style);
        sendTitle(player, title_c, subtitle_c);
    }

    public static void titleError(Player player, String title, String subtitle) {
        Component title_c = Component.text(title, e_style);
        Component subtitle_c = Component.text(subtitle, e_style);
        sendTitle(player, title_c, subtitle_c);
    }
}
