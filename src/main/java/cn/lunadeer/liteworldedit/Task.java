package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.Jobs.Job;
import cn.lunadeer.liteworldedit.Jobs.JobErrCode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class Task implements Runnable {
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
        for (int i = 0; i < LiteWorldEdit.config.getMultiplier(); i++) {
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
                Player player = job.get_creator();
                if (re.canContinue()) {
                    TextComponent warn = Component.text("警告：" + re.getMessage(), Style.style(TextColor.color(TextColor.color(255, 185, 69)), TextDecoration.BOLD));
                    player.sendActionBar(warn);
                    job = this.xPlayer.popJob();
                    if (job == null) {
                        return;
                    }
                } else {
                    Notification.titleError(player, "错误 任务已自动暂停", re.getMessage());
                    this.xPlayer.addJob(job);   // 任务暂停 将没有执行的任务重新加入队列
                    this.xPlayer.pauseJob();
                    return;
                }
            }
        }
    }
}
