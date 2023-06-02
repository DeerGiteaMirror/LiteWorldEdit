package site.deercloud.liteworldedit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import site.deercloud.liteworldedit.Jobs.Job;
import site.deercloud.liteworldedit.Managers.ConfigManager;
import site.deercloud.liteworldedit.Managers.Cache;

import java.util.Objects;

public final class LiteWorldEdit extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        _config = new ConfigManager();
        _cache = new Cache();

        new BukkitRunnable() {
            @Override
            public void run() {
                Job job = _cache.get_one_job();
                if (job != null) {
                    job.Do();
                }
            }
        }.runTaskTimer(this, 0, 1);

        Objects.requireNonNull(Bukkit.getPluginCommand("LiteWorldEdit")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("LiteWorldEdit")).setTabCompleter(new Commands());

        LoggerX.info("插件启动成功。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigMgr() {
        return _config;
    }

    public Cache getCache() {
        return _cache;
    }

    public static LiteWorldEdit instance;
    private ConfigManager _config;
    private Cache _cache;
}
