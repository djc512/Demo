package huanxing_print.com.cn.printhome.util.time;

public interface ScheduledTask {
    public void run(int times);

    public void end();
}