package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.OrderStatusBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;


public class PrintStatusActivity extends BasePrintActivity implements View.OnClickListener {

    private TextView stausTv;
    private RelativeLayout successRyt;
    private RelativeLayout stateRyt;
    private ImageView exceptionImg;
    private ImageView queueImg;


    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_status);
        initData();
        initView();
        setQueueView();
        timer.schedule(task, 1000 * 3, 1000 * 2);
    }

    private void initData() {
        orderId = getIntent().getLongExtra(DocPrintActivity.ORDER_ID, -1);
    }

    private void initView() {
        initTitleBar("打印");
        successRyt = (RelativeLayout) findViewById(R.id.successRyt);
        stateRyt = (RelativeLayout) findViewById(R.id.stateRyt);
        exceptionImg = (ImageView) findViewById(R.id.exceptionImg);
        queueImg = (ImageView) findViewById(R.id.queueImg);
        queueImg.setImageResource(R.drawable.anim_queue);
        AnimationDrawable animationDrawable = (AnimationDrawable) queueImg.getDrawable();
        animationDrawable.start();
        findViewById(R.id.exitTv).setOnClickListener(this);
        findViewById(R.id.errorExitTv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.errorExitTv:
                finish();
                break;
            case R.id.exitTv:
                finish();
                break;
        }
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
        Logger.i("querystate");
    }

    MyHandler handler = new MyHandler(this);
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            PrintRequest.queryOrderStatus(activity, orderId, new HttpListener() {
                @Override
                public void onSucceed(String content) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    OrderStatusBean orderStatusBean = GsonUtil.GsonToBean(content, OrderStatusBean.class);
                    if (orderStatusBean == null) {
                        return;
                    }
                    if (orderStatusBean.isSuccess()) {

                    } else {
//                        ToastUtil.doToast(context, orderStatusBean.getErrorMsg());
//                        stopTimerTask();
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

    private void setSuccessView() {
        successRyt.setVisibility(View.VISIBLE);
        stateRyt.setVisibility(View.GONE);
    }

    private void setExceptionView() {
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        queueImg.setVisibility(View.GONE);
        exceptionImg.setVisibility(View.VISIBLE);
    }

    private void setQueueView() {
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        queueImg.setVisibility(View.VISIBLE);
        exceptionImg.setVisibility(View.GONE);
    }

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PrintStatusActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopTimerTask();
        super.onDestroy();
    }
}
