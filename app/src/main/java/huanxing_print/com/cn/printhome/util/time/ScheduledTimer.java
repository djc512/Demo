package huanxing_print.com.cn.printhome.util.time;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTimer implements Runnable {
    private ScheduledExecutorService executorService;

    private ScheduledFuture future;

    private int corePoolSize = 1;

    private volatile int times = 0;

    private int maxTimes;

    private int initialDelay;

    private int delay;

    private boolean isSingerTask;

    private String TAG = "ScheduledTimer";

    private ScheduledTask scheduledTask;

    private ScheduledHandler scheduledHandler;

    public static final int NEVER_STOP = 0;

    public ScheduledTimer(int initialDelay) {
        this(null, initialDelay, initialDelay, 1);

    }

    public ScheduledTimer(int initialDelay, int delay, int maxTimes) {
        this(null, initialDelay, delay, maxTimes);

    }

    public ScheduledTimer(ScheduledTask scheduledTask, int initialDelay, int delay,
                 int maxTimes) {
        this.scheduledTask = scheduledTask;
        this.maxTimes = maxTimes;
        this.delay = delay;
        this.initialDelay = initialDelay;
        executorService = Executors.newScheduledThreadPool(corePoolSize);

    }

    public ScheduledTimer(ScheduledHandler scheduledHandler, int initialDelay,
                 int delay, int maxTimes, boolean... flags) {
        this.scheduledHandler = scheduledHandler;
        this.maxTimes = maxTimes;
        this.delay = delay;
        this.initialDelay = initialDelay;

        if (null != flags && flags.length > 0) {
            this.isSingerTask = flags[0];
        }

        executorService = Executors.newScheduledThreadPool(corePoolSize);

    }

    public void start() {
        cancel();
        future = executorService.scheduleWithFixedDelay(this, initialDelay,
                delay, TimeUnit.MILLISECONDS);

    }

    public void cancel() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }

    }

    @Override
    public void run() {
        times++;

        if (scheduledTask != null) {
            scheduledTask.run(times);
        }
        if (scheduledHandler != null) {
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);
        }
        if (maxTimes != 0 && times >= maxTimes) {
            if (future != null) {
                future.cancel(true);
            }

            if (scheduledTask != null) {
                scheduledTask.end();
            }
            if (scheduledHandler != null) {
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            }
        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (scheduledHandler != null) {
                    scheduledHandler.post(times);
                }
            }
            if (msg.what == 2) {
                if (scheduledHandler != null) {
                    scheduledHandler.end();
                }

            }
        }
    };

}
