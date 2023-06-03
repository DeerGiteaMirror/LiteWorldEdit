package site.deercloud.liteworldedit.Managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Job;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Cache {
    private final Map<String, Map<Integer, Point>> _points;
    private final Map<String, LinkedList<Job>> _jobs;
    private String _last_jobs_player;   // 上一次被获取任务的玩家uuid
    private final Map<String, BossBar> _bars;

    public Cache() {
        _points = new HashMap<String, Map<Integer, Point>>();
        _jobs = new HashMap<String, LinkedList<Job>>();
        _bars = new HashMap<String, BossBar>();
    }

    public boolean addPoint(Player player, Integer index, Point point) {
        if (!_points.containsKey(player.getUniqueId().toString())) {
            _points.put(player.getUniqueId().toString(), new HashMap<Integer, Point>());
        }
        if (_points.get(player.getUniqueId().toString()).size() >= 20) {
            return false;
        }
        _points.get(player.getUniqueId().toString()).put(index, point);
        return true;
    }

    public void addJob(Player player, Job job) {
        if (!_jobs.containsKey(player.getUniqueId().toString())) {
            _jobs.put(player.getUniqueId().toString(), new LinkedList<Job>());
        }
        _jobs.get(player.getUniqueId().toString()).add(job);
    }

    public Job getOneJob() {
        String player = getNextPlayer();
        if (player == null) {
            return null;
        }
        if (!_jobs.containsKey(player)) {
            return null;
        }
        if (_jobs.get(player).isEmpty()) {
            _jobs.remove(player);
            return null;
        }
        Job job = _jobs.get(player).pop();
        updateBarOfPlayer(job.get_creator());
        return job;
    }

    public Map<Integer, Point> getPoints(Player player) {
        return _points.get(player.getUniqueId().toString());
    }

    public void deleteAllJobsOfPlayer(Player player) {
        _jobs.get(player.getUniqueId().toString()).clear();
        updateBarOfPlayer(player);
    }

    public void deletePlayerCache(Player player) {
        // _points.remove(player.getUniqueId().toString());
        _jobs.remove(player.getUniqueId().toString());
        _bars.remove(player.getUniqueId().toString());
    }

    public void updateBarOfPlayer(Player player) {
        if (!_bars.containsKey(player.getUniqueId().toString())) {
            _bars.put(player.getUniqueId().toString(), Bukkit.createBossBar("§a§lLiteWorldEdit 施工进度", BarColor.GREEN, BarStyle.SOLID));
            _bars.get(player.getUniqueId().toString()).addPlayer(player);
        }
        BossBar bar = _bars.get(player.getUniqueId().toString());
        bar.setProgress(1);
        bar.setTitle("§a§lLiteWorldEdit 施工进度 剩余任务: " + _jobs.get(player.getUniqueId().toString()).size());
        bar.setVisible(_jobs.get(player.getUniqueId().toString()).size() != 0);
    }

    private String getNextPlayer() {
        if (_jobs.size() == 0) {
            return null;
        }
        if (_last_jobs_player == null) {
            _last_jobs_player = _jobs.keySet().iterator().next();
            return _last_jobs_player;
        }
        boolean found = false;
        for (String key : _jobs.keySet()) {
            if (found) {
                _last_jobs_player = key;
                return key;
            }
            if (key.equals(_last_jobs_player)) {
                found = true;
            }
        }
        _last_jobs_player = _jobs.keySet().iterator().next();
        return _last_jobs_player;
    }
}
