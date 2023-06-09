package site.deercloud.liteworldedit.Managers;

import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Job;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    private final Map<String, Map<Integer, Point>> _points;
    private final Map<String, JobQueue> _jobs;
    private String _last_jobs_player;   // 上一次被获取任务的玩家uuid

    public Cache() {
        _points = new HashMap<String, Map<Integer, Point>>();
        _jobs = new HashMap<String, JobQueue>();
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
            _jobs.put(player.getUniqueId().toString(), new JobQueue(player));
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
        return _jobs.get(player).pop();
    }

    public Map<Integer, Point> getPoints(Player player) {
        return _points.get(player.getUniqueId().toString());
    }

    public JobQueue getQueueOf(Player player) {
        if (!_jobs.containsKey(player.getUniqueId().toString())) {
            return null;
        }
        if (_jobs.get(player.getUniqueId().toString()).isEmpty()) {
            return null;
        }
        return _jobs.get(player.getUniqueId().toString());
    }

    public void deletePlayerCache(Player player) {
        _jobs.remove(player.getUniqueId().toString());
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
                // 忽略没有任务的玩家
                if (_jobs.get(key).size() == 0) {
                    continue;
                }
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
