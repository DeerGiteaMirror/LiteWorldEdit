package site.deercloud.liteworldedit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import site.deercloud.liteworldedit.Managers.Cache;
import site.deercloud.liteworldedit.Managers.ConfigManager;

import java.util.Objects;

public final class LiteWorldEdit extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        _config = new ConfigManager();
        _cache = new Cache();

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
