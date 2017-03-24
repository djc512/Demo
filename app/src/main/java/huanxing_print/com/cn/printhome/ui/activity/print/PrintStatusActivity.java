package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.OrderStatusBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;


public class PrintStatusActivity extends BasePrintActivity {


    private TextView stausTv;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_status);
        initData();
        initView();
        timer.schedule(task, 1000 * 3, 1000 * 2);
    }

    private void initData() {
        orderId = getIntent().getLongExtra(DocPrintActivity.ORDER_ID, -1);
    }

    static class MyHandler extends Handler {

        WeakReference mActivity;

        MyHandler(PrintStatusActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PrintStatusActivity theActivity = (PrintStatusActivity) mActivity.get();
            switch (msg.what) {
                case 1:
                    if (theActivity != null) {
                        theActivity.update();
                    }
                    break;
            }
        }
    }

    public void update() {
        if (stausTv != null) {
            stausTv.setText(SystemClock.currentThreadTimeMillis() + "");
        }
    }

    MyHandler handler = new MyHandler(this);
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            PrintRequest.queryOrderStatus(activity, orderId, new HttpListener() {
                @Override
                public void onSucceed(String content) {
                    OrderStatusBean orderStatusBean = GsonUtil.GsonToBean(content, OrderStatusBean.class);
                    if (orderStatusBean == null) {
                        return;
                    }
                    if (orderStatusBean.isSuccess()) {

                    } else {
                        ToastUtil.doToast(context, orderStatusBean.getErrorMsg());
                        stopTimerTask();
                    }
                }

                @Override
                public void onFailed(String exception) {
                    ShowUtil.showToast(getString(R.string.net_error));
                    stopTimerTask();
                }
            });
        }
    };

    private void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopTimerTask();
        super.onDestroy();
    }

    private void initView() {
        stausTv = (TextView) findViewById(R.id.stausTv);
    }
}
