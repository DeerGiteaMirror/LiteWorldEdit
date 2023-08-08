package site.deercloud.liteworldedit;

import org.bukkit.entity.Player;
import site.deercloud.liteworldedit.Jobs.Job;
import site.deercloud.liteworldedit.Jobs.JobErrCode;

public class Task implements Runnable{
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */

    private final XPlayer xPlayer;

    Task(XPlayer player) {
        this.xPlayer = player;
    }

    @Override
    public void run() {
        Job job = this.xPlayer.popJob();
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
                job = this.xPlayer.popJob();
                if (job == null) {
                    return;
                }
            } else {
                Player player = job.get_creator();
                player.sendTitle("§c错误 任务已自动暂停", "§c" + re.getMessage(), 10, 70, 20);
                this.xPlayer.pauseJob();
                return;
            }
        }
    }
}
