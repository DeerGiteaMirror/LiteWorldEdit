package cn.lunadeer.liteworldedit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class SchedulerUtil {

    private static Boolean IS_FOLIA = null;

    private static boolean tryFolia() {
        try {
            Bukkit.getAsyncScheduler();
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static Boolean isFolia() {
        if (IS_FOLIA == null) IS_FOLIA = tryFolia();
        return IS_FOLIA;
    }

    public static void runLaterEntity(Entity entity, Plugin plugin, Runnable runnable, int ticks) {
        if (isFolia()) entity.getScheduler().runDelayed(plugin, (task) -> runnable.run(), null, ticks);
        else Bukkit.getScheduler().runTaskLater(plugin, runnable, ticks);
    }

    public static void runAtFixedRateEntity(Entity entity, Plugin plugin, Runnable runnable, int ticks) {
        if (isFolia()) entity.getScheduler().runAtFixedRate(plugin, (task) -> runnable.run(), null, ticks, ticks);
        else Bukkit.getScheduler().runTaskTimer(plugin, runnable, ticks, ticks);
    }
}
