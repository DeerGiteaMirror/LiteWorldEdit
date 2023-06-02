package site.deercloud.liteworldedit.Managers;

import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Job;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Cache {
    private final Map<String, Map<Integer, Point>> _points;
    private final Map<String, LinkedList<Job>> _jobs;

    public Cache() {
        _points = new HashMap<String, Map<Integer, Point>>();
        _jobs = new HashMap<String, LinkedList<Job>>();
    }

    public void add_point(Player player, Integer index, Point point) {
        if (!_points.containsKey(player.getUniqueId().toString())) {
            _points.put(player.getUniqueId().toString(), new HashMap<Integer, Point>());
        }
        _points.get(player.getUniqueId().toString()).put(index, point);
    }

    public void add_job(Player player, Job job) {
        if (!_jobs.containsKey(player.getUniqueId().toString())) {
            _jobs.put(player.getUniqueId().toString(), new LinkedList<Job>());
        }
        _jobs.get(player.getUniqueId().toString()).add(job);
    }

    public Job get_one_job() {
        for (Map.Entry<String, LinkedList<Job>> entry : _jobs.entrySet()) {
            if (entry.getValue().size() > 0) {
                return entry.getValue().removeFirst();
            }
        }
        return null;
    }
    public Map<Integer, Point> get_points(Player player) {
        return _points.get(player.getUniqueId().toString());
    }

    public void delete_player_jobs(Player player) {
        _jobs.remove(player.getUniqueId().toString());
    }

    public void delete_player_points(Player player) {
        _points.remove(player.getUniqueId().toString());
    }
}
