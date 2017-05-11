package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.OrderStatusResp;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.CommentActivity;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;


public class PrintStatusActivity extends BasePrintActivity implements View.OnClickListener {

    private TextView stateTv;
    private TextView stateDetailTv;
    private RelativeLayout successRyt;
    private RelativeLayout stateRyt;
    private LinearLayout exceptionLyt;
    private ImageView animImg;

    private OrderStatusResp orderStatusResp;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_status);
        initData();
        initView();
        setUpload();
        timer.schedule(task, 1000 * 3, 1000 * 2);
    }

    private void initData() {
        orderId = getIntent().getExtras().getLong(ORDER_ID);
    }

    private void initView() {
        initTitleBar("打印");
        successRyt = (RelativeLayout) findViewById(R.id.successRyt);
        stateRyt = (RelativeLayout) findViewById(R.id.stateRyt);
        exceptionLyt = (LinearLayout) findViewById(R.id.exceptionLyt);

        animImg = (ImageView) findViewById(R.id.animImg);
        stateTv = (TextView) findViewById(R.id.stateTv);
        stateDetailTv = (TextView) findViewById(R.id.stateDetailTv);
        findViewById(R.id.exitTv).setOnClickListener(this);
        findViewById(R.id.errorExitTv).setOnClickListener(this);
        findViewById(R.id.printTv).setOnClickListener(this);
        findViewById(R.id.shareTv).setOnClickListener(this);
        findViewById(R.id.commentTv).setOnClickListener(this);
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
            case R.id.printTv:
                finish();
                break;
            case R.id.shareTv:
                ShowUtil.showToast("shareTv");
                break;
            case R.id.commentTv:
                Bundle bundle = new Bundle();
                bundle.putLong("order_id", orderId);
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
        OrderStatusResp.OrderStatus orderStatus = orderStatusResp.getData();
        if (orderStatus.isNeedAwake()) {
            setAwake();
        } else if (orderStatus.getWaitingCount() > 0) {
            setQueueView(orderStatus.getWaitingCount());
        } else {
            switch (orderStatus.getStatus()) {
                //正在打印
                case 0:
                    setExceptionView();
                    break;
                //打印成功
                case 1:
                    stopTimerTask();
                    setSuccessView();
                    break;
                //打印失败
                case 2:
                    setExceptionView();
                    break;
            }

        }
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
                    orderStatusResp = GsonUtil.GsonToBean(content, OrderStatusResp.class);
                    if (orderStatusResp != null && orderStatusResp.isSuccess()) {

                    } else {
                        Logger.i(orderStatusResp.getErrorMsg());
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
        exceptionLyt.setVisibility(View.GONE);
    }

    private void setExceptionView() {
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.VISIBLE);

        animImg.setImageResource(R.drawable.ic_exception);
        stateTv.setText("打印异常");
        stateDetailTv.setText("很抱歉打印机发生了故障，暂停服务");
    }

    private void setAwake() {
        animImg.setImageResource(R.drawable.anim_awake);
        AnimationDrawable queueAnum = (AnimationDrawable) animImg.getDrawable();
        queueAnum.start();
        stateTv.setText("文件发送成功，打印机预热中...");
        stateDetailTv.setText("");
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.GONE);
    }

    private void setUpload() {
        animImg.setImageResource(R.drawable.anim_upload);
        AnimationDrawable queueAnum = (AnimationDrawable) animImg.getDrawable();
        queueAnum.start();
        stateTv.setText("文件上传中");
        stateDetailTv.setText("");
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.GONE);
    }

    private void setQueueView(int count) {
        animImg.setImageResource(R.drawable.anim_queue);
        AnimationDrawable queueAnum = (AnimationDrawable) animImg.getDrawable();
        queueAnum.start();
        stateTv.setText("发送文件排队中");
        stateDetailTv.setText("前面有" + count + "个打印任务,请耐心等待");
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.GONE);
    }

    public static final String ORDER_ID = "order_id";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PrintStatusActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopTimerTask();
        super.onDestroy();
    }
}
