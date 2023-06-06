package site.deercloud.liteworldedit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import site.deercloud.liteworldedit.Jobs.Job;
import site.deercloud.liteworldedit.Jobs.JobErrCode;
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
                Job job = _cache.getOneJob();
                if (job == null) {
                    return;
                }
                // 如果任务不可执行 允许在一个tick内多次执行直到任务可执行
                int max_retries = 100;
                JobErrCode re;
                while ((re = job.Do()) != JobErrCode.OK) {
                    max_retries--;
                    if (max_retries <= 0) {
                        break;
                    }
                    if (re.canContinue()) {
                        job.get_creator().sendTitle("§e警告", "§e" + re.getMessage(), 10, 70, 20);
                        job = _cache.getOneJob();
                        if (job == null) {
                            return;
                        }
                    } else {
                        Player player = job.get_creator();
                        player.sendTitle("§c错误 任务已自动暂停", "§c" + re.getMessage(), 10, 70, 20);
                        _cache.getQueueOf(player).pause();
                        return;
                    }
                }
            }
        }.runTaskTimer(this, 0, 1);

        Bukkit.getPluginManager().registerEvents(new Events(), this);
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
