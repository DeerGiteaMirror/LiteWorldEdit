package site.deercloud.liteworldedit;

import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Job;
import site.deercloud.liteworldedit.Managers.JobQueue;
import site.deercloud.liteworldedit.Managers.Point;

import java.util.HashMap;
import java.util.Map;

import static site.deercloud.liteworldedit.SchedulerUtil.runAtFixedRateEntity;

public class XPlayer {

    private final Map<Integer, Point> points;
    private final Player player;
    private final JobQueue queue;
    private final Task task;

    public XPlayer(Player player) {
        this.player = player;
        this.points = new HashMap<>();
        this.queue = new JobQueue(player);
        this.task = new Task(this);
        runAtFixedRateEntity(player, LiteWorldEdit.instance, task, 1);
    }

    public void addJob(Job job) {
        queue.add(job);
    }

    public Job popJob() {
        return queue.pop();
    }

    public void pauseJob() {
        queue.pause();
    }

    public void resumeJob() {
        queue.resume();
    }

    public void cancelJob() {
        queue.cancel();
    }

    public boolean hasJob() {
        return !queue.isEmpty();
    }

    public boolean addPoint(Integer index, Point point) {
        if (points.size() >= 20) {
            return false;
        }
        points.put(index, point);
        return true;
    }

    public Map<Integer, Point> getPoints() {
        return points;
    }
}
