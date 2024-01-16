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

        String logo = "LiteWorldEdit 已加载 版本: " + getPluginMeta().getVersion() + "\n";
        // https://patorjk.com/software/taag/#p=display&f=Big&t=LiteWorldEdit
        logo += " _      _ _    __          __        _     _ ______    _ _ _   \n";
        logo += "| |    (_) |   \\ \\        / /       | |   | |  ____|  | (_) |  \n";
        logo += "| |     _| |_ __\\ \\  /\\  / /__  _ __| | __| | |__   __| |_| |_ \n";
        logo += "| |    | | __/ _ \\\\/  \\/ / _ \\| '__| |/ _` |  __| / _` | | __|\n";
        logo += "| |____| | ||  __/\\  /\\  / (_) | |  | | (_| | |___| (_| | | |_ \n";
        logo += "|______|_|\\__\\___| \\/  \\/ \\___/|_|  |_|\\__,_|______\\__,_|_|\\__|\n";
        logo += "\n";
        LoggerX.info(logo);
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
