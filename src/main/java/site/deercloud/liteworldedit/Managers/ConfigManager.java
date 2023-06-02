package site.deercloud.liteworldedit.Managers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.configuration.file.FileConfiguration;
import site.deercloud.liteworldedit.LiteWorldEdit;

public class ConfigManager {
    public ConfigManager() {
        _plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        _plugin.reloadConfig();
        _file = _plugin.getConfig();
        _debug = _file.getBoolean("Debug", false);
        _x_max = _file.getInt("MaxX", 64);
        _y_max = _file.getInt("MaxY", 64);
        _z_max = _file.getInt("MaxZ", 64);
    }

    public Boolean isDebug() {
        return _debug;
    }

    public void setDebug(Boolean debug) {
        _debug = debug;
        _file.set("Debug", debug);
        _plugin.saveConfig();
    }

    public Integer getXMax() {
        return _x_max;
    }

    public Integer getYMax() {
        return _y_max;
    }

    public Integer getZMax() {
        return _z_max;
    }

    public void setMaxSize(Integer x, Integer y, Integer z) {
        _x_max = x;
        _y_max = y;
        _z_max = z;
        _file.set("MaxX", x);
        _file.set("MaxY", y);
        _file.set("MaxZ", z);
        _plugin.saveConfig();
    }


    private final LiteWorldEdit _plugin = LiteWorldEdit.instance;
    private FileConfiguration _file;

    private Boolean _debug;

    private Integer _x_max;
    private Integer _y_max;
    private Integer _z_max;
}
