package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.Managers.Cache;
import cn.lunadeer.liteworldedit.Managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LiteWorldEdit extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        config = new ConfigManager();
        _cache = new Cache();

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("LiteWorldEdit")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("LiteWorldEdit")).setTabCompleter(new Commands());

        Metrics metrics = new Metrics(this, 21436);
        if (config.isCheckUpdate()) {
            giteaReleaseCheck = new GiteaReleaseCheck(this,
                    "https://ssl.lunadeer.cn:14446",
                    "zhangyuheng",
                    "LiteWorldEdit");
        }

        LoggerX.info("LiteWorldEdit 已加载");
        LoggerX.info("版本: " + getPluginMeta().getVersion());
        LoggerX.info("");
        // https://patorjk.com/software/taag/#p=display&f=Big&t=LiteWorldEdit
        LoggerX.info(" _      _ _    __          __        _     _ ______    _ _ _   ");
        LoggerX.info("| |    (_) |   \\ \\        / /       | |   | |  ____|  | (_) |  ");
        LoggerX.info("| |     _| |_ __\\ \\  /\\  / /__  _ __| | __| | |__   __| |_| |_ ");
        LoggerX.info("| |    | | __/ _ \\\\/  \\/ / _ \\| '__| |/ _` |  __| / _` | | __|");
        LoggerX.info("| |____| | ||  __/\\  /\\  / (_) | |  | | (_| | |___| (_| | | |_ ");
        LoggerX.info("|______|_|\\__\\___| \\/  \\/ \\___/|_|  |_|\\__,_|______\\__,_|_|\\__|");
        LoggerX.info("");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigMgr() {
        return config;
    }

    public Cache getCache() {
        return _cache;
    }

    public static LiteWorldEdit instance;
    public static ConfigManager config;
    private Cache _cache;
    private GiteaReleaseCheck giteaReleaseCheck;
}
