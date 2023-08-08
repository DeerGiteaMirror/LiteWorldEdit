package site.deercloud.liteworldedit.Managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Job;

import java.util.LinkedList;

public class JobQueue {

    private final LinkedList<Job> queue = new LinkedList<Job>();

    private final BossBar bar = Bukkit.createBossBar("§a§lLiteWorldEdit", BarColor.GREEN, BarStyle.SOLID);

    boolean paused = false;

    int total = 0;

    int ms_last_pop;

    Player player;

    public JobQueue(Player player) {
        this.player = player;
        this.bar.addPlayer(player);
        this.bar.setVisible(false);
    }

    public void add(Job job) {
        queue.add(job);
        total++;
    }

    public Job pop() {
        if (queue.isEmpty()) {
            total = 0;
            return null;
        }
        if (paused) {
            return null;
        }
        Job job = queue.pop();
        updateBarProgressBar();
        ms_last_pop = (int) System.currentTimeMillis();
        return job;
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void pause() {
        bar.setColor(BarColor.YELLOW);
        bar.setTitle("§e§lLiteWorldEdit [暂停] 剩余任务: " + queue.size());
        paused = true;
    }

    public void resume() {
        bar.setColor(BarColor.GREEN);
        paused = false;
    }

    public void cancel() {
        queue.clear();
        total = 0;
        bar.setVisible(false);
        paused = false;
    }

    private void updateBarProgressBar() {
        bar.setProgress((total - queue.size()) * 1.0 / total);
        int delta_time_ms = (int) System.currentTimeMillis() - ms_last_pop;
        int time_s_remaining = queue.size() * delta_time_ms / 1000;
        int time_m_remaining = time_s_remaining / 60;
        int time_s_remaining_mod = time_s_remaining % 60;
        bar.setTitle("§a§lLiteWorldEdit [正在运行] 剩余任务: " + queue.size() + " 预计剩余时间: " + time_m_remaining + "分" + time_s_remaining_mod + "秒");
        bar.setVisible(queue.size() != 0);
    }

}
