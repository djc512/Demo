package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.event.print.PrintTypeEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.OrderStatusResp;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.CommentActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.CopyActivity;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.WeiXinUtils;


public class PrintStatusActivity extends BasePrintActivity implements View.OnClickListener {

    private TextView stateTv;
    private TextView stateDetailTv;
    private RelativeLayout successRyt;
    private RelativeLayout stateRyt;
    private LinearLayout exceptionLyt;
    private ImageView animImg;

    private OrderStatusResp orderStatusResp;
    private PrintInfoResp.PrinterPrice printerPrice;
    private long orderId;
    private int awakeAccount = 0;

    private PrintTypeEvent printTypeEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_status);
        EventBus.getDefault().register(context);
        initData();
        initView();
        setUpload();
        timer.schedule(task, 1000 * 3, 1000 * 2);
    }

    private void initData() {
        orderId = getIntent().getExtras().getLong(ORDER_ID);
        printerPrice = getIntent().getExtras().getParcelable(PRINTER_PRICE);
        Logger.i(printerPrice.toString());
        Logger.i(orderId);
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
        findViewById(R.id.backImg).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(PrintTypeEvent printTypeEvent) {
        this.printTypeEvent = printTypeEvent;
        Logger.i(printTypeEvent.toString());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backImg:
                finishThis();
                break;
            case R.id.errorExitTv:
                finish();
                break;
            case R.id.exitTv:
                finishThis();
                break;
            case R.id.printTv:
                finish();
                break;
            case R.id.shareTv:
                WeiXinUtils weiXinUtils = WeiXinUtils.getInstance();
                weiXinUtils.init(this, BaseApplication.getInstance().WX_APPID);
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.appicon_print);
                weiXinUtils.shareToWxSceneSession(String.format("%s邀请您使用印家打印", BaseApplication.getInstance()
                        .getNickName()), "我在用印家打印APP,打印、办公非常方便,快来下载吧", "https://www.baidu.com", bmp);
                break;
            case R.id.commentTv:
                Bundle bundle = new Bundle();
                bundle.putLong("order_id", orderId);
                bundle.putString("printNum", printerPrice.getPrinterNo());
                bundle.putString("location", printerPrice.getPrintAddress());
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void finishThis() {
        if (PrintTypeEvent.TYPE_COPY == printTypeEvent.getType()) {
            startActivity(new Intent(context, CopyActivity.class));
        } else {
            startActivity(new Intent(context, MainActivity.class));
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
        if (orderStatus.isNeedAwake() && awakeAccount < 3) {
            awakeAccount++;
            setAwake();
        } else if (orderStatus.getWaitingCount() > 0) {
            setQueueView(orderStatus.getWaitingCount());
        } else {
            switch (orderStatus.getStatus()) {
                //正在打印
                case 0:
                    setUpload();
                    break;
                //打印成功
                case 1:
                    stopTimerTask();
                    setSuccessView();
                    break;
                //打印失败
                case 2:
                    stopTimerTask();
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
        stateTv.setText("文件发送中");
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

    public static final String ORDER_ID = "orderId";
    public static final String PRINTER_PRICE = "printerPrice";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PrintStatusActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopTimerTask();
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(context);
    }
}
